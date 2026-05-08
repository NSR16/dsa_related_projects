# Trie-Based Autocomplete System

A Java mini-application demonstrating the **Trie** (Prefix Tree) data structure for Autocomplete and Search Suggestions. This project was developed as a Design and Analysis of Algorithms (DAA) mini-project.

## Features
- **Real-time Suggestions**: As you type, the system instantly provides matching words.
- **Prefix Highlighting**: The matched prefix is visually highlighted in red within the suggestions.
- **Dynamic Insertion**: New words can be dynamically added to the Trie from the UI and are immediately available for autocomplete.
- **Match Counting**: Displays the total number of valid matching words found in the Trie before trimming to the top 5 limit.
- **Clean Separation of Concerns**: Core algorithmic logic (`Trie.java`) is completely separated from the Graphical User Interface (`AutocompleteUI.java`).

## Why use a Trie instead of Brute-Force?

### Brute-Force Approach
In a naive brute-force approach, we would store all words in a standard array or list. To find all words starting with a prefix of length $m$ in a dictionary of $N$ words, we would need to check every single word. 
**Time Complexity:** $O(N \times m)$. As the dictionary grows large, scanning the entire list for every keystroke becomes extremely inefficient.

### Trie Approach
A Trie organizes words into a tree of characters. To find words starting with a prefix of length $m$, we only traverse $m$ nodes down the tree. Once we reach the end of the prefix, all words branching down from that point are guaranteed to be valid suggestions. We do not need to scan the rest of the dictionary.
**Time Complexity:** $O(m)$ to find the prefix node, plus the time to traverse the sub-tree. This is completely independent of $N$ (the total number of words in the dictionary), making it remarkably fast.

## Time Complexities

Let $m$ be the length of the string/prefix, and $k$ be the number of nodes in the sub-tree starting from the prefix node.

1. **Insert Operation:** `O(m)`
   - We traverse the length of the word, creating new nodes if the character path doesn't already exist.

2. **Search Prefix Node:** `O(m)`
   - We follow the character path down the tree to locate the node where the prefix ends.

3. **Get Suggestions:** `O(m + k)`
   - Takes `O(m)` to find the prefix node.
   - Takes `O(k)` using **Depth-First Search (DFS)** to traverse the sub-tree and collect all complete words that branch off from the prefix node.

## Space Complexity
- **O(N × L)** in the worst case (where $N$ is the number of words and $L$ is the average word length). However, because the Trie shares common prefixes (e.g., "tree" and "trie" share "tr"), it is very space-efficient for dictionaries with many overlapping words.

## How to Run

1. Open your terminal or command prompt.
2. Navigate to the project directory.
3. Compile the Java files:
   ```bash
   javac Trie.java AutocompleteUI.java
   ```
4. Run the main UI class:
   ```bash
   java AutocompleteUI
   ```
