/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dei.Sprint2.Trees;

import org.dei.Sprint2.Country;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei._Location.GeographicalLocation;
import org.dei._Facilities.Station.Station;

import java.util.ArrayList;
import java.util.List;

public class AVL extends BST<NodeData> {

    private int balanceFactor(BST.Node<NodeData> node){
        return (node.getRight() != null ? node.getRight().getElement().getHeight() : -1)
                - (node.getLeft()  != null ? node.getLeft().getElement().getHeight()  : -1);
    }

    private BST.Node<NodeData> rightRotation(BST.Node<NodeData> node){
        BST.Node<NodeData> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());
        leftChild.setRight(node);

        int nodeHeight = 1 + Math.max(
                node.getLeft() != null ? node.getLeft().getElement().getHeight() : -1,
                node.getRight() != null ? node.getRight().getElement().getHeight() : -1
        );
        node.getElement().setHeight(nodeHeight);

        int leftHeight = 1 + Math.max(
                leftChild.getLeft() != null ? leftChild.getLeft().getElement().getHeight() : -1,
                leftChild.getRight() != null ? leftChild.getRight().getElement().getHeight() : -1
        );
        leftChild.getElement().setHeight(leftHeight);

        return leftChild;
    }


    private BST.Node<NodeData> leftRotation(BST.Node<NodeData> node){
        BST.Node<NodeData> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());
        rightChild.setLeft(node);

        int nodeHeight = 1 + Math.max(
                node.getLeft() != null ? node.getLeft().getElement().getHeight() : -1,
                node.getRight() != null ? node.getRight().getElement().getHeight() : -1
        );
        node.getElement().setHeight(nodeHeight);

        int rightHeight = 1 + Math.max(
                rightChild.getLeft() != null ? rightChild.getLeft().getElement().getHeight() : -1,
                rightChild.getRight() != null ? rightChild.getRight().getElement().getHeight() : -1
        );
        rightChild.getElement().setHeight(rightHeight);

        return rightChild;
    }

    
    private Node twoRotations(Node node){
        if(balanceFactor(node)>0){
            node.setRight( rightRotation(node.getRight()) );
            node = leftRotation(node);
        }
        else{
            node.setLeft( leftRotation(node.getLeft()) );
            node = rightRotation(node);
        }
        return node;
    }
    
    private Node balanceNode(Node node){
        int balance = balanceFactor(node);

        if (balance > 1){
            if (balanceFactor(node.getRight()) < 0){
                node = twoRotations(node);
            } else {
                node = leftRotation(node);
            }
        } else if (balance < -1){
            if (balanceFactor(node.getLeft()) > 0){
                node = twoRotations(node);
            } else {
                node = rightRotation(node);
            }
        }
        return node;
    }
    
    @Override
    public void insert(NodeData element){
        root = insert(element, root);
    }
    private Node<NodeData> insert(NodeData element, BST.Node<NodeData> node){
        if(node == null){
            return new Node<>(element, null, null);
        }
        else if(element.compareTo(node.getElement()) < 0){
            node.setLeft( insert(element, node.getLeft()) );
        }
        else{
            node.setRight( insert(element, node.getRight()) );
        }

        int height = 1 + Math.max(
                node.getLeft() != null ? node.getLeft().getElement().getHeight() : -1,
                node.getRight() != null ? node.getRight().getElement().getHeight() : -1
        );
        node.getElement().setHeight(height);

        node = balanceNode(node);
        return node;
    }

    
    @Override  
    public void remove(NodeData element){
        root = remove(element, root());
    }



    private Node<NodeData> remove(NodeData element, BST.Node<NodeData> node){
        if (node == null){
            return null;
        }
        if (element.compareTo(node.getElement()) == 0){
            if (node.getLeft() == null && node.getRight() == null){
                return null;
            }
            if (node.getLeft() == null){
                return node.getRight();
            }
            if (node.getRight() == null){
                return node.getLeft();
            }

            NodeData smallElem = smallestElement(node.getRight());
            node.setElement(smallElem);
            node.setRight( remove(smallElem, node.getRight()) );
            node = balanceNode(node);
        }
        else if (element.compareTo(node.getElement()) < 0){
            node.setLeft( remove(element, node.getLeft()) );
            node = balanceNode(node);
        }
        else {
            node.setRight( remove(element, node.getRight()) );
            node = balanceNode(node);
        }
        if (node != null){
            int height = 1 + Math.max(
                    node.getLeft() != null ? node.getLeft().getElement().getHeight() : -1,
                    node.getRight() != null ? node.getRight().getElement().getHeight() : -1
            );
            node.getElement().setHeight(height);
        }

        return node;
    }

    public NodeData findNodeByCoordinates(GeographicalLocation coord){
        return findNodeByCoordinatesHelper(coord, root);
    }

    private NodeData findNodeByCoordinatesHelper(GeographicalLocation coord, BST.Node<NodeData> node) {
        if (node == null) return null;
        NodeData data = node.getElement();

        int cmpLat = Double.compare(coord.getLatitude(), data.getCoordinate().getLatitude());
        if (cmpLat != 0) {
            return cmpLat < 0
                    ? findNodeByCoordinatesHelper(coord, node.getLeft())
                    : findNodeByCoordinatesHelper(coord, node.getRight());
        }
        int cmpLon = Double.compare(coord.getLongitude(), data.getCoordinate().getLongitude());
        if (cmpLon != 0) {
            return cmpLon < 0
                    ? findNodeByCoordinatesHelper(coord, node.getLeft())
                    : findNodeByCoordinatesHelper(coord, node.getRight());
        }
        return node.getElement();
    }

    /**
     * Get an array of nodes with all the nodes from the avl tree
     * sorted from smallest to highest according to the latitude
     * @return List of node
     */
    public List<Node> inOrderTransversal(){
        List<Node> nodes = new ArrayList<>();

        inOrderTransversalHelper(nodes, root);
        return (nodes);
    }

    private void inOrderTransversalHelper(List<Node> nodes, Node node){
        if (node == null)
            return;

        inOrderTransversalHelper(nodes, node.getLeft());
        nodes.add(node);
        inOrderTransversalHelper(nodes, node.getRight());
    }

    /**
     * Get an array of nodes with all the nodes from the avl tree
     * sorted from smallest to highest according to the latitude
     * @return List of node
     */
    public List<NodeData> inOrderTransversalData(){
        List<NodeData> nodes = new ArrayList<>();

        inOrderTransversalHelperData(nodes, root);
        return (nodes);
    }

    private void inOrderTransversalHelperData(List<NodeData> nodes, Node node){
        if (node == null)
            return;

        inOrderTransversalHelperData(nodes, node.getLeft());
        nodes.add((NodeData) node.getElement());
        inOrderTransversalHelperData(nodes, node.getRight());
    }


    public boolean equals(Object otherObj){

        if (this == otherObj) 
            return true;

        if (otherObj == null || this.getClass() != otherObj.getClass())
            return false;

        AVL second = (AVL) otherObj;
        return equals(root, second.root);
    }

    public boolean equals(Node<NodeData> root1, Node<NodeData> root2){
        if (root1 == null && root2 == null)
            return true;
        else if (root1 != null && root2 != null) {
            if (root1.getElement().compareTo(root2.getElement()) == 0) {
                return equals(root1.getLeft(), root2.getLeft())
                        && equals(root1.getRight(), root2.getRight());
            } else
                return false;
        }
        else return false;
    }

    public String toInOrderString(){
        StringBuilder sb = new StringBuilder();
        toInOrderStringHelper(root, sb);
        return sb.toString();
    }

    private void toInOrderStringHelper(Node node, StringBuilder sb){
        if (node == null) return;
        toInOrderStringHelper(node.getLeft(), sb);
        sb.append(node.getElement().toString()).append("\n");
        toInOrderStringHelper(node.getRight(), sb);
    }


    //======Sample Queries==========

    public List<Station> findByTimeZoneGroupAndCountry(TimeZoneGroup tzGroup, Country country){
        List<Station> result = new ArrayList<>();
        findByTimeZoneGroupAndCountryHelper(root, tzGroup, country, result);
        return result;
    }
    private void findByTimeZoneGroupAndCountryHelper(Node node, TimeZoneGroup tzGroup, Country country, List<Station> result){
        if (node == null) return;
        NodeData nodeData = (NodeData) node.getElement();
        for (Station s : nodeData.getStations()){
            if (s.getTimeZoneGroup().equals(tzGroup) && s.getCountry().equals(country)){
                result.add(s);
            }
        }
        findByTimeZoneGroupAndCountryHelper(node.getLeft(), tzGroup, country, result);
        findByTimeZoneGroupAndCountryHelper(node.getRight(), tzGroup, country, result);
    }


    public List<NodeData> searchByCoordinatesLessThan(double x, double y){
        List<NodeData> result = new ArrayList<>();
        searchByCoordinatesLessThanHelper(root, x, y, result);
        return result;
    }

    private void searchByCoordinatesLessThanHelper(Node<NodeData> node, double x, double y, List<NodeData> result){
        if (node == null) return;

        NodeData data = node.getElement();
        double lat = data.getCoordinate().getLatitude();
        double lon = data.getCoordinate().getLongitude();

        if (lat < x && lon < y) {
            result.add(data);
        }

        searchByCoordinatesLessThanHelper(node.getLeft(), x, y, result);
        searchByCoordinatesLessThanHelper(node.getRight(), x, y, result);
    }


    public List<NodeData> searchByCoordinatesExact(double x, double y){
        List<NodeData> result = new ArrayList<>();
        searchByCoordinatesExactHelper(root, x, y, result);
        return result;
    }

    private void searchByCoordinatesExactHelper(Node<NodeData> node, double x, double y, List<NodeData> result){
        if (node == null) return;

        NodeData data = node.getElement();
        if (Double.compare(data.getCoordinate().getLatitude(), x) == 0
                && Double.compare(data.getCoordinate().getLongitude(), y) == 0) {
            result.add(data);
        }

        if (Double.compare(data.getCoordinate().getLatitude(), x) < 0)
        {
            searchByCoordinatesExactHelper(node.getLeft(), x, y, result);
        }
        else if (Double.compare(data.getCoordinate().getLatitude(), x) > 0)
        {
            searchByCoordinatesExactHelper(node.getRight(), x, y, result);
        }
        else
        {
            if (Double.compare(data.getCoordinate().getLongitude(), y) < 0) {
                searchByCoordinatesExactHelper(node.getLeft(), x, y, result);
            }
            else if (Double.compare(data.getCoordinate().getLongitude(), x) > 0) {
                searchByCoordinatesExactHelper(node.getRight(), x, y, result);
            }
        }

    }

}