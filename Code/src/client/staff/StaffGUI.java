package client.staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StaffGUI {
    private StaffClient client;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private JTextArea messageArea;
    private JPanel actionPanel;
    private DefaultTableModel resourceTableModel;
    private JTable resourceTable;
    private String borrowUserId;
    private String borrowDate;

    public StaffGUI(StaffClient client) {
        this.client = client;
        showLoginWindow();
    }

    private void showLoginWindow() {
        loginFrame = new JFrame("Staff Login - LIBcore");
        loginFrame.setSize(350, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(4, 2));

        JTextField userIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginFrame.add(new JLabel("User ID:"));
        loginFrame.add(userIdField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);
        loginFrame.add(new JLabel(""));
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            String userID = userIdField.getText();
            String password = new String(passwordField.getPassword());
            if (!userID.isEmpty() && !password.isEmpty()) {
                client.sendMessage("LOGIN," + userID + "," + password);
            }
        });

        loginFrame.setVisible(true);
    }

    public void showMainWindow(String username) {
        loginFrame.dispose();

        mainFrame = new JFrame("LIBcore Staff Dashboard - Welcome " + username);
        mainFrame.setSize(1000, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        JPanel topButtonPanel = new JPanel(new FlowLayout());
        JButton logoutButton = new JButton("Logout");
        JButton viewResourcesButton = new JButton("View Resources");
        JButton addResourceButton = new JButton("Add Resource");
        JButton exportTransactionsButton = new JButton("Export Transactions");
        JButton viewUsersButton = new JButton("View Users");
        JButton viewTransactionHistoryButton = new JButton("View Transaction History");
        JButton returnResourceButton = new JButton("Return Resource");
        JButton borrowResourceButton = new JButton("Borrow Resource");

        topButtonPanel.add(viewResourcesButton);
        topButtonPanel.add(addResourceButton);
        topButtonPanel.add(viewUsersButton);
        topButtonPanel.add(viewTransactionHistoryButton);
        topButtonPanel.add(exportTransactionsButton);
        topButtonPanel.add(borrowResourceButton);
        topButtonPanel.add(returnResourceButton);
        topButtonPanel.add(logoutButton);

        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        logoutButton.addActionListener(e -> {
            client.sendMessage("LOGOUT");
            mainFrame.dispose();
            showLoginWindow();
        });

        viewResourcesButton.addActionListener(e -> {
            clearActionPanel();
            setupResourceTable();
            client.sendMessage("VIEW_RESOURCES");
        });

        addResourceButton.addActionListener(e -> showAddResourceForm());

        exportTransactionsButton.addActionListener(e -> {
            clearActionPanel();
            messageArea.setText("");
            client.sendMessage("EXPORT_TRANSACTIONS");
        });

        viewUsersButton.addActionListener(e -> {
            clearActionPanel();
            messageArea.setText("");
            client.sendMessage("VIEW_USERS");
        });

        viewTransactionHistoryButton.addActionListener(e -> {
            clearActionPanel();
            messageArea.setText("");
            client.sendMessage("VIEW_TRANSACTIONS");
        });

        returnResourceButton.addActionListener(e -> showReturnResourceForm());
        borrowResourceButton.addActionListener(e -> showBorrowResourceForm());

        mainFrame.add(topButtonPanel, BorderLayout.NORTH);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(actionPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private void clearActionPanel() {
        actionPanel.removeAll();
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void setupResourceTable() {
        resourceTableModel = new DefaultTableModel(new String[]{"Resource ID", "Title", "Author", "Category", "Available"}, 0);
        resourceTable = new JTable(resourceTableModel);
        JScrollPane tableScrollPane = new JScrollPane(resourceTable);

        actionPanel.removeAll();
        actionPanel.add(tableScrollPane);
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showAddResourceForm() {
        clearActionPanel();

        JTextField idField = new JTextField(10);
        JTextField titleField = new JTextField(10);
        JTextField authorField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JButton submitButton = new JButton("Add Resource");

        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Resource ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String id = idField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String category = categoryField.getText();

            if (!id.isEmpty() && !title.isEmpty() && !author.isEmpty() && !category.isEmpty()) {
                client.sendMessage("ADD_RESOURCE," + id + "," + title + "," + author + "," + category);
                messageArea.setText("Resource added: " + title);
                clearActionPanel();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.");
            }
        });

        actionPanel.add(formPanel);
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showReturnResourceForm() {
        clearActionPanel();

        JTextField userIdField = new JTextField(10);
        JTextField resourceIdField = new JTextField(10);
        JTextField timeField = new JTextField(10);
        JButton submitButton = new JButton("Return Resource");

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIdField);
        formPanel.add(new JLabel("Resource ID:"));
        formPanel.add(resourceIdField);
        formPanel.add(new JLabel("Return Time (YYYY-MM-DD):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String resourceId = resourceIdField.getText();
            String returnTime = timeField.getText();

            if (!userId.isEmpty() && !resourceId.isEmpty() && !returnTime.isEmpty()) {
                client.sendMessage("RETURN_RESOURCE," + userId + "," + resourceId + "," + returnTime);
                messageArea.setText("Resource returned successfully.");
                clearActionPanel();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.");
            }
        });

        actionPanel.add(formPanel);
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void showBorrowResourceForm() {
        clearActionPanel();

        borrowUserId = JOptionPane.showInputDialog("Enter User ID:");
        borrowDate = JOptionPane.showInputDialog("Enter Borrow Date (YYYY-MM-DD):");

        if (borrowUserId != null && borrowDate != null && !borrowUserId.isEmpty() && !borrowDate.isEmpty()) {
            setupResourceTable();
            client.sendMessage("VIEW_RESOURCES");

            JButton confirmButton = new JButton("Confirm Borrow");
            confirmButton.addActionListener(e -> {
                int selectedRow = resourceTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String resourceId = (String) resourceTableModel.getValueAt(selectedRow, 0);
                    int result = JOptionPane.showConfirmDialog(mainFrame, "Confirm borrowing Resource ID: " + resourceId + "?", "Confirm Borrow", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        client.sendMessage("BORROW_RESOURCE," + borrowUserId + "," + resourceId + "," + borrowDate);
                        messageArea.setText("Resource borrowed successfully: " + resourceId);
                        clearActionPanel();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select a resource to borrow.");
                }
            });

            actionPanel.add(confirmButton);
            actionPanel.revalidate();
            actionPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Borrow cancelled. Please enter valid details.");
        }
    }


    public void displayServerMessage(String message) {
        if (message.startsWith("LOGIN_SUCCESS")) {
            String[] parts = message.split(",");
            String username = parts[1];
            showMainWindow(username);
        } else if (resourceTableModel != null && message.contains(",")) {
            String[] parts = message.split(",");
            if (parts.length >= 5) {
                resourceTableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3], parts[4]});
            }
        } else if (messageArea != null) { // FIX: Only append if messageArea is ready
            messageArea.append(message + "\\n");
        }
    }

}
