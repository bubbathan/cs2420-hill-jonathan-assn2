import java.util.*;

public class Tree<E extends Comparable<? super E>> {
    private BinaryTreeNode root;  // Root of tree
    private String name;     // Name of tree

    /**
     * Create an empty tree
     *
     * @param label Name of tree
     */
    public Tree(String label) {
        name = label;
    }

    /**
     * Create BST from ArrayList
     *
     * @param arr   List of elements to be added
     * @param label Name of tree
     */
    public Tree(ArrayList<E> arr, String label) {
        name = label;
        for (E key : arr) {
            insert(key);
        }
    }

    /**
     * Create BST from Array
     *
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        name = label;
        for (E key : arr) {
            insert(key);
        }
    }

    /**
     * Return a string containing the tree contents as a tree with one node per line
     */
    public String toString() {
        String treeString = this.name + ":\n";

        if (root == null) {
            treeString += "Empty Tree\n";
            return treeString;
        }

        return createString(root, 0);
    }

    private String createString(BinaryTreeNode treeNode, int depth) {
        String str = "";
        if ( treeNode == null) {return str;}

        str += createString(treeNode.right, depth + 1);

        for (int i = 0; i < depth; i++) {
            str += "  ";
        }

        if (treeNode.parent == null) {
            str += treeNode.element + " [no parent]\n";
        } else {
            str += treeNode.element + " [" + treeNode.parent.element + "]\n";
        }

        str += createString(treeNode.left, depth + 1);

        return str;
    }

    /**
     * Return a string containing the tree contents as a single line
     */
    public String inOrderToString() {
        String inOrderString = this.name + ": ";
        ArrayList<BinaryTreeNode> inOrderNodes = new ArrayList<>();
        if (root == null) {
            return inOrderString += "Empty Tree";
        }
        inOrderNodes = inOrderTraversal(root, inOrderNodes);

        for (int i = 0; i < inOrderNodes.size(); i++) {
            inOrderString += inOrderNodes.get(i).element + " ";
        }

        return inOrderString;
    }

    private ArrayList<BinaryTreeNode> inOrderTraversal(BinaryTreeNode node, ArrayList<BinaryTreeNode> nodeList) {
        if (node == null) {
            return nodeList;
        }

        inOrderTraversal(node.left, nodeList);
        nodeList.add(node);
        inOrderTraversal(node.right, nodeList);

        return nodeList;
    }

    /**
     * reverse left and right children recursively
     */
    public void flip() {
        if (root != null) {
            flipTree(root);
        }
    }

    private void flipTree(BinaryTreeNode node) {
        if (node != null) {
            BinaryTreeNode newLeftNode = node.right;
            node.right = node.left;
            node.left = newLeftNode;
            flipTree(node.right);
            flipTree(node.left);
        }
    }

    /**
     * Returns the in-order successor of the specified node
     * @param node node from which to find the in-order successor
     */
    public BinaryTreeNode inOrderSuccessor(BinaryTreeNode node) {
        if (root == null) {return null;}

        return findSuccessor(root, root.parent, node);
    }

    private BinaryTreeNode findSuccessor(BinaryTreeNode treeNode, BinaryTreeNode parent, BinaryTreeNode searchNode) {
        if (treeNode == null) {
            return backwardsTraversal(parent, searchNode);
        } else if (searchNode.element.compareTo(treeNode.element) >= 0) {
            return findSuccessor(treeNode.right, treeNode, searchNode);
        } else {
            return findSuccessor(treeNode.left, treeNode, searchNode);
        }
    }

    private BinaryTreeNode backwardsTraversal(BinaryTreeNode treeNode, BinaryTreeNode searchNode) {
        if (treeNode.element.compareTo(searchNode.element) <= 0) {
            return backwardsTraversal(treeNode.parent, searchNode);
        } else {
            return treeNode;
        }
    }

    /**
     * Counts number of nodes in specified level
     *
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        if (root == null){
            return 0;
        }

        return countNodesInLevel(level, root, 0, 0);
    }

    private int countNodesInLevel(int level, BinaryTreeNode node, int levelCount, int nodeCount) {
        if (node == null) {
            return nodeCount;
        }

        if (level == levelCount) {
            return nodeCount + 1;
        } else {
            int leftNodes = countNodesInLevel(level, node.left, levelCount + 1, nodeCount);
            int rightNodes = countNodesInLevel(level, node.right, levelCount + 1, nodeCount);
            return leftNodes + rightNodes;
        }
    }

    /**
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        if (root == null) {return;}

        pathTraversal(root, root.parent);
    }

    private void pathTraversal(BinaryTreeNode node, BinaryTreeNode parent) {
        if (node.left == null && node.right == null) {
            printPath(root, node.element);
        } else if (node.left == null) {
            pathTraversal(node.right, node);
        } else if (node.right == null) {
            pathTraversal(node.left, node);
        } else {
            pathTraversal(node.left, node);
            pathTraversal(node.right, node);
        }
    }

    private void printPath(BinaryTreeNode node, E key) {
        if (key.compareTo(node.element) == 0) {
            System.out.print(node.element + "\n");
        } else {
            System.out.print(node.element + " ");

            if (key.compareTo(node.element) < 0) {
                printPath(node.left, key);
            } else {
                printPath(node.right, key);
            }
        }
    }

    /**
     * Counts all non-null binary search trees embedded in tree
     *
     * @return Count of embedded binary search trees
     */
    public int countBST() {
        int count = 0;
        if (root == null) {
            return 0;
        }

        return countingBST(root, count);
    }

    private int countingBST(BinaryTreeNode node, int count) {
        if (node == null) {
            return 0;
        }

        int leftCount = countingBST(node.left, count);
        int rightCount = countingBST(node.right, count);
        if (identifyBST(node)) {
            leftCount += 1;
        }

        return count + rightCount + leftCount;
    }

    private boolean identifyBST(BinaryTreeNode node) {
        if (node.left == null && node.right == null) {
            return true;
        } else if (node.left == null && node.right.element.compareTo(node.element) > 0) {
            return identifyBST(node.right);
        } else if (node.right == null && node.left.element.compareTo(node.element) < 0) {
            return identifyBST(node.left);
        } else if (node.right != null && node.left != null) {
            if (node.right.element.compareTo(node.element) > 0 && node.left.element.compareTo(node.element) < 0) {
                return identifyBST(node.right) && identifyBST(node.left);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     *
     * @param x the item to insert.
     */
    public void insert(E x) {
        root = insert(x, root, null);
    }

    public BinaryTreeNode getByKey(E key) {
        if (root == null) {
            return null;
        }

        return getByKeyTraversal(key, root);
    }

    private BinaryTreeNode getByKeyTraversal(E key, BinaryTreeNode node) {
        if (key.compareTo(node.element) == 0) {
            return node;
        } else if (key.compareTo(node.element) < 0) {
            return getByKeyTraversal(key, node.left);
        } else {
            return getByKeyTraversal(key, node.right);
        }
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        ArrayList<BinaryTreeNode> inOrderNodes = new ArrayList<>();
        if (root != null) {
            inOrderNodes = inOrderTraversal(root, inOrderNodes);
            binaryTraversal (this.root, this.root.parent, inOrderNodes, 0, inOrderNodes.size());
        }
    }

    private void binaryTraversal(BinaryTreeNode node, BinaryTreeNode parent, ArrayList<BinaryTreeNode> nodeList, int min, int max) {
        if (min >= max) {
            return;
        } else {
            int value = ((max - min) / 2) + min;
            node = nodeList.get(value);
            node.parent = parent;

            binaryTraversal(node.left, node, nodeList, min, value);
            binaryTraversal(node.right, node, nodeList, value + 1, max);
        }
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryTreeNode insert(E x, BinaryTreeNode t, BinaryTreeNode parent) {
        if (t == null) return new BinaryTreeNode(x, null, null, parent);

        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = insert(x, t.left, t);
        } else {
            t.right = insert(x, t.right, t);
        }

        return t;
    }


    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     *          SIDE EFFECT: Sets local variable curr to be the node that is found
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryTreeNode t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }

    // Basic node stored in unbalanced binary trees
    public class BinaryTreeNode {
        E element;            // The data in the node
        BinaryTreeNode left;   // Left child
        BinaryTreeNode right;  // Right child
        BinaryTreeNode parent; //  Parent node

        // Constructors
        BinaryTreeNode(E theElement) {
            this(theElement, null, null, null);
        }

        BinaryTreeNode(E theElement, BinaryTreeNode lt, BinaryTreeNode rt, BinaryTreeNode pt) {
            element = theElement;
            left = lt;
            right = rt;
            parent = pt;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            if (parent == null) {
                sb.append("<>");
            } else {
                sb.append("<");
                sb.append(parent.element);
                sb.append(">");
            }

            return sb.toString();
        }
    }
}
