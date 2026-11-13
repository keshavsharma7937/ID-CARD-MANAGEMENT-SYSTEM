import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompleteDetails extends JFrame {

    private String studentRegNo;
    private JTextField nameTxt, fatherTxt, regNoTxt, mailTxt, addressTxt, dobTxt, bloodTxt, resStatusTxt, transportTxt;

    public CompleteDetails(String regNo) {
        this.studentRegNo = regNo;

        setTitle("Complete Student Details");
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

        JPanel form = new JPanel(new GridLayout(10, 2, 8, 6));
        form.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        nameTxt = createReadOnlyField();
        fatherTxt = createReadOnlyField();
        regNoTxt = createReadOnlyField();
        mailTxt = createReadOnlyField();
        addressTxt = createReadOnlyField();
        dobTxt = createReadOnlyField();
        bloodTxt = createReadOnlyField();
        resStatusTxt = createReadOnlyField();
        transportTxt = createReadOnlyField();

        form.add(new JLabel(" Registration No:"));
        form.add(regNoTxt);

        form.add(new JLabel(" Name:"));
        form.add(nameTxt);

        form.add(new JLabel(" Father Name:"));
        form.add(fatherTxt);

        form.add(new JLabel(" Mail ID:"));
        form.add(mailTxt);

        form.add(new JLabel(" Address:"));
        form.add(addressTxt);

        form.add(new JLabel(" DOB:"));
        form.add(dobTxt);

        form.add(new JLabel(" Blood Group:"));
        form.add(bloodTxt);

        form.add(new JLabel(" Residential Status:"));
        form.add(resStatusTxt);

        form.add(new JLabel(" Transport Service:"));
        form.add(transportTxt);

        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(85, 26));
        closeBtn.addActionListener(e -> dispose());

        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnWrap.add(closeBtn);

        form.add(new JLabel());
        form.add(btnWrap);

        add(form, BorderLayout.CENTER);

        loadCompleteDetails();
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        return field;
    }

    private void loadCompleteDetails() {
        String sql = "SELECT * FROM student WHERE reg_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, studentRegNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                regNoTxt.setText(rs.getString("reg_no"));
                nameTxt.setText(rs.getString("name"));
                fatherTxt.setText(rs.getString("father_name"));
                mailTxt.setText(rs.getString("email"));
                addressTxt.setText(rs.getString("address"));
                dobTxt.setText(rs.getString("dob"));
                bloodTxt.setText(rs.getString("blood_group"));
                resStatusTxt.setText(rs.getString("residential_status"));
                transportTxt.setText(rs.getString("transport_service"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading complete details: " + ex.getMessage());
        }
    }
}