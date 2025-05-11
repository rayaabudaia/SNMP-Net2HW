package com.mycompany.part2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class SystemTab extends JPanel {
    private JTable systemTable;
    private DefaultTableModel tableModel;
    private JTextField sysContactField, sysNameField, sysLocationField;

    public SystemTab() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background

        JButton fetchBtn = new JButton("Get System Data");
        fetchBtn.setBackground(new Color(70, 130, 180)); // Steel blue
        fetchBtn.setForeground(Color.WHITE);
        fetchBtn.setFocusPainted(false);
        fetchBtn.setFont(new Font("Arial", Font.BOLD, 16));
        fetchBtn.addActionListener(e -> fetchSystemData());

        // Define table columns
        String[] columnNames = { "Property", "Value" };
        tableModel = new DefaultTableModel(columnNames, 0);
        systemTable = new JTable(tableModel);
        systemTable.setFillsViewportHeight(true);
        systemTable.setFont(new Font("Courier New", Font.PLAIN, 14));
        systemTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(systemTable);

        JPanel editPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        editPanel.setBackground(new Color(200, 191, 231)); // Lighter cyan
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit System Info"));

        sysContactField = new JTextField(20);
        sysNameField = new JTextField(20);
        sysLocationField = new JTextField(20);

        JButton setContact = createStyledButton("Update Contact");
        JButton setName = createStyledButton("Update Name");
        JButton setLocation = createStyledButton("Update Location");

        setContact.addActionListener(e -> setSystemValue("sysContact", sysContactField.getText()));
        setName.addActionListener(e -> setSystemValue("sysName", sysNameField.getText()));
        setLocation.addActionListener(e -> setSystemValue("sysLocation", sysLocationField.getText()));

        editPanel.add(new JLabel("sysContact:", JLabel.RIGHT));
        editPanel.add(sysContactField);
        editPanel.add(setContact);

        editPanel.add(new JLabel("sysName:", JLabel.RIGHT));
        editPanel.add(sysNameField);
        editPanel.add(setName);

        editPanel.add(new JLabel("sysLocation:", JLabel.RIGHT));
        editPanel.add(sysLocationField);
        editPanel.add(setLocation);

        add(fetchBtn, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        return button;
    }

    private void fetchSystemData() {
        try {
            URL url = new URL("http://localhost/sys.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            JSONObject json = new JSONObject(response);

            // Clear previous data in table
            tableModel.setRowCount(0);

            // Populate table with new data
            tableModel.addRow(new Object[]{"sysContact", json.optString("sysContact", "")});
            tableModel.addRow(new Object[]{"sysName", json.optString("sysName", "")});
            tableModel.addRow(new Object[]{"sysLocation", json.optString("sysLocation", "")});
            tableModel.addRow(new Object[]{"sysDescr", json.optString("sysDescr", "")});
            tableModel.addRow(new Object[]{"sysUpTime", json.optString("sysUpTime", "")});
            tableModel.addRow(new Object[]{"sysObjectID", json.optString("sysObjectID", "")});

            // Optionally, update the fields as well
            sysContactField.setText(json.optString("sysContact", ""));
            sysNameField.setText(json.optString("sysName", ""));
            sysLocationField.setText(json.optString("sysLocation", ""));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Fetch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setSystemValue(String key, String value) {
        try {
            URL url = new URL("http://localhost/sys.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String oidMap = "";
            switch (key) {
                case "sysContact": oidMap = "1.3.6.1.2.1.1.4.0"; break;
                case "sysName": oidMap = "1.3.6.1.2.1.1.5.0"; break;
                case "sysLocation": oidMap = "1.3.6.1.2.1.1.6.0"; break;
            }

            String data = "oid=" + URLEncoder.encode(oidMap, "UTF-8") +
                         "&value=" + URLEncoder.encode(value, "UTF-8");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            JOptionPane.showMessageDialog(this, "Update Response: " + response, "Update Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
