
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

public class TcpTab extends JPanel {
    private JTextArea outputArea;

    public TcpTab() {
        setLayout(new BorderLayout());

        JButton fetchBtn = new JButton("Get TCP Table");
        fetchBtn.addActionListener(e -> fetchTcpData());

        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);

        add(fetchBtn, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
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

        // Log the raw response for debugging
        System.out.println("Response from PHP: " + sb.toString());  // Print the raw response

        // Parse the response as a JSON array
        try {
            JSONArray stats = new JSONArray(sb.toString());
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i);
                int index = stat.getInt("index");
                String state = stat.getString("state");
                String localAddr = stat.getString("localAddr");
                String localPort = stat.getString("localPort");
                String remoteAddr = stat.getString("remoteAddr");
                String remotePort = stat.getString("remotePort");

                result.append("[").append(index).append("] ")
                      .append("State: ").append(state).append(", ")
                      .append("Source IP: ").append(localAddr).append(" : ").append(localPort).append(", ")
                      .append("Destination IP: ").append(remoteAddr).append(" : ").append(remotePort).append("\n");
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
