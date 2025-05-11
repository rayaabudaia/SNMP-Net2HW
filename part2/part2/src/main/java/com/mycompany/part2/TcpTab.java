package com.mycompany.part2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class TcpTab extends JPanel {
    private JTable tcpTable;
    private DefaultTableModel tableModel;

    public TcpTab() {
        setLayout(new BorderLayout());

        JButton fetchBtn = new JButton("Get TCP Table");
        fetchBtn.addActionListener(e -> fetchTcpData());
        fetchBtn.setBackground(new Color(70, 130, 180));
        fetchBtn.setForeground(Color.WHITE);
        fetchBtn.setFocusPainted(false);
        fetchBtn.setFont(new Font("Arial", Font.BOLD, 16));

        // Define table columns
        String[] columnNames = { "Index", "State", "Source IP", "Source Port", "Destination IP", "Destination Port" };
        tableModel = new DefaultTableModel(columnNames, 0);
        tcpTable = new JTable(tableModel);
        tcpTable.setFillsViewportHeight(true);
        tcpTable.setFont(new Font("Courier New", Font.PLAIN, 14));
        tcpTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(tcpTable);

        add(fetchBtn, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void fetchTcpData() {
        try {
            URL url = new URL("http://localhost/Tcp.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            // Log the raw response (optional for debugging)
            System.out.println("Response from PHP: " + sb.toString());

            // Clear existing rows
            tableModel.setRowCount(0);

            // Parse the response as a JSON array
            JSONArray stats = new JSONArray(sb.toString());
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i);
                Object[] rowData = {
                    stat.getInt("index"),
                    stat.getString("state"),
                    stat.getString("localAddr"),
                    stat.getString("localPort"),
                    stat.getString("remoteAddr"),
                    stat.getString("remotePort")
                };
                tableModel.addRow(rowData);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Fetch Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
