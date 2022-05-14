/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;

import com.emiliohernandez.enlatadosapi.bean.Client;
import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */

public class AVLTree {
    
    private AVLNode rootNode;

    
    public AVLTree(){
        rootNode = null;
    }
    
    public void removeAll(){
        rootNode = null;
    }
    
    public boolean isEmpty(){
        if(rootNode == null)
            return true;
        else
            return false;
    }
    
    public void insertElement(Client elem){
        rootNode = insertElement(rootNode, Long.parseLong(elem.getCui()));
    }
    
    public void deleteElement(Long key){
        rootNode = delete(rootNode, key);
    }
    
    private int getHeight(AVLNode node){
        return node == null ? -1 : node.height;
    }
    
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight)  
    {  
    return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;  
    }  
      
    
    public String Graph(){
        return rootNode.toGraphviz();
    }

    
    private AVLNode insertElement(AVLNode node, long key){
        /* 1. Perform the normal BST rotation */
        if (node == null)
            return (new AVLNode());
 
        if (key < Long.parseLong(node.element.getCui()))
            node.leftChild = insertElement(node.leftChild, key);
        else if (key > Long.parseLong(node.element.getCui()))
            node.rightChild = insertElement(node.rightChild, key);
        else 
            return node;

        node.height = 1 + max(height(node.leftChild),
                            height(node.rightChild));

        int balance = getBalance(node);

        if (balance > 1 && key < Long.parseLong(node.leftChild.element.getCui()))
            return rightRotate(node);

        if (balance < -1 && key > Long.parseLong(node.rightChild.element.getCui()))
            return leftRotate(node);
 
        if (balance > 1 && key > Long.parseLong(node.leftChild.element.getCui()))
        {
            node.leftChild = leftRotate(node.leftChild);
            return rightRotate(node);
        }
 
        // Right Left Case
        if (balance < -1 && key < Long.parseLong(node.rightChild.element.getCui()))
        {
            node.rightChild = rightRotate(node.rightChild);
            return leftRotate(node);
        }

        return node;
    }
    private int height(AVLNode n){
        return n == null ? -1 : n.height;
    }
    
    private int getBalance(AVLNode n){
        return (n == null) ? 0 : height(n.rightChild) - height(n.leftChild);
    }
    private void updateHeight(AVLNode n){
        n.height = 1 + Math.max(height(n.leftChild), height(n.rightChild));
    }
    
    private AVLNode rotateLeft(AVLNode y){
        AVLNode x = y.rightChild;
        AVLNode z = x.leftChild;
        x.leftChild = y;
        y.rightChild = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }
    
    private AVLNode rotateRight(AVLNode y){
        AVLNode x = y.leftChild;
        AVLNode z = x.rightChild;
        x.rightChild = y;
        y.leftChild = z;
        updateHeight(y);
        updateHeight(x);
        return x;
    }
    private AVLNode rebalance(AVLNode z){
        updateHeight(z);
        int balance = getBalance(z);
        if(balance > 1 ){
            if(height(z.rightChild.rightChild) > height(z.rightChild.leftChild)){
                z = rotateLeft(z);
            }else{
                z.rightChild = rotateRight(z.rightChild);
                z = rotateLeft(z);
            }
        }else if(balance <-1){
            if(height(z.leftChild.leftChild) > height(z.leftChild.rightChild))
                z = rotateRight(z);
            else{
                z.leftChild = rotateLeft(z.leftChild);
                z = rotateRight(z);
            }
        }
        return z;
    }
    
    private AVLNode minValueNode(AVLNode node)
    {
        AVLNode current = node;
        while (current.leftChild != null)
        current = current.leftChild;
 
        return current;
    }
    
    int max(int a, int b)
    {
        return (a > b) ? a : b;
    }
    
    
    AVLNode leftRotate(AVLNode x)
    {
        AVLNode y = x.rightChild;
        AVLNode T2 = y.leftChild;

        y.leftChild = x;
        x.rightChild = T2;
 

        x.height = max(height(x.leftChild), height(x.rightChild)) + 1;
        y.height = max(height(y.leftChild), height(y.rightChild)) + 1;

        return y;
    }

    
    private AVLNode rightRotate(AVLNode y)
    {
        AVLNode x = y.leftChild;
        AVLNode T2 = x.rightChild;

        x.rightChild = y;
        y.leftChild = T2;

        y.height = max(height(y.leftChild), height(y.rightChild)) + 1;
        x.height = max(height(x.leftChild), height(x.rightChild)) + 1;
 
        return x;
    }
    private AVLNode delete(AVLNode root, long key){
        if (root == null)
            return root;
        if (key < Long.parseLong(root.element.getCui()))
            root.leftChild = delete(root.leftChild, key);

        else if (key > Long.parseLong(root.element.getCui()))
            root.rightChild = delete(root.rightChild, key);
 
        else
        {
            if ((root.leftChild == null) || (root.rightChild == null))
            {
                AVLNode temp = null;
                if (temp == root.leftChild)
                    temp = root.rightChild;
                else
                    temp = root.leftChild;
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else 
                    root = temp;
            }
            else
            {
 
                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                AVLNode temp = minValueNode(root.rightChild);
 
                // Copy the inorder successor's data to this node
                root.element.setCui(temp.element.getCui());
 
                // Delete the inorder successor
                root.rightChild = delete(root.rightChild, Long.parseLong(temp.element.getCui()));
            }
        }
 
        if (root == null)
            return root;
        root.height = max(height(root.leftChild), height(root.rightChild)) + 1;

        int balance = getBalance(root);
 
        if (balance > 1 && getBalance(root.leftChild) >= 0)
            return rightRotate(root);
        if (balance > 1 && getBalance(root.leftChild) < 0)
        {
            root.leftChild = leftRotate(root.leftChild);
            return rightRotate(root);
        }
 
        if (balance < -1 && getBalance(root.rightChild) <= 0)
            return leftRotate(root);
 
        if (balance < -1 && getBalance(root.rightChild) > 0)
        {
            root.rightChild = rightRotate(root.rightChild);
            return leftRotate(root);
        }
 
        return root;
    }
    
    
    private AVLNode rotateWithLeftChild(AVLNode node){
        AVLNode nodeP = node.leftChild;
        node.leftChild = nodeP.rightChild;
        nodeP.rightChild = node;
        node.height = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        nodeP.height = getMaxHeight(getHeight(nodeP.leftChild), node.height) + 1;
        return nodeP;
    }
    
    private AVLNode rotateWithRightChild(AVLNode node){
        AVLNode aux = node.rightChild;
        node.rightChild = aux.leftChild;
        aux.leftChild = node;
        node.height = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) +1 ;
        aux.height = getMaxHeight(getHeight(aux.rightChild), node.height) + 1;
        return aux;
    }
    
    private AVLNode doubleWithLeftChild(AVLNode node){
        node.leftChild = rotateWithRightChild(node.leftChild);
        return rotateWithLeftChild(node);
    }
    
    private AVLNode doubleWithRightChild(AVLNode node){
        node.rightChild = rotateWithLeftChild(node.rightChild);
        return rotateWithRightChild(node);
    }
    
    
    public int getNodes(){
        return getNodes(rootNode);
    }

    
    private int getNodes(AVLNode head){
        if(head == null)
            return 0;
        else{
            int length = 1;
            length = length + getNodes(head.leftChild);
            length = length + getNodes(head.rightChild);
            return length;
        }
    }
    
    public boolean searchElement(Client element)  
    {  
        return searchElement(rootNode, element);  
    }  
    
    public AVLNode find(Long key){
        AVLNode temp = search(rootNode, key);
        return temp != null ? temp : null;
    }
    
    private AVLNode search(AVLNode root, Long key){
        if(root == null || Long.parseLong(root.element.getCui()) == key)
            return root;
        
        if(Long.parseLong(root.element.getCui()) < key)
            return search(root.rightChild, key);
        
        return search(root.leftChild, key);
    }
    
    private boolean searchElement(AVLNode head, Client search){
        boolean check = false;
        while((head != null) && !check){
            Client headElement = head.element;
            if(Long.parseLong(search.getCui()) < Long.parseLong(headElement.getCui()))
                head = head.leftChild;
            else if(Long.parseLong(search.getCui()) > Long.parseLong(headElement.getCui())){
                head = head.rightChild;
            }
            else{
                check = true;
                break;
            }
            check = searchElement(head, search);
        }
        return check;
    }
    
    public ArrayList<Client> all(String order){
        return new ArrayList<>();
    }
    
    private ArrayList<Client> clients = new ArrayList<>();
    
    public ArrayList<Client> getAllByOrder(String order){
        if(!clients.isEmpty()) clients.clear();
        switch(order){
            case "preOrder":
                System.out.println("preOrder");
                preOrder(rootNode);
                break;
                
            case "inOrder":
                System.out.println("inOrder");
                inOrder(rootNode);
                break;
            
            case "posOrder":
                System.out.println("posOrder");
                posOrder(rootNode);
                break;
                
            default:
                inOrder(rootNode);
                break;
        }
        return clients;
    }
    
    public ArrayList<Client> getAllByOrder(){
        if(!clients.isEmpty()) clients.clear();
        inOrder(rootNode);
        return clients;
    }
    
    private void preOrder(AVLNode node){
        if(node == null) return;
        clients.add(node.element);
        preOrder(node.leftChild);
        preOrder(node.rightChild);
    }
    private void inOrder(AVLNode node){
        if(node == null) return;
        inOrder(node.leftChild);
        clients.add(node.element);
        inOrder(node.rightChild);
    }
    
    private void posOrder(AVLNode node){
        if(node == null) return;
        posOrder(node.leftChild);
        posOrder(node.rightChild);
        clients.add(node.element);
    }
    
    
}
