/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jorgi
 */
public class Kdtree<E extends Comparable<E>> {

    protected static class KdNode<E> {

        private E element;          // an element stored at this node
        private double x;
        private double y;
        KdNode<E> left;       // a reference to the left child (if any)
        KdNode<E> right;      // a reference to the right child (if any)
        boolean vertical;

        /**
         * Constructs a node with the given element and neighbors.
         *
         * @param e the element to be stored
         * @param latitude latitude do element
         * @param longitude longitude do element
         * @param leftChild reference to a left child node
         * @param rightChild reference to a right child node
         * @param verticalT se é ou não vertical
         */
        public KdNode(E e, double latitude, double longitude, KdNode<E> leftChild, KdNode<E> rightChild, boolean verticalT) {
            element = e;
            x = latitude;
            y = longitude;
            left = leftChild;
            right = rightChild;
            vertical = verticalT;
        }

        // accessor methods
        public E getElement() {
            return element;
        }

        public double getLatitude() {
            return x;
        }

        public double getLongitude() {
            return y;

        }

        public KdNode<E> getLeft() {
            return left;
        }

        public KdNode<E> getRight() {
            return right;
        }

        public boolean getVertical() {
            return vertical;
        }

        // update methods
        public void setElement(E e) {
            element = e;
        }

        public void setLatitude(Double latitude) {
            x = latitude;
        }
        public void setLongitude(Double longitude) {
            y = longitude;
        }

        public void setLeft(KdNode<E> leftChild) {
            left = leftChild;
        }

        public void setRight(KdNode<E> rightChild) {
            right = rightChild;
        }
        
        public void setVertical(boolean verticalT){
            vertical = verticalT;
        }
    }

    //----------- end of nested Node class -----------
    protected KdNode<E> root = null;     // root of the tree

    /* Constructs an empty binary search tree. */
    public Kdtree() {
        root = null;
    }

    /*
    * @return root Node of the tree (or null if tree is empty)
     */
    protected KdNode<E> root() {
        return root;
    }

    /*
    * Verifies if the tree is empty
    * @return true if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /*
    * Inserts an element in the tree.
     */
    public void insert(E element, double latitude, double longitude) {
        root = insert(element, latitude, longitude, root, true);
    }

    private KdNode<E> insert(E element, double latitude, double longitude, KdNode<E> node, boolean vertical) {
        if (node == null) {
            return new KdNode(element,latitude,longitude, null, null, vertical);
        }
        if (node.getLatitude() == latitude && node.getLongitude() == longitude){
            return node;
        }
        if (node.getLatitude() > latitude && node.getVertical() || node.getLongitude() > longitude && !node.getVertical() ) {
            node.setLeft(insert(element,latitude,longitude,node.getLeft(),!node.vertical));
        }else{
            node.setRight(insert(element,latitude,longitude,node.getRight(),!node.vertical));
        }
        return node;
    }

    /**
     * Removes an element from the tree maintaining its consistency as a Binary
     * Search Tree.
     */
    public void remove(E element) {
        root = remove(element, root());
    }

    private KdNode<E> remove(E element, KdNode<E> node) {

        if (node == null) {
            return null;    //throw new IllegalArgumentException("Element does not exist");
        }
        if (element.compareTo(node.getElement()) == 0) {
            // node is the Node to be removed
            if (node.getLeft() == null && node.getRight() == null) { //node is a leaf (has no childs)
                return null;
            }
            if (node.getLeft() == null) {   //has only right child
                return node.getRight();
            }
            if (node.getRight() == null) {  //has only left child
                return node.getLeft();
            }
            E min = smallestElement(node.getRight());
            node.setElement(min);
            node.setRight(remove(min, node.getRight()));
        } else if (element.compareTo(node.getElement()) < 0) {
            node.setLeft(remove(element, node.getLeft()));
        } else {
            node.setRight(remove(element, node.getRight()));
        }

        return node;
    }

    /*
    * Returns the number of nodes in the tree.
    * @return number of nodes in the tree
     */
    public int size() {
        return size(root);
    }

    private int size(KdNode<E> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.getLeft()) + size(node.getRight());
    }

    /*
    * Returns the height of the tree
    * @return height 
     */
    public int height() {
        return height(root);
    }

    /*
    * Returns the height of the subtree rooted at Node node.
    * @param node A valid Node within the tree
    * @return height 
     */
    protected int height(KdNode<E> node) {
        if (node == null) {
            return -1;
        }

        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());

        if (leftHeight > rightHeight) {
            return 1 + leftHeight;
        }

        return 1 + rightHeight;
    }

    /**
     * Returns the smallest element within the tree.
     *
     * @return the smallest element within the tree
     */
    public E smallestElement() {
        return smallestElement(root);
    }

    protected E smallestElement(KdNode<E> node) {
        if (node.getLeft() == null) {
            return node.getElement();
        }
        return smallestElement(node.getLeft());
    }

    /**
     * Returns the Node containing a specific Element, or null otherwise.
     *
     * @param element the element to find
     * @return the Node that contains the Element, or null otherwise
     *
     * This method despite not being essential is very useful. It is written
     * here in order to be used by this class and its subclasses avoiding
     * recoding. So its access level is protected
     */
    public KdNode<E> find(E element) {
        return find(element, root);
    }

    protected KdNode<E> find(E element, KdNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getLeft().getElement().compareTo(element) == 0) {
            return node;
        }
        if (node.getLeft().getElement().compareTo(element) > 0) {
            return find(element, node.getLeft());
        }
        if (node.getRight().getElement().compareTo(element) < 0) {
            return find(element, node.getRight());
        }
        return node;
    }


    /*
   * Returns an iterable collection of elements of the tree, reported in in-order.
   * @return iterable collection of the tree's elements reported in in-order
     */
    public Iterable<E> inOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null) {
            inOrderSubtree(root, snapshot);   // fill the snapshot recursively
        }
        return snapshot;
    }

    /**
     * Adds elements of the subtree rooted at Node node to the given snapshot
     * using an in-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void inOrderSubtree(KdNode<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Returns an iterable collection of elements of the tree, reported in
     * pre-order.
     *
     * @return iterable collection of the tree's elements reported in pre-order
     */
    public Iterable<E> preOrder() {
        List<E> list = new ArrayList<>();
        preOrderSubtree(root, list);
        return list;
    }

    /**
     * Adds elements of the subtree rooted at Node node to the given snapshot
     * using an pre-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void preOrderSubtree(KdNode<E> node, List<E> snapshot) {
        if (node != null) {
            snapshot.add(node.getElement());
            preOrderSubtree(node.getLeft(), snapshot);
            preOrderSubtree(node.getRight(), snapshot);
        }
    }

    /**
     * Returns an iterable collection of elements of the tree, reported in
     * post-order.
     *
     * @return iterable collection of the tree's elements reported in post-order
     */
    public Iterable<E> posOrder() {
        List<E> list = new ArrayList<>();
        posOrderSubtree(root, list);
        return list;
    }

    /**
     * Adds positions of the subtree rooted at Node node to the given snapshot
     * using an post-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void posOrderSubtree(KdNode<E> node, List<E> snapshot) {
        if (node != null) {
            posOrderSubtree(node.getLeft(), snapshot);
            posOrderSubtree(node.getRight(), snapshot);
            snapshot.add(node.getElement());
        }
    }

    /*
    * Returns a map with a list of nodes by each tree level.
    * @return a map with a list of nodes by each tree level
     */
    public Map<Integer, List<E>> nodesByLevel() {
        Map<Integer, List<E>> map = new HashMap<>();
        processBstByLevel(root, map, 0);
        return map;
    }

    private void processBstByLevel(KdNode<E> node, Map<Integer, List<E>> result, int level) {
        if (node != null) {
            if (!result.containsKey(level)) {
                result.put(level, new ArrayList<E>());
            }
            List<E> list = result.get(level);
            list.add(node.getElement());
            processBstByLevel(node.getLeft(), result, level + 1);
            processBstByLevel(node.getRight(), result, level + 1);
        }
    }

//#########################################################################
    /**
     * Returns a string representation of the tree. Draw the tree horizontally
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();
    }

    private void toStringRec(KdNode<E> root, int level, StringBuilder sb) {
        if (root == null) {
            return;
        }
        toStringRec(root.getRight(), level + 1, sb);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++) {
                sb.append("|\t");
            }
            sb.append("|-------" + root.getElement() + "\n");
        } else {
            sb.append(root.getElement() + "\n");
        }
        toStringRec(root.getLeft(), level + 1, sb);
    }

}
