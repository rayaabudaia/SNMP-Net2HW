package com.mycompany.part2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class IcmpTab extends JPanel {
    private JTable icmpTable;
    private DefaultTableModel tableModel;

    public IcmpTab() {
        setLayout(new BorderLayout());

        JButton fetchBtn = new JButton("Get ICMP Statistics");
        fetchBtn.addActionListener(e -> fetchIcmpData());
        fetchBtn.setBackground(new Color(70, 130, 180));
        fetchBtn.setForeground(Color.WHITE);
        fetchBtn.setFocusPainted(false);
        fetchBtn.setFont(new Font("Arial", Font.BOLD, 16));

        // Define table columns
        String[] columnNames = { "ID", "Name", "Value" };
        tableModel = new DefaultTableModel(columnNames, 0);
        icmpTable = new JTable(tableModel);
        icmpTable.setFillsViewportHeight(true);
        icmpTable.setFont(new Font("Courier New", Font.PLAIN, 14));
        icmpTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(icmpTable);

        add(fetchBtn, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void fetchIcmpData() {
        try {
            URL url = new URL("http://localhost/Icmp.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            System.out.println("Response from PHP: " + sb.toString());

            // Clear previous data
            tableModel.setRowCount(0);

            JSONArray stats = new JSONArray(sb.toString());
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i);
                Object[] rowData = {
                    stat.getInt("id"),
                    stat.getString("name"),
                    stat.getString("value")
                };
                tableModel.addRow(rowData);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Fetch Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
