package com.mycompany.part2;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Part2 {

    public static void main(String[] args) {
       JFrame frame = new JFrame("SNMP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("System", new SystemTab());
        tabs.addTab("TCP Table", new TcpTab());      
        tabs.addTab("ICMP Stats", new IcmpTab()); 

        frame.add(tabs);
        frame.setSize(700, 500);
        frame.setVisible(true);
    }
}
