/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.part2;

/**
 *
 * @author user
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.*;
public class SystemTab extends JPanel {
    private JTextArea outputArea;
    private JTextField sysContactField, sysNameField, sysLocationField;

    public SystemTab() {
        setLayout(new BorderLayout());

        JButton fetchBtn = new JButton("Get System Data");
        fetchBtn.addActionListener(e -> fetchSystemData());

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);

        // Editable Fields
        JPanel editPanel = new JPanel(new GridLayout(3, 3));
        sysContactField = new JTextField(20);
        sysNameField = new JTextField(20);
        sysLocationField = new JTextField(20);

        JButton setContact = new JButton("Set");
        JButton setName = new JButton("Set");
        JButton setLocation = new JButton("Set");

        setContact.addActionListener(e -> setSystemValue("sysContact", sysContactField.getText()));
        setName.addActionListener(e -> setSystemValue("sysName", sysNameField.getText()));
        setLocation.addActionListener(e -> setSystemValue("sysLocation", sysLocationField.getText()));

        editPanel.add(new JLabel("sysContact:"));
        editPanel.add(sysContactField);
        editPanel.add(setContact);

        editPanel.add(new JLabel("sysName:"));
        editPanel.add(sysNameField);
        editPanel.add(setName);

        editPanel.add(new JLabel("sysLocation:"));
        editPanel.add(sysLocationField);
        editPanel.add(setLocation);

        add(fetchBtn, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
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
            outputArea.setText(json.toString(4));

            // Optional: Fill editable fields
            sysContactField.setText(json.getString("sysContact"));
            sysNameField.setText(json.getString("sysName"));
            sysLocationField.setText(json.getString("sysLocation"));

        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void setSystemValue(String key, String value) {
        try {
            URL url = new URL("http://localhost/sys.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String data = key + "=" + URLEncoder.encode(value, "UTF-8");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            outputArea.setText("Set result: " + response);
        } catch (Exception e) {
            outputArea.setText("Set error: " + e.getMessage());
        }
    }
}