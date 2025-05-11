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

public class IcmpTab extends JPanel {
    private JTextArea outputArea;

    public IcmpTab() {
        setLayout(new BorderLayout());

        JButton fetchBtn = new JButton("Get ICMP Statistics");
        fetchBtn.addActionListener(e -> fetchIcmpData());

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);

        add(fetchBtn, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
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

        // Log the raw response for debugging
        System.out.println("Response from PHP: " + sb.toString());  // Print the raw response

        // Parse the response as a JSON array
        try {
            JSONArray stats = new JSONArray(sb.toString());
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i);
                // Use .getInt() for "id" because it's likely an integer
                int id = stat.getInt("id");  
                String name = stat.getString("name");
                String value = stat.getString("value");

                result.append("[").append(id).append("] ").append(name)
                      .append(" = ").append(value).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (JSONException e) {
            outputArea.setText("Error: Invalid JSON format - " + e.getMessage());
        }

    } catch (Exception e) {
        outputArea.setText("Error: " + e.getMessage());
    }
}


}
