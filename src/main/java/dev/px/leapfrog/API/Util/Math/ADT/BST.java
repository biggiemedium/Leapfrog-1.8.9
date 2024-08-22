package dev.px.leapfrog.API.Util.Math.ADT;

public class BST<T extends Comparable<T>> {

    private BSTNode<T> root;

    public BST() {
        this.root = null;
    }

    /**
     * Inserts a value into the BST.
     *
     * @param value the value to be inserted
     */
    public void insert(T value) {
        root = insertRecursive(root, value);
    }


    /**
     * Searches for a value in the BST.
     *
     * @param value the value to be searched for
     * @return {@code true} if the value is found, {@code false} otherwise
     */
    public boolean search(T value) {
        return searchRecursive(root, value);
    }

    /**
     * Recursive helper method to search for a value in the BST.
     *
     * @param node  the current node being examined
     * @param value the value to be searched for
     * @return {@code true} if the value is found, {@code false} otherwise
     */
    private boolean searchRecursive(BSTNode<T> node, T value) {
        if (node == null) {
            return false;
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            return searchRecursive(node.left, value);
        } else if (cmp > 0) {
            return searchRecursive(node.right, value);
        } else {
            return true;
        }
    }
    /**
     * Recursive helper method to insert a value into the BST.
     *
     * @param node  the current node being examined
     * @param value the value to be inserted
     * @return the updated node
     */
    private BSTNode<T> insertRecursive(BSTNode<T> node, T value) {
        if (node == null) {
            return new BSTNode<>(value);
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, value);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, value);
        }
        return node;
    }

    /**
     * Inner class representing a node in the BST.
     *
     * @param <T> the type of value held in the node
     */
    private class BSTNode<T> {
        private T value;
        private BSTNode<T> left;
        private BSTNode<T> right;

        /**
         * Constructs a BST node with the given value.
         *
         * @param value the value to be stored in the node
         */
        public BSTNode(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
}
