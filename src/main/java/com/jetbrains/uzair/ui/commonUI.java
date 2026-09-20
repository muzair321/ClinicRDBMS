package com.jetbrains.uzair.ui;

import com.jetbrains.uzair.app.UserSession;
import com.jetbrains.uzair.db.*;
import com.jetbrains.uzair.model.Patient;
import com.jetbrains.uzair.model.Visit;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class commonUI {
    public static Font valueFont = new Font("Segoe UI", Font.PLAIN, 13);
    //table design
    public static JTable tableDesign(DefaultTableModel model){
        JTable table = new JTable(model) {


            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column) {

                Component c = super.prepareRenderer(renderer, row, column);

                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(218, 217, 217)); // light gray
                    }
                } else {
                    c.setBackground(getSelectionBackground());
                }

                return c;
            }
        };
        JTableHeader header1 = table.getTableHeader();

        header1.setDefaultRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                lbl.setBackground(new Color(22, 51, 83)); // dark blue
                lbl.setForeground(Color.WHITE);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
                lbl.setOpaque(true);

                return lbl;
            }
        });
        header1.setReorderingAllowed(false);
        header1.setResizingAllowed(true);
        table.setRowHeight(22);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(220, 220, 220));
        return table;
    }
    //get data from database
    public static JTable getDBData(JFrame window, int ui){
        var headers = getStrings(ui);
        String[] header = headers.toArray(new String[0]);
        String[][] data;
        try {
            if(ui == 1) {
                data = PatientDB.returnUI();
            } else if (ui == 2) {
                data = VisitDB.returnUI();
            } else if (ui == 3) {
                data = InventoryDB.returnUI();
            } else if (ui == 4) {
                data = InventoryLogsDB.returnUI();
            } else if (ui == 5) {
                data = UsersDB.returnUI();
            } else if (ui == 6) {
                data = UserLogsDB.returnUI();
            } else if(ui == 7){
                data = InventoryDB.returnAlerts();
            }else{
                throw new SQLException("Error Reading");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
            window.dispose();
            throw new RuntimeException("Error While Reading Data");
        }
        DefaultTableModel model = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevent accidental edits
            }
        };
        return tableDesign(model);
    }
    //get header for table creation
    private static ArrayList<String> getStrings(int ui) {
        var headers = new ArrayList<String>();
        if(ui == 1) {
            headers.add("MRR");
            headers.add("Name");
            headers.add("Age");
            headers.add("Gender");
            headers.add("CNIC / Phone");
            headers.add("Created At");
        }
        else if(ui == 2){
            headers.add("Visit ID");
            headers.add("Patient MRR");
            headers.add("Patient Name");
            headers.add("Illness");
            headers.add("Date Of Visit");
        }else if(ui == 3){
            headers.add("Item ID");
            headers.add("Name");
            headers.add("Storage Type");
            headers.add("Stock");
            headers.add("Last Updated");
        }else if (ui == 4){
            headers.add("Log ID");
            headers.add("Item Name");
            headers.add("User Name");
            headers.add("Amount");
            headers.add("Datetime");
        } else if (ui == 5) {
            headers.add("User ID");
            headers.add("Username");
            headers.add("Admin Access");
        } else if (ui == 6) {
            headers.add("Log ID");
            headers.add("User ID");
            headers.add("Username");
            headers.add("Datetime");
        }else if(ui == 7){
            headers.add("Item ID");
            headers.add("Item Name");
            headers.add("Storage");
            headers.add("Stock");
            headers.add("Alert At");
            headers.add("Last Updated");
        } else {
            headers.add("Error");
        }
        return headers;
    }
    //search for visit or patient
    public static void search(JTable table, String search, int ui) {
        SwingWorker<String[][], Void> worker = new SwingWorker<>() {
            @Override
            protected String[][] doInBackground() throws SQLException {
                if(ui == 1) {
                    return PatientDB.returnUI(search);
                } else if (ui == 2) {
                    return VisitDB.returnUI(search);
                } else if(ui == 3){
                    return InventoryDB.returnUI(search);
                } else if (ui == 4) {
                    return InventoryLogsDB.returnUI(search);
                } else if (ui == 6) {
                    return UserLogsDB.returnUI(search);
                } else {
                    return null;
                }
            }
            @Override
            protected void done() {
                try {
                    String[][] data = get();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0);
                    for (String[] row : data) {
                        model.addRow(row);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(table, e.getMessage() + "Failed To Search");
                }
            }
        };
        worker.execute();
    }
    //refresh the current table
    public static void refresh(JTable table, int ui) {
        if(table != null) {
            SwingWorker<String[][], Void> worker = new SwingWorker<>() {
                @Override
                protected String[][] doInBackground() throws SQLException {
                    if (ui == 1) {
                        return PatientDB.returnUI();
                    } else if (ui == 2) {
                        return VisitDB.returnUI();
                    } else if(ui == 3){
                        return InventoryDB.returnUI();
                    } else if (ui == 4) {
                        return InventoryLogsDB.returnUI();
                    } else if (ui == 5) {
                        return UsersDB.returnUI();
                    } else if (ui == 6) {
                        return UserLogsDB.returnUI();
                    }
                    else if (ui == 7) {
                        return InventoryDB.returnAlerts();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        String[][] data = get();
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.setRowCount(0);
                        for (String[] row : data) {
                            model.addRow(row);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(table, "Failed To Refresh Data");
                    }
                }
            };
            worker.execute();
        }
    }



    //make a read only text field
    public static JTextField createReadOnlyField(String text, Font font) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        field.setBackground(UIManager.getColor("Panel.background"));
        field.setFont(font);
        field.setColumns(40);
        return field;
    }
    //view details of patient
    public static void patientDetails(JTable table, JFrame window, int id){
        try {
            Patient p = PatientDB.returnUISingle(id);
            JDialog disp = commonDialog("Patient Details", window);
            JPanel panel = commonPanel();
            disp.setSize(770, 810);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;
            int row = 0;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("MRR:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField("PAT-" + String.valueOf(p.getId()), valueFont), gbc);
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getName(), valueFont), gbc);
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Age:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(String.valueOf(p.getAge()), valueFont), gbc);
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Gender:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getGender(), valueFont), gbc);
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("CNIC / Phone:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getPhone(), valueFont), gbc);
            row++;
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Date Of Creation:"), gbc);
            gbc.gridx = 1;
            panel.add(commonUI.createReadOnlyField(p.getCreatedAt(), valueFont), gbc);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setBackground(new Color(12, 38, 78));
            JButton btnEdit = PatientPanel.editButton2(table, window, id);
            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(_ -> disp.dispose());
            JButton btnDel = new JButton("Delete");
            btnDel.addActionListener(_ -> {
                try {SoundPlayer.play("popup.wav");} catch (RuntimeException ex) {JOptionPane.showMessageDialog(window, ex.getMessage());}
                int choice = JOptionPane.showConfirmDialog(
                        window,
                        "Are You Sure You Want To Delete The Selected Patient(s)?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
                try{
                    PatientDB.delete(id);
                    JOptionPane.showMessageDialog(window, "Patient Deleted Successfully");
                    commonUI.refresh(table, 1);
                    disp.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(disp, ex.getMessage());
                }});
            JButton btnAddVisit = new JButton("Add Visit");
            btnAddVisit.addActionListener(_ -> {
                try {
                    VisitPanel.commonForum(window, id, -1, null);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
                }
            });
            disp.getRootPane().setDefaultButton(btnAddVisit);
            if (UserSession.isAdmin()) commonUI.commonAddBtn(buttonPanel,btnClose, btnDel, btnEdit,  btnAddVisit);
            else commonUI.commonAddBtn(buttonPanel,btnClose, btnAddVisit);
            //header
            JPanel header = commonHeader("Patient Data");
            //panels
            JPanel centerPanel = new JPanel();
            JScrollPane bottom = getVisitTables(id);
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(panel, BorderLayout.NORTH);
            centerPanel.add(bottom, BorderLayout.CENTER);
            disp.setLayout(new BorderLayout());
            disp.add(header, BorderLayout.NORTH);
            disp.add(centerPanel, BorderLayout.CENTER);
            disp.add(buttonPanel, BorderLayout.SOUTH);
            disp.setVisible(true);
        }catch (SQLException sqle){
            JOptionPane.showMessageDialog(window,  "Error Occured: " + sqle.getMessage());
        }
    }
    //delete selected
    public static JButton deleteButton(JTable table, JFrame window, int ui){
        JButton btnDel = new JButton("Delete");
        btnDel.addActionListener(_ -> {
            int[] selectedRows = table.getSelectedRows();
            String t = null;
            if(ui == 1){t = "Patient";
            } else if (ui == 2) {t = "Visit";
            }else if (ui == 3){t = "Inventory Item";
            } else if (ui == 5) {t = "User";}
            if (selectedRows.length == 0) {JOptionPane.showMessageDialog(window, "No " + t + "(s) Selected");return;}
            List<Integer> patientIds = new ArrayList<>();
            for (int viewRow : selectedRows) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                int id = Integer.parseInt((table.getModel().getValueAt(modelRow, 0).toString()).substring(4));
                patientIds.add(id);
            }
            try {SoundPlayer.play("popup.wav");} catch (RuntimeException ex) {JOptionPane.showMessageDialog(window, ex.getMessage());}
            int choice = JOptionPane.showConfirmDialog(
                    window,
                    "Are You Sure You Want To Delete The Selected " + t + "(s)?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION)return;
            try {
                if(ui == 1) {PatientDB.deleteList(patientIds);
                } else if (ui == 2) {VisitDB.deleteList(patientIds);
                }else if(ui == 3){InventoryDB.deleteList(patientIds);
                } else if (ui ==  5) {UsersDB.delete(patientIds);}
                JOptionPane.showMessageDialog(window, "Deleted Successfully");
                refresh(table, ui);
            } catch (SQLException ex) {JOptionPane.showMessageDialog(window, "Delete Failed");}
        });
        return btnDel;
    }
    //highlighting buttons
    public static void buttonHighlight(JButton btnEdit, JButton btnView, JButton btnDel, JTable table) {
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnDel.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(_ -> {
            boolean selected = table.getSelectedRowCount() > 0;
            btnView.setEnabled(table.getSelectedRowCount() == 1);
            btnEdit.setEnabled(table.getSelectedRowCount() == 1);
            btnDel.setEnabled(selected);
        });
    }
    public static void buttonHighlight(JButton btnEdit, JButton btnUpdate, JButton btnView, JButton btnDel, JTable table) {
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnDel.setEnabled(false);
        btnUpdate.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(_ -> {
            boolean selected = table.getSelectedRowCount() > 0;
            btnView.setEnabled(table.getSelectedRowCount() == 1);
            btnUpdate.setEnabled(table.getSelectedRowCount() == 1);
            btnEdit.setEnabled(table.getSelectedRowCount() == 1);
            btnDel.setEnabled(selected);
        });
    }
    public static void buttonHighlight(JButton btnDel, JTable table) {
        btnDel.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(_ -> {
            boolean selected = table.getSelectedRowCount() > 0;
            btnDel.setEnabled(selected);
        });
    }
    public static void buttonHighlight(JButton btnUpdate, JButton btnView, JTable table) {
        btnView.setEnabled(false);
        btnUpdate.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(_ -> {
            boolean selected = table.getSelectedRowCount() == 1;
            btnUpdate.setEnabled(selected);
            btnView.setEnabled(selected);
        });
    }
    //misc methods
    public static JDialog commonDialog(String title, JFrame window){
        JDialog disp = new JDialog(window,title, true);
        disp.setSize(700, 810);
        disp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        disp.setLocationRelativeTo(null); // center on screen
        return disp;
    }
    public static JPanel commonPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        return panel;
    }
    public static void commonAddBtn(JPanel buttonPanel, JButton... buttons){
        for(JButton btn: buttons){
            buttonPanel.add(btn);
        }
    }
    public static JPanel commonHeader(String title){
        JPanel header = new JPanel();
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setBackground(new Color(12, 38, 78));
        JLabel head = new JLabel(title);
        head.setForeground(Color.WHITE);
        head.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.add(head);
        return header;
    }
    public static JPanel commonForum(){
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        return formPanel;
    }
    public static GridBagConstraints commonFormGrid(JPanel formPanel, int row){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
//name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        return gbc;
    }
    //getting tables for view dialog
    private static JScrollPane getVisitTables(int patientId) throws SQLException {
        String[] headers = { "Visit ID", "Illness", "Treatment", "Date" };
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<Visit> visits = VisitDB.returnVisits(patientId);
        for (Visit v : visits) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getIll(),
                    v.getTreatment(),
                    v.getDate()
            });
        }
        JTable table = tableDesign(model);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        });
        return new JScrollPane(table);
    }
    //for uneditable textfields
    public static JComponent createReadOnlyArea(String text, Font font) {
        JTextArea area = new JTextArea(text);
        area.setFont(font);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false); // blends with panel
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scroll.setPreferredSize(new Dimension(480, 300));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }
}
//for expandable textarea
class TextAreaRenderer extends JTextArea implements TableCellRenderer {
    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setText(value == null ? "" : value.toString());
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        // Auto-adjust row height
        setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
        int preferredHeight = getPreferredSize().height;
        if (table.getRowHeight(row) != preferredHeight) {
            table.setRowHeight(row, preferredHeight);
        }
        return this;
    }
}
