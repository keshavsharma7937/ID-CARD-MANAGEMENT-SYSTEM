import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddDetails extends JFrame {

    public AddDetails() {
        setTitle("Add Details");
        setSize(480, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = getClass().getResource("/PLOGO.png");
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage().getScaledInstance(420, 60, Image.SCALE_SMOOTH);
            topPanel.add(new JLabel(new ImageIcon(img)));
        } else {
            topPanel.add(new JLabel("POORNIMA LOGO"));
        }
        add(topPanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(11, 2, 8, 4));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        form.add(new JLabel(" Add Photo:"));
        JButton photoBtn = new JButton("Choose Photo");
        photoBtn.setPreferredSize(new Dimension(140, 24));
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnWrap.add(photoBtn);
        form.add(btnWrap);

        JTextField nameTxt = new JTextField();
        JTextField fatherTxt = new JTextField();
        JTextField regNoTxt = new JTextField();
        JTextField mailTxt = new JTextField();
        JTextField addressTxt = new JTextField();
        JTextField dobTxt = new JTextField();
        JTextField bloodTxt = new JTextField();

        form.add(new JLabel(" Name:"));
        form.add(nameTxt);

        form.add(new JLabel(" Father Name:"));
        form.add(fatherTxt);

        form.add(new JLabel(" Registration No:"));
        form.add(regNoTxt);

        form.add(new JLabel(" Mail ID:"));
        form.add(mailTxt);

        form.add(new JLabel(" Address:"));
        form.add(addressTxt);

        form.add(new JLabel(" DOB:"));
        form.add(dobTxt);

        form.add(new JLabel(" Blood Group:"));
        form.add(bloodTxt);

        JRadioButton h1 = new JRadioButton("Hostler"), h2 = new JRadioButton("Non Hostler");
        ButtonGroup bg1 = new ButtonGroup(); bg1.add(h1); bg1.add(h2);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p1.add(h1); p1.add(h2);
        form.add(new JLabel(" Residential Status:"));
        form.add(p1);

        JRadioButton t1 = new JRadioButton("SELF"), t2 = new JRadioButton("College Bus");
        ButtonGroup bg2 = new ButtonGroup(); bg2.add(t1); bg2.add(t2);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p2.add(t1); p2.add(t2);
        form.add(new JLabel(" Transport Service:"));
        form.add(p2);

        JButton addBtn = new JButton("Add");
        addBtn.setPreferredSize(new Dimension(75, 26));
        JPanel addWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addWrap.add(addBtn);

        form.add(new JLabel());
        form.add(addWrap);

        // Database insertion handler
        addBtn.addActionListener(e -> {
            String name = nameTxt.getText().trim();
            String father = fatherTxt.getText().trim();
            String regNo = regNoTxt.getText().trim();
            String mail = mailTxt.getText().trim();
            String address = addressTxt.getText().trim();
            String dob = dobTxt.getText().trim();
            String blood = bloodTxt.getText().trim();
            String resStatus = h1.isSelected() ? "Hostler" : (h2.isSelected() ? "Non Hostler" : "");
            String transport = t1.isSelected() ? "SELF" : (t2.isSelected() ? "College Bus" : "");

            if (regNo.isEmpty() || name.isEmpty() || mail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Registration No, Name, and Mail ID are required!");
                return;
            }

            String sql = "INSERT INTO student (reg_no, name, father_name, email, password, address, dob, blood_group, residential_status, transport_service) VALUES (?, ?, ?, ?, '123456789', ?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, regNo);
                pst.setString(2, name);
                pst.setString(3, father);
                pst.setString(4, mail);
                pst.setString(5, address);
                pst.setString(6, dob);
                pst.setString(7, blood);
                pst.setString(8, resStatus);
                pst.setString(9, transport);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Details Added Successfully!");
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting data: " + ex.getMessage());
            }
        });

        add(form, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new AddDetails().setVisible(true);
    }
}