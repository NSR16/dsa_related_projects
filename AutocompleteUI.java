import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Graphical User Interface for the Autocomplete Trie Application.
 * Built using Java Swing to provide a clean, minimal interface.
 */
public class AutocompleteUI extends JFrame {

    private Trie trie;
    private JTextField searchField;
    private DefaultListModel<String> listModel;
    private JLabel statusLabel;
    private JList<String> suggestionList;

    public AutocompleteUI() {
        // Initialize the Trie data structure
        trie = new Trie();
        initializeData();

        // Setup the main window properties
        setTitle("Trie Autocomplete");
        setSize(550, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Main container with some padding
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Create the header panel (Title and Description)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Autocomplete with Trie");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 120, 215)); // A nice modern blue
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>A demonstration of the Trie data structure for fast prefix-based search and suggestions.</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        headerPanel.add(titleLabel);
        headerPanel.add(descLabel);

        // Create the search panel (Search input and Add button)
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        // FlatLaf specific properties (will be ignored gracefully if FlatLaf isn't active)
        searchField.putClientProperty("JTextField.placeholderText", "Start typing to search...");
        searchField.putClientProperty("JComponent.roundRect", true);

        JButton addButton = new JButton("Add Word");
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.putClientProperty("JButton.buttonType", "roundRect");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(addButton, BorderLayout.EAST);

        // Combine header and search panel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(headerPanel);
        topContainer.add(searchPanel);

        // Create the center panel (Suggestions list)
        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);
        suggestionList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Add row height and padding for a cleaner list
        suggestionList.setFixedCellHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(suggestionList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Let FlatLaf handle scroll borders
        scrollPane.putClientProperty("JScrollPane.smoothScrolling", true);
        
        // Add a titled border or a clean label above
        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        JLabel suggestionsTitle = new JLabel("Suggestions");
        suggestionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        suggestionsTitle.setForeground(Color.GRAY);
        centerPanel.add(suggestionsTitle, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the bottom panel (Status label)
        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.add(statusLabel);

        // Assemble main container
        mainContainer.add(topContainer, BorderLayout.NORTH);
        mainContainer.add(centerPanel, BorderLayout.CENTER);
        mainContainer.add(bottomPanel, BorderLayout.SOUTH);

        // Add to frame
        setContentPane(mainContainer);

        // Add event listeners
        setupListeners(addButton);
    }

    /**
     * Pre-populates the Trie with some initial dataset.
     */
    private void initializeData() {
        String[] words = {
            "algorithm", "array", "binary", "binary search", "binary tree",
            "complexity", "computer", "data", "database", "data structure",
            "dynamic programming", "graph", "greedy", "hash", "hash table",
            "heap", "java", "javascript", "linked list", "machine learning",
            "node", "object oriented", "programming", "queue", "recursion",
            "software", "sorting", "stack", "time complexity", "trie", "tree"
        };
        
        for (String word : words) {
            trie.insert(word);
        }
    }

    /**
     * Sets up the event listeners for the input field and add button.
     */
    private void setupListeners(JButton addButton) {
        // DocumentListener triggers every time the text in the search box changes
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
        });

        // Add Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newWord = searchField.getText().trim();
                if (!newWord.isEmpty()) {
                    trie.insert(newWord);
                    JOptionPane.showMessageDialog(AutocompleteUI.this, 
                        "Successfully added '" + newWord + "' to the Trie!", 
                        "Word Added", 
                        JOptionPane.INFORMATION_MESSAGE);
                    updateSuggestions(); // Refresh the list
                }
            }
        });
        suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        String selected = suggestionList.getSelectedValue();

        if (selected != null) {

            // Remove HTML tags used for highlighting
            selected = selected.replaceAll("<[^>]*>", "");

            // Autofill textbox
            searchField.setText(selected);

            // Clear suggestions after selection
            listModel.clear();

            statusLabel.setText("Selected: " + selected);
        }
    }
});
    }

    /**
     * Fetches suggestions from the Trie based on the current input
     * and updates the UI accordingly.
     */
    private void updateSuggestions() {
        String prefix = searchField.getText().trim().toLowerCase();
        listModel.clear();

        if (prefix.isEmpty()) {
            statusLabel.setText("Type a word to see suggestions.");
            return;
        }

        // Fetch all matching words from the Trie
        List<String> matches = trie.getSuggestions(prefix);
        int totalMatches = matches.size();

        if (totalMatches == 0) {
            statusLabel.setText("No matches found for prefix: '" + prefix + "'");
            listModel.addElement("<html><i>No suggestions</i></html>");
            return;
        }

        // Limit to top 5 suggestions
        int limit = Math.min(5, totalMatches);
        
        for (int i = 0; i < limit; i++) {
            String word = matches.get(i);
            // Highlight the matched prefix using HTML (using a nice modern blue color)
            String highlightedWord = "<html><b><font color='#0078D7'>" + 
                                     word.substring(0, prefix.length()) + 
                                     "</font></b>" + 
                                     word.substring(prefix.length()) + 
                                     "</html>";
            listModel.addElement(highlightedWord);
        }

        // Update the status label
        statusLabel.setText("Found " + totalMatches + " matching word(s). Showing top " + limit + ".");
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Attempt to load FlatLaf Dark theme
                try {
                    UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
                } catch (Exception ex) {
                    // Fallback to FlatLaf Light or System L&F
                    try {
                        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                    } catch (Exception ex2) {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception ex3) {
                            // Default Metal
                        }
                    }
                }
                new AutocompleteUI().setVisible(true);
            }
        });
    }
}
