import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    public Login() {
        setTitle("User Login");
        setSize(420, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        URL imgUrl = getClass().getResource("/PLOGO.png");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        topPanel.setBackground(Color.WHITE);

        if (imgUrl != null) {
            Image scaled = new ImageIcon(imgUrl).getImage().getScaledInstance(380, 60, Image.SCALE_SMOOTH);
            topPanel.add(new JLabel(new ImageIcon(scaled)));
        } else {
            JLabel logoLabel = new JLabel("POORNIMA LOGO", SwingConstants.CENTER);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            topPanel.add(logoLabel);
        }
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JLabel title = new JLabel("USER LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension fieldSize = new Dimension(260, 38);

        JTextField email = new JTextField();
        email.setMaximumSize(fieldSize);
        email.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField pass = new JPasswordField();
        pass.setMaximumSize(fieldSize);
        pass.setBorder(BorderFactory.createTitledBorder("Password"));

        JLabel forgot = new JLabel("<html><u>Forgot Password?</u></html>");
        forgot.setForeground(Color.BLUE);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btn = new JButton("LOGIN");
        btn.setPreferredSize(new Dimension(90, 30));

        // Database authentication trigger
        btn.addActionListener(e -> {
            String userEmail = email.getText().trim();
            String userPass = new String(pass.getPassword()).trim();

            if (userEmail.isEmpty() || userPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Email and Password.");
                return;
            }

            String sql = "SELECT * FROM student WHERE email = ? AND password = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, userEmail);
                pst.setString(2, userPass);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    new Dashboard().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Email or Password!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Connection Error: " + ex.getMessage());
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setMaximumSize(new Dimension(260, 40));
        btnPanel.add(btn);

        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        forgotPanel.setMaximumSize(new Dimension(260, 30));
        forgotPanel.add(forgot);

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(email);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(pass);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(forgotPanel);
        centerPanel.add(btnPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}