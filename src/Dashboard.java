import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("ID Card Management System");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = getClass().getResource("/PLOGO.png");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        topPanel.setBackground(Color.WHITE);

        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage().getScaledInstance(650, 80, Image.SCALE_SMOOTH);
            topPanel.add(new JLabel(new ImageIcon(img)));
        } else {
            JLabel titleHeader = new JLabel("POORNIMA INSTITUTE OF ENGINEERING & TECHNOLOGY");
            titleHeader.setFont(new Font("Arial", Font.BOLD, 18));
            topPanel.add(titleHeader);
        }
        add(topPanel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        Dimension btnSize = new Dimension(150, 35);

        JButton addBtn = new JButton("Add details");
        addBtn.setMaximumSize(btnSize);

        JButton viewBtn = new JButton("View details");
        viewBtn.setMaximumSize(btnSize);

        JButton passBtn = new JButton("Change Password");
        passBtn.setMaximumSize(btnSize);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setMaximumSize(btnSize);

        addBtn.addActionListener(e -> new AddDetails().setVisible(true));
        viewBtn.addActionListener(e -> new ViewDetails().setVisible(true));
        passBtn.addActionListener(e -> new ChangePassword().setVisible(true));
        logoutBtn.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        sidebar.add(addBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(viewBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(passBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("About ID Card Management System");
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        center.add(heading, BorderLayout.NORTH);

        JTextArea aboutTextArea = new JTextArea();
        aboutTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutTextArea.setLineWrap(true);
        aboutTextArea.setWrapStyleWord(true);
        aboutTextArea.setText("The ID Card Management System is a centralized digital platform designed to streamline student data collection and institutional record-keeping. It eliminates traditional manual paperwork by allowing users to easily register, update, and manage comprehensive student profiles, including photos, contact information, blood group, residential status, and transport preferences. Built with secure, role-based access control, the system ensures data safety and quick navigation for both administrators and students, enabling hassle-free identity management across the institution.");

        JScrollPane scrollPane = new JScrollPane(aboutTextArea);
        center.add(scrollPane, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}