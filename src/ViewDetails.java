import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewDetails extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewDetails() {
        setTitle("View Details - ID Card Management System");
        setSize(700, 500);
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

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] columnNames = {"Registration No", "Student Name", "Mail ID", "Residential Status"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadDataFromDatabase();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        JButton viewCompleteBtn = new JButton("View Complete Details");
        viewCompleteBtn.setPreferredSize(new Dimension(180, 30));

        JButton editBtn = new JButton("Edit Details");
        editBtn.setPreferredSize(new Dimension(120, 30));

        btnPanel.add(viewCompleteBtn);
        btnPanel.add(editBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        viewCompleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String regNo = table.getValueAt(selectedRow, 0).toString();
                new CompleteDetails(regNo).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student record from the table first.");
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String regNo = table.getValueAt(selectedRow, 0).toString();
                new EditDetails(regNo, this::loadDataFromDatabase).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student record to edit.");
            }
        });
    }

    private void loadDataFromDatabase() {
        model.setRowCount(0);
        String sql = "SELECT reg_no, name, email, residential_status FROM student";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("reg_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("residential_status")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching records: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewDetails().setVisible(true);
    }
}