package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.db.PatientDB;
import com.jetbrains.uzair.db.VisitDB;
import com.jetbrains.uzair.model.ValidationException;
import com.jetbrains.uzair.model.Visit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class VisitPanel {
    //clinic letterhead — update these for your clinic
    private static final String CLINIC_NAME = "Changez Clinic";
    private static final String CLINIC_ADDRESS = "Main Bazaar, Chak Beli Khan, Rawalpindi";
    private static final String CLINIC_CONTACT = "Tel: 0311-5116317   |   asimhort1@gmail.com";
    //put a logo.png on the classpath (e.g. src/main/resources/logo.png) to have it appear automatically
    private static final String LOGO_RESOURCE = "/img/Clinic-Colored.png";
    private static BufferedImage clinicLogo;
    private static boolean logoLoadAttempted = false;
    public static JPanel mainWindow(JFrame window){
        JPanel panel = new JPanel();

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        //search
        PlaceholderTextField searchField = new PlaceholderTextField("Search Visit");

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(255, 255, 255));
        searchField.setOpaque(true);
        //table
        JTable table = commonUI.getDBData(window, 2);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        //buttons
        JButton btnVisit = editButton1(table, window);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(_ -> commonUI.search(table, searchField.getText(), 2));
        searchField.addActionListener(_ -> btnSearch.doClick());
        JButton btnDelete = commonUI.deleteButton(table, window, 2);
        JButton btnView = viewVisit(table, window);
        JButton btnRef = new JButton("Refresh");
        btnRef.addActionListener(_ -> commonUI.refresh(table, 2));
        commonUI.buttonHighlight(btnVisit, btnView, btnDelete, table);
        btnRef.setMnemonic('R');
        btnDelete.setMnemonic('D');
        //toolbar
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        toolbar.setBackground(new Color(33, 69, 126));
        toolbar.add(btnView);
        if (UserSession.isAdmin()){toolbar.add(btnVisit);}
        toolbar.add(Box.createHorizontalStrut(20));
        if (UserSession.isAdmin()){toolbar.add(btnDelete);}
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnRef);
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        //main layout
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
    public static void commonForum( JFrame window, int patientId, int id, JTable table) throws SQLException{
        Visit v;
        String name = "Error";
        String treatS;
        String payS;
        String illS;
        String t;
        if(id != -1){
            v = VisitDB.returnUISingle(id);
            t = "Edit Visit Details";
            treatS = v.getTreatment();
            payS = Integer.toString(v.getPaid());
            illS = v.getIll();
        }else{
            v = new Visit();
            t =  "Add Visit Details";
            treatS = null;
            payS = null;
            illS = null;
        }

        Font valueFont = new Font("Segoe UI", Font.PLAIN, 13);
        JDialog forum = new JDialog(window,t, true);
        forum.setSize(700, 750);
        forum.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        forum.setLocationRelativeTo(null);
        forum.setLayout(new BorderLayout());

        try{name = PatientDB.getName(patientId);} catch (SQLException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
        //header
        JPanel header = commonUI.commonHeader(t);
        //Form panel
        JPanel formPanel = commonUI.commonForum();

        int row = 0;
        GridBagConstraints gbc = commonUI.commonFormGrid(formPanel, row);
        JTextField nameT = new JTextField(name);
        nameT.setEditable(false);
        formPanel.add(nameT, gbc);
        row++;
        //illness
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Illness:"), gbc);
        gbc.gridx = 1;
        JTextField ill = new JTextField(illS, 40);
        formPanel.add(ill, gbc);
        ill.requestFocus();
        row++;
//treatment
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Treatment:"), gbc);
        gbc.gridx = 1;
        JTextArea treatArea = new JTextArea(treatS, 20, 40);
        JScrollPane treat = new JScrollPane(treatArea);
        formPanel.add(treat, gbc);
        row++;
//payment
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Payment:"), gbc);

        gbc.gridx = 1;
        JTextField pay = new JTextField(payS, 40);
        formPanel.add(pay, gbc);
//button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        buttonPanel.setBackground(new Color(12, 38, 78));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        btnSave.addActionListener(_ -> {
            try {
                String[] raw = {
                        String.valueOf(v.getId()),
                        Integer.toString(patientId),
                        ill.getText(),
                        treatArea.getText(),
                        pay.getText()
                };
                if(id != -1){
                    VisitDB.edit(Visit.check(Visit.convArrayToOb(raw)));
                }else {
                    VisitDB.insert(Visit.check(Visit.convArrayToOb(raw)));
                }
                commonUI.refresh(table, 1);
                forum.dispose();
            } catch (ValidationException| SQLException e) {
                JOptionPane.showMessageDialog(forum, e.getMessage());
            }
        });
        FocusUtils.enableArrowNavigation(nameT, ill, treat, pay);
        forum.getRootPane().setDefaultButton(btnSave);
        btnCancel.addActionListener(_ -> forum.dispose());
        forum.add(header, BorderLayout.NORTH);
        forum.add(formPanel, BorderLayout.CENTER);
        forum.add(buttonPanel, BorderLayout.SOUTH);
        forum.setVisible(true);
    }
    private static JButton editButton1(JTable table, JFrame window) {
        JButton btn = new JButton("Edit");

        btn.addActionListener(_ -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Visit(s) Selected");
                return;
            }
            if (selectedRows.length > 1){
                JOptionPane.showMessageDialog(window, "Select Only 1 Visit");
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            int patientId = Integer.parseInt(table.getModel().getValueAt(modelRow, 1).toString());

            try {commonForum(window, patientId, id, table);} catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed To Load Visit Data");
            }
        });

        return btn;
    }
    private static JButton viewVisit(JTable table, JFrame window){
        JButton btnVew = new JButton("Details");
        btnVew.addActionListener(_ -> {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(window, "No Visit(s) Selected");
                return;
            }
            if (selectedRows.length > 1){
                JOptionPane.showMessageDialog(window, "Select Only 1 Visit");
                return;
            }

            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            try {
                com.jetbrains.uzair.model.Visit v = VisitDB.returnUISingle(id);
                String patientName = PatientDB.getName(v.getPatientId());
                JDialog disp = commonUI.commonDialog("Visit Details", window);

                JPanel panel = commonUI.commonPanel();

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.anchor = GridBagConstraints.WEST;

                Font valueFont = commonUI.valueFont;

                int row = 0;
                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Visit ID:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyField(String.valueOf(v.getId()), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Patient MRR:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyField("PAT-" + String.valueOf(v.getPatientId()), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Patient Name:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyField(patientName, valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Illness:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyField(v.getIll(), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Treatement:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyArea(v.getTreatment(), valueFont), gbc);
                row++;

                gbc.gridx = 0; gbc.gridy = row;
                panel.add(new JLabel("Date Of Visit:"), gbc);

                gbc.gridx = 1;
                panel.add(commonUI.createReadOnlyField(v.getDate(), valueFont), gbc);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
                buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                buttonPanel.setBackground(new Color(12, 38, 78));

                JButton btnPrint = new JButton("Print");
                btnPrint.addActionListener(_ -> printVisit(v, patientName, window));
                JButton btnEdit = editButton2(v.getPatientId(), window, id, table);
                JButton btnClose = new JButton("Close");
                btnClose.addActionListener(_ -> disp.dispose());
                JButton btnDel = new JButton("Delete");
                btnDel.addActionListener(_ -> {
                    try {
                        SoundPlayer.play("popup.wav");
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(window, ex.getMessage());
                    }
                    int choice = JOptionPane.showConfirmDialog(
                            window,
                            "Are You Sure You Want To Delete The Selected Visit?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
                    try{
                        VisitDB.delete(id);
                        JOptionPane.showMessageDialog(window, "Visit Deleted Successfully");
                        commonUI.refresh(table, 2);
                        disp.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(disp, ex.getMessage());
                    }});
                JButton btnViewPatient = new JButton("View Patient");
                btnViewPatient.addActionListener(_ -> commonUI.patientDetails(commonUI.getDBData(window, 1), window, v.getPatientId()));

                disp.getRootPane().setDefaultButton(btnViewPatient);
                if (UserSession.isAdmin()) {
                    commonUI.commonAddBtn(buttonPanel, btnPrint, btnDel, btnEdit, btnViewPatient, btnClose);
                }else {
                    commonUI.commonAddBtn(buttonPanel, btnViewPatient, btnClose);
                }
                //header
                JPanel header = commonUI.commonHeader("Visit Data");

                //panels
                disp.setLayout(new BorderLayout());
                disp.add(header, BorderLayout.NORTH);
                disp.add(panel, BorderLayout.CENTER);
                disp.add(buttonPanel, BorderLayout.SOUTH);
                disp.setVisible(true);
            }catch (SQLException sqle){
                JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
            }
        });
        return btnVew;
    }
    //lazily loads the clinic logo from the classpath once; returns null if none is bundled
    private static BufferedImage getClinicLogo() {
        if (!logoLoadAttempted) {
            logoLoadAttempted = true;
            try (InputStream in = VisitPanel.class.getResourceAsStream(LOGO_RESOURCE)) {
                if (in != null) {
                    clinicLogo = ImageIO.read(in);
                }
            } catch (IOException ignored) {
                clinicLogo = null;
            }
        }
        return clinicLogo;
    }
    //clinic name / logo / address across the top; returns the y position to continue drawing from
    private static int drawLetterhead(Graphics2D g2d, int width) {
        int logoSize = 75;
        BufferedImage logo = getClinicLogo();
        if (logo != null) {
            g2d.drawImage(logo, 0, 0, logoSize, logoSize, null);
        } else {
            //placeholder box so the layout still looks intentional until a real logo is added
            g2d.setColor(new Color(235, 235, 235));
            g2d.fillRect(0, 0, logoSize, logoSize);
            g2d.setColor(new Color(190, 190, 190));
            g2d.drawRect(0, 0, logoSize, logoSize);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2d.setColor(new Color(150, 150, 150));
            FontMetrics fmL = g2d.getFontMetrics();
            String placeholder = "LOGO";
            g2d.drawString(placeholder, (logoSize - fmL.stringWidth(placeholder)) / 2, logoSize / 2 + 4);
        }

        int textX = logoSize + 15;
        g2d.setColor(new Color(12, 38, 78));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.drawString(CLINIC_NAME, textX, 20);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawString(CLINIC_ADDRESS, textX, 38);
        g2d.drawString(CLINIC_CONTACT, textX, 54);

        int dividerY = logoSize + 18;
        g2d.setColor(new Color(12, 38, 78));
        g2d.fillRect(0, dividerY, width, 2);

        return dividerY + 35;
    }
    //centered report title; returns the y position to continue drawing from
    private static int drawTitle(Graphics2D g2d, int width, int y) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "Visit Report";
        g2d.drawString(title, (width - fm.stringWidth(title)) / 2, y);

        y += 18;
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, y, width, 1);
        return y + 28;
    }
    //one bold-label / plain-value row, aligned to a fixed value column; returns the next y position
    private static int drawField(Graphics2D g2d, String label, String value, int y) {
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.setColor(new Color(60, 60, 60));
        g2d.drawString(label, 0, y);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.setColor(Color.BLACK);
        g2d.drawString(value == null || value.isBlank() ? "-" : value, 140, y);
        return y + 22;
    }
    //word-wraps text into lines that fit maxWidth, without drawing it
    private static List<String> wrapText(FontMetrics fm, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isBlank()) {
            lines.add("-");
            return lines;
        }
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (fm.stringWidth(candidate) > maxWidth && !line.isEmpty()) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(candidate);
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        return lines;
    }
    //draws the report content; shared by both the on-screen preview and the actual printout
    private static void drawReport(Graphics2D g2d, Visit v, String patientName, int width) throws SQLException {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int y = drawLetterhead(g2d, width);
        y = drawTitle(g2d, width, y);

        y = drawField(g2d, "Visit ID:", String.valueOf(v.getId()), y);
        y = drawField(g2d, "Patient MRR:", "PAT-" + v.getPatientId(), y);
        y = drawField(g2d, "Patient Name:", patientName, y);
        y = drawField(g2d, "CNIC / Phone:", PatientDB.returnUISingle(v.getPatientId()).getPhone(), y);
        y = drawField(g2d, "Illness:", v.getIll(), y);
        y = drawField(g2d, "Date Of Visit:", v.getDate(), y);
        y += 8;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.setColor(new Color(60, 60, 60));
        g2d.drawString("Treatment", 0, y);
        y += 10;

        //boxed treatment area, height computed from the wrapped line count
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FontMetrics tfm = g2d.getFontMetrics();
        List<String> treatmentLines = wrapText(tfm, v.getTreatment(), width - 24);
        int lineHeight = tfm.getHeight();
        int boxPadding = 10;
        int boxTop = y;
        int boxHeight = treatmentLines.size() * lineHeight + boxPadding * 2;

        g2d.setColor(new Color(250, 250, 250));
        g2d.fillRect(0, boxTop, width, boxHeight);
        g2d.setColor(new Color(210, 210, 210));
        g2d.drawRect(0, boxTop, width, boxHeight);

        g2d.setColor(Color.BLACK);
        int textY = boxTop + boxPadding + tfm.getAscent();
        for (String line : treatmentLines) {
            g2d.drawString(line, 12, textY);
            textY += lineHeight;
        }
        y = boxTop + boxHeight + 35;

        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, y, width, 1);
        y += 28;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Payment Received: Rs. " + v.getPaid(), 0, y);
        y += 45;

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2d.setColor(new Color(90, 90, 90));
        g2d.drawString("Approved By: " + UserSession.getUser().getUsername(), 0, y);

        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 9));
        g2d.setColor(new Color(150, 150, 150));
        String footerNote = "This is a system-generated visit report.";
        FontMetrics ffm = g2d.getFontMetrics();
        g2d.drawString(footerNote, (width - ffm.stringWidth(footerNote)) / 2, y + 40);
    }
    //shows a print preview window (a rendered page you can review before printing)
    private static void printVisit(Visit v, String patientName, JFrame window) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.defaultPage();

        Printable printable = (Graphics graphics, PageFormat pf, int pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            try {
                drawReport(g2d, v, patientName, (int) pf.getImageableWidth());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Printable.PAGE_EXISTS;
        };

        job.setJobName("Visit Report - " + v.getId());
        job.setPrintable(printable, pageFormat);

        JDialog preview = new JDialog(window, "Print Preview", true);
        preview.setSize(650, 780);
        preview.setLocationRelativeTo(window);
        preview.setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(new PrintPreviewPanel(printable, pageFormat));
        scroll.getViewport().setBackground(new Color(200, 200, 200));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JButton btnPrintNow = new JButton("Print...");
        JButton btnClose = new JButton("Close");
        buttonPanel.add(btnPrintNow);
        buttonPanel.add(btnClose);

        btnClose.addActionListener(_ -> preview.dispose());
        //this opens the standard Windows print dialog (printer selection, page range, copies, etc.)
        btnPrintNow.addActionListener(_ -> {
            if (job.printDialog()) {
                try {
                    job.print();
                    preview.dispose();
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(preview, "Printing Failed: " + ex.getMessage());
                }
            }
        });

        JPanel header = commonUI.commonHeader("Print Preview");

        preview.add(header, BorderLayout.NORTH);
        preview.add(scroll, BorderLayout.CENTER);
        preview.add(buttonPanel, BorderLayout.SOUTH);
        preview.setVisible(true);
    }
    //renders the page as it will be printed, scaled to fit the preview window
    private static class PrintPreviewPanel extends JPanel {
        private final Printable printable;
        private final PageFormat pageFormat;

        PrintPreviewPanel(Printable printable, PageFormat pageFormat) {
            this.printable = printable;
            this.pageFormat = pageFormat;
            setPreferredSize(new Dimension(
                    (int) pageFormat.getWidth() + 40,
                    (int) pageFormat.getHeight() + 40));
            setBackground(new Color(200, 200, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int pageWidth = (int) pageFormat.getWidth();
            int pageHeight = (int) pageFormat.getHeight();
            int x = Math.max(20, (getWidth() - pageWidth) / 2);
            int y = 20;

            //page shadow + white sheet
            g2d.setColor(new Color(150, 150, 150));
            g2d.fillRect(x + 4, y + 4, pageWidth, pageHeight);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x, y, pageWidth, pageHeight);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(x, y, pageWidth, pageHeight);

            //render the exact same content that will be printed
            g2d.translate(x, y);
            g2d.setClip(0, 0, pageWidth, pageHeight);
            try {
                printable.print(g2d, pageFormat, 0);
            } catch (PrinterException ex) {
                g2d.setColor(Color.RED);
                g2d.drawString("Preview Error: " + ex.getMessage(), 20, 40);
            }
            g2d.dispose();
        }
    }
    private static JButton editButton2(int patientId, JFrame window, int id, JTable table) {
        JButton btn = new JButton("Edit Visit");

        btn.addActionListener(_ -> {
            try {
                commonForum(window, patientId, id, table);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(window, "Failed to load patient data");
            }
        });
        return btn;
    }
}