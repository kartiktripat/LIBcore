package client.member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberGUI {
    private MemberClient client;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private DefaultTableModel resourceTableModel;
    private JTable resourceTable;
    private JTextArea messageArea;
    private String borrowUserId;
    private String borrowDate;

    public MemberGUI(MemberClient client) {
        this.client = client;
        showLoginWindow();
    }

    private void showLoginWindow() {
        loginFrame = new JFrame("Member Login - LIBcore");
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

        mainFrame = new JFrame("LIBcore Member Dashboard - Welcome " + username);
        mainFrame.setSize(900, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JPanel topButtonPanel = new JPanel(new FlowLayout());
        JButton logoutButton = new JButton("Logout");
        JButton viewResourcesButton = new JButton("View Resources");
        JButton returnResourceButton = new JButton("Return Resource");
        JButton borrowResourceButton = new JButton("Borrow Resource");

        topButtonPanel.add(viewResourcesButton);
        topButtonPanel.add(borrowResourceButton);
        topButtonPanel.add(returnResourceButton);
        topButtonPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            client.sendMessage("LOGOUT");
            mainFrame.dispose();
            showLoginWindow();
        });

        viewResourcesButton.addActionListener(e -> {
            setupResourceTable();
            client.sendMessage("VIEW_RESOURCES");
        });

        borrowResourceButton.addActionListener(e -> showBorrowResourceForm());
        returnResourceButton.addActionListener(e -> showReturnResourceForm());

        mainFrame.add(topButtonPanel, BorderLayout.NORTH);
        mainFrame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private void setupResourceTable() {
        resourceTableModel = new DefaultTableModel(new String[]{"Resource ID", "Title", "Author", "Category", "Available"}, 0);
        resourceTable = new JTable(resourceTableModel);
        JScrollPane tableScrollPane = new JScrollPane(resourceTable);

        mainFrame.getContentPane().removeAll();
        JPanel topButtonPanel = new JPanel(new FlowLayout());
        JButton logoutButton = new JButton("Logout");
        JButton viewResourcesButton = new JButton("View Resources");
        JButton returnResourceButton = new JButton("Return Resource");
        JButton borrowResourceButton = new JButton("Borrow Resource");

        topButtonPanel.add(viewResourcesButton);
        topButtonPanel.add(borrowResourceButton);
        topButtonPanel.add(returnResourceButton);
        topButtonPanel.add(logoutButton);

        mainFrame.add(topButtonPanel, BorderLayout.NORTH);
        mainFrame.add(tableScrollPane, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();

        logoutButton.addActionListener(e -> {
            client.sendMessage("LOGOUT");
            mainFrame.dispose();
            showLoginWindow();
        });

        viewResourcesButton.addActionListener(e -> {
            setupResourceTable();
            client.sendMessage("VIEW_RESOURCES");
        });

        borrowResourceButton.addActionListener(e -> showBorrowResourceForm());
        returnResourceButton.addActionListener(e -> showReturnResourceForm());
    }

    private void showBorrowResourceForm() {
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
                        JOptionPane.showMessageDialog(mainFrame, "Resource borrowed successfully: " + resourceId);
                        showMainWindow(borrowUserId);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select a resource to borrow.");
                }
            });
            mainFrame.add(confirmButton, BorderLayout.SOUTH);
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Borrow cancelled. Please enter valid details.");
        }
    }

    private void showReturnResourceForm() {
        JTextField userIdField = new JTextField(10);
        JTextField resourceIdField = new JTextField(10);
        JTextField timeField = new JTextField(10);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIdField);
        formPanel.add(new JLabel("Resource ID:"));
        formPanel.add(resourceIdField);
        formPanel.add(new JLabel("Return Time (YYYY-MM-DD):"));
        formPanel.add(timeField);

        int result = JOptionPane.showConfirmDialog(mainFrame, formPanel, "Return Resource", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String userId = userIdField.getText();
            String resourceId = resourceIdField.getText();
            String returnTime = timeField.getText();

            if (!userId.isEmpty() && !resourceId.isEmpty() && !returnTime.isEmpty()) {
                client.sendMessage("RETURN_RESOURCE," + userId + "," + resourceId + "," + returnTime);
                JOptionPane.showMessageDialog(mainFrame, "Resource returned successfully.");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all fields.");
            }
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
        } else {
            messageArea.append(message + "\n");
        }
    }
}