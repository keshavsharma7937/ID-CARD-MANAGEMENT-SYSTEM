import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditDetails extends JFrame {

    private String studentRegNo;
    private Runnable onUpdateCallback;

    private JTextField nameTxt, fatherTxt, regNoTxt, mailTxt, addressTxt, dobTxt, bloodTxt;
    private JRadioButton h1, h2, t1, t2;

    public EditDetails(String regNo, Runnable onUpdateCallback) {
        this.studentRegNo = regNo;
        this.onUpdateCallback = onUpdateCallback;

        setTitle("Edit Details");
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

        form.add(new JLabel(" Update Photo:"));
        JButton photoBtn = new JButton("Choose Photo");
        photoBtn.setPreferredSize(new Dimension(140, 24));
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnWrap.add(photoBtn);
        form.add(btnWrap);

        nameTxt = new JTextField();
        fatherTxt = new JTextField();
        regNoTxt = new JTextField(regNo);
        regNoTxt.setEditable(false); // Primary key cannot be changed
        mailTxt = new JTextField();
        addressTxt = new JTextField();
        dobTxt = new JTextField();
        bloodTxt = new JTextField();

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

        h1 = new JRadioButton("Hostler");
        h2 = new JRadioButton("Non Hostler");
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(h1);
        bg1.add(h2);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p1.add(h1);
        p1.add(h2);
        form.add(new JLabel(" Residential Status:"));
        form.add(p1);

        t1 = new JRadioButton("SELF");
        t2 = new JRadioButton("College Bus");
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(t1);
        bg2.add(t2);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p2.add(t1);
        p2.add(t2);
        form.add(new JLabel(" Transport Service:"));
        form.add(p2);

        JButton updateBtn = new JButton("Update");
        updateBtn.setPreferredSize(new Dimension(85, 26));
        JPanel updateWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        updateWrap.add(updateBtn);

        form.add(new JLabel());
        form.add(updateWrap);
        add(form, BorderLayout.CENTER);
        loadStudentDetails();
        updateBtn.addActionListener(e -> updateStudentDetails());
    }

    private void loadStudentDetails() {
        String sql = "SELECT * FROM student WHERE reg_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, studentRegNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameTxt.setText(rs.getString("name"));
                fatherTxt.setText(rs.getString("father_name"));
                mailTxt.setText(rs.getString("email"));
                addressTxt.setText(rs.getString("address"));
                dobTxt.setText(rs.getString("dob"));
                bloodTxt.setText(rs.getString("blood_group"));

                String resStatus = rs.getString("residential_status");
                if ("Hostler".equalsIgnoreCase(resStatus)) h1.setSelected(true);
                else if ("Non Hostler".equalsIgnoreCase(resStatus)) h2.setSelected(true);

                String transport = rs.getString("transport_service");
                if ("SELF".equalsIgnoreCase(transport)) t1.setSelected(true);
                else if ("College Bus".equalsIgnoreCase(transport)) t2.setSelected(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading details: " + ex.getMessage());
        }
    }

    private void updateStudentDetails() {
        String name = nameTxt.getText().trim();
        String father = fatherTxt.getText().trim();
        String mail = mailTxt.getText().trim();
        String address = addressTxt.getText().trim();
        String dob = dobTxt.getText().trim();
        String blood = bloodTxt.getText().trim();
        String resStatus = h1.isSelected() ? "Hostler" : (h2.isSelected() ? "Non Hostler" : "");
        String transport = t1.isSelected() ? "SELF" : (t2.isSelected() ? "College Bus" : "");

        if (name.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Mail ID cannot be empty!");
            return;
        }

        String sql = "UPDATE student SET name = ?, father_name = ?, email = ?, address = ?, dob = ?, blood_group = ?, residential_status = ?, transport_service = ? WHERE reg_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, father);
            pst.setString(3, mail);
            pst.setString(4, address);
            pst.setString(5, dob);
            pst.setString(6, blood);
            pst.setString(7, resStatus);
            pst.setString(8, transport);
            pst.setString(9, studentRegNo);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Details Updated Successfully!");

            if (onUpdateCallback != null) {
                onUpdateCallback.run();
            }
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating data: " + ex.getMessage());
        }
    }
}