import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerShow extends JFrame implements ActionListener {

    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton previousButton, nextButton;
    private Connection connection;
    private ResultSet resultSet;

    public CustomerShow() {
        setTitle("Customer Information");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        displayPanel.add(idLabel, gbc);

        idField = new JTextField(20);
        idField.setFont(new Font("Arial", Font.PLAIN, 20));
        idField.setEditable(false);
        gbc.gridx = 1;
        displayPanel.add(idField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridx = 0;
        displayPanel.add(lastNameLabel, gbc);

        lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 20));
        lastNameField.setEditable(false);
        gbc.gridx = 1;
        displayPanel.add(lastNameField, gbc);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 2;
        gbc.gridx = 0;
        displayPanel.add(firstNameLabel, gbc);

        firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 20));
        firstNameField.setEditable(false);
        gbc.gridx = 1;
        displayPanel.add(firstNameField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 3;
        gbc.gridx = 0;
        displayPanel.add(phoneLabel, gbc);

        phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
        phoneField.setEditable(false);
        gbc.gridx = 1;
        displayPanel.add(phoneField, gbc);

        JPanel buttonPanel = new JPanel();
        previousButton = new JButton("Previous");
        previousButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 20));
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);

        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initializeDB();

        setVisible(true);
    }

    private void initializeDB() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/exam_db",
                    "postgres",
                    "Panhadb"
            );
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("""
                                                    SELECT * FROM customers
                                                """);

            if (resultSet.next()) {
                displayCustomer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCustomer() {
        try {
            idField.setText(resultSet.getString("id"));
            lastNameField.setText(resultSet.getString("last_name"));
            firstNameField.setText(resultSet.getString("first_name"));
            phoneField.setText(resultSet.getString("phone"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == previousButton) {
                if (resultSet.previous()) {
                    displayCustomer();
                }
            } else if (e.getSource() == nextButton) {
                if (resultSet.next()) {
                    displayCustomer();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerShow::new);
    }
}
