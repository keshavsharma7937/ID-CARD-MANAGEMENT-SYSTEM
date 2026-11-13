import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePassword extends JFrame {

    private JTextField emailTxt;
    private JPasswordField oldPassTxt, newPassTxt, confirmPassTxt;

    public ChangePassword() {
        setTitle("Change Password");
        setSize(420, 460);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));

        JLabel title = new JLabel("CHANGE PASSWORD");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension fieldSize = new Dimension(260, 42);

        emailTxt = new JTextField();
        emailTxt.setMaximumSize(fieldSize);
        emailTxt.setBorder(BorderFactory.createTitledBorder("Email ID"));

        oldPassTxt = new JPasswordField();
        oldPassTxt.setMaximumSize(fieldSize);
        oldPassTxt.setBorder(BorderFactory.createTitledBorder("Current Password"));

        newPassTxt = new JPasswordField();
        newPassTxt.setMaximumSize(fieldSize);
        newPassTxt.setBorder(BorderFactory.createTitledBorder("New Password"));

        confirmPassTxt = new JPasswordField();
        confirmPassTxt.setMaximumSize(fieldSize);
        confirmPassTxt.setBorder(BorderFactory.createTitledBorder("Confirm New Password"));

        JButton updateBtn = new JButton("UPDATE");
        updateBtn.setPreferredSize(new Dimension(100, 30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setMaximumSize(new Dimension(260, 40));
        btnPanel.add(updateBtn);

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(emailTxt);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(oldPassTxt);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(newPassTxt);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(confirmPassTxt);
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(btnPanel);

        add(centerPanel, BorderLayout.CENTER);

        updateBtn.addActionListener(e -> handleChangePassword());
    }

    private void handleChangePassword() {
        String email = emailTxt.getText().trim();
        String oldPass = new String(oldPassTxt.getPassword()).trim();
        String newPass = new String(newPassTxt.getPassword()).trim();
        String confirmPass = new String(confirmPassTxt.getPassword()).trim();

        if (email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New Password and Confirm Password do not match!");
            return;
        }

        String verifySql = "SELECT * FROM student WHERE email = ? AND password = ?";
        String updateSql = "UPDATE student SET password = ? WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement verifyPst = con.prepareStatement(verifySql)) {

            verifyPst.setString(1, email);
            verifyPst.setString(2, oldPass);
            ResultSet rs = verifyPst.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updatePst = con.prepareStatement(updateSql)) {
                    updatePst.setString(1, newPass);
                    updatePst.setString(2, email);
                    updatePst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Password Updated Successfully!");
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email ID or Current Password!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePassword().setVisible(true));
    }
}