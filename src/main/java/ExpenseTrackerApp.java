import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ExpenseTrackerApp extends JFrame {

    // Input fields and button for the expense form
    private JTextField dateField, categoryField, descriptionField, amountField;
    private JButton addButton;
    private JButton removeButton;
    
    // Table to display expenses
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    
    // Labels to display the remaining budget and total spent
    private JLabel budgetLabel;
    private JLabel spentLabel;
    
    // Variables to track budget and expenses
    private double initialBudget;
    private double spentAmount;
    private double remainingBudget;

    public ExpenseTrackerApp() {
        // Set up the main window
        setTitle("Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen

        // --- Prompt for Budget with a Custom Dialog ---
        JPanel budgetPanel = new JPanel();
        budgetPanel.add(new JLabel("Enter your total budget (in FCFA): "));
        JTextField budgetField = new JTextField(10);
        budgetPanel.add(budgetField);
        
        int result = JOptionPane.showConfirmDialog(this, budgetPanel, "Initial Budget", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                initialBudget = Double.parseDouble(budgetField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid budget input. Setting budget to 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                initialBudget = 0;
            }
        } else {
            // If user cancels, exit the application.
            System.exit(0);
        }
        spentAmount = 0;
        remainingBudget = initialBudget;
        
        // --- Input Panel Setup ---
        // Create a panel with a grid layout (2 rows x 5 columns) for labels and input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        
        removeButton = new JButton("Remove Expense");
        // First row: Labels
        inputPanel.add(new JLabel("Date (yyyy-mm-dd):"));
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JLabel("Amount (FCFA):"));
        inputPanel.add(removeButton);
        //inputPanel.add(new JLabel("")); // Empty label for alignment
        
        // Second row: Input fields and the button
        dateField = new JTextField();
        categoryField = new JTextField();
        descriptionField = new JTextField();
        amountField = new JTextField();
        addButton = new JButton("Add Expense");
        
        inputPanel.add(dateField);
        inputPanel.add(categoryField);
        inputPanel.add(descriptionField);
        inputPanel.add(amountField);
        inputPanel.add(addButton);
        
        // --- Table Setup ---
        // Define column names for the expense table
        String[] columnNames = {"Date", "Category", "Description", "Amount (FCFA)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        
        // --- Bottom Panel for Budget and Spent Amount ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        budgetLabel = new JLabel("Remaining Budget: FCFA " + remainingBudget);
        spentLabel = new JLabel("Total Spent: FCFA " + spentAmount);
        bottomPanel.add(budgetLabel);
        bottomPanel.add(spentLabel);
        
        // --- Layout the Main Frame ---
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // --- Event Handling ---
        // Add an ActionListener to handle button clicks for adding an expense
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
    }
    
    /**
     * This method retrieves input data, validates it,
     * adds a new row to the table if valid, updates the budget information, 
     * and clears the input fields.
     */
    private void addExpense() {
        String date = dateField.getText().trim();
        String category = categoryField.getText().trim();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        
        // Validate that none of the fields are empty
        if (date.isEmpty() || category.isEmpty() || description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate that the amount is a valid number
        double expenseAmount;
        try {
            expenseAmount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Add the expense details as a new row in the table
        Object[] rowData = { date, category, description, expenseAmount };
        tableModel.addRow(rowData);
        
        // Update the total spent and remaining budget
        spentAmount += expenseAmount;
        remainingBudget = initialBudget - spentAmount;
        budgetLabel.setText("Remaining Budget: FCFA " + remainingBudget);
        spentLabel.setText("Total Spent: FCFA " + spentAmount);
        
        // Notify the user if the budget is exceeded
        if (remainingBudget < 0) {
            JOptionPane.showMessageDialog(this, "Warning: You are out of budget!", "Budget Alert", JOptionPane.WARNING_MESSAGE);
        }
        
        // Clear the input fields for the next entry
        dateField.setText("");
        categoryField.setText("");
        descriptionField.setText("");
        amountField.setText("");
    }
    
    private void removeExpense(){
    
    }
    
    
    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExpenseTrackerApp tracker = new ExpenseTrackerApp();
                tracker.setVisible(true);
            }
        });
    }
}
