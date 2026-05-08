import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trie Data Structure Implementation for Autocomplete
 * This class handles the core algorithmic logic of inserting words and
 * finding suggestions using Depth-First Search (DFS).
 */
public class Trie {

    /**
     * TrieNode represents a single character in the Trie.
     */
    private class TrieNode {
        // Using a HashMap to store children allows for a flexible character set
        // (not just a-z, but also capital letters, numbers, etc.)
        Map<Character, TrieNode> children;
        boolean isEndOfWord;

        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     * Time Complexity: O(m), where m is the length of the word.
     * Space Complexity: O(m) in the worst case (if no common prefix exists).
     *
     * @param word The word to insert.
     */
    public void insert(String word) {
        if (word == null || word.isEmpty()) return;
        
        TrieNode current = root;
        // Convert to lowercase to make the autocomplete case-insensitive
        word = word.toLowerCase();

        for (char ch : word.toCharArray()) {
            // If the character doesn't exist in the current node's children, add it
            current.children.putIfAbsent(ch, new TrieNode());
            // Move to the child node
            current = current.children.get(ch);
        }
        // Mark the end of the inserted word
        current.isEndOfWord = true;
    }

    /**
     * Finds the node representing the end of the given prefix.
     * Time Complexity: O(m), where m is the length of the prefix.
     *
     * @param prefix The prefix to search for.
     * @return The TrieNode at the end of the prefix, or null if the prefix is not in the Trie.
     */
    private TrieNode searchPrefixNode(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return null; // Prefix not found
            }
            current = node;
        }
        return current;
    }

    /**
     * Gets all words in the Trie that start with the given prefix.
     * Time Complexity: O(m + k), where m is the prefix length and k is the number of nodes in the subtree.
     *
     * @param prefix The prefix to search for.
     * @return A list of all matching words.
     */
    public List<String> getSuggestions(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix == null || prefix.isEmpty()) {
            return results;
        }

        prefix = prefix.toLowerCase();
        
        // 1. Find the node where the prefix ends
        TrieNode prefixNode = searchPrefixNode(prefix);

        // If the prefix doesn't exist, return empty list
        if (prefixNode == null) {
            return results;
        }

        // 2. Perform DFS from the prefix node to find all complete words
        dfs(prefixNode, prefix, results);

        return results;
    }

    /**
     * Depth-First Search traversal to collect all words from a given node.
     *
     * @param node The current TrieNode.
     * @param currentWord The word formed from the root to the current node.
     * @param results The list to store the collected words.
     */
    private void dfs(TrieNode node, String currentWord, List<String> results) {
        // If this node marks the end of a word, add it to our results
        if (node.isEndOfWord) {
            results.add(currentWord);
        }

        // Recursively visit all children
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), currentWord + entry.getKey(), results);
        }
    }
}
