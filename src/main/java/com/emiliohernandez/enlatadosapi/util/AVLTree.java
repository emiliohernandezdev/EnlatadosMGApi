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
        rootNode = insertElement(elem, rootNode);
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

    
    private AVLNode insertElement(Client element, AVLNode node){
        if(node == null)
            node = new AVLNode(element);
        else if(Long.parseLong(element.getCui()) < Long.parseLong(node.element.getCui())){
            node.leftChild = insertElement(element, node.leftChild);
            if(getHeight(node.leftChild) - getHeight(node.rightChild) == 2)
                if(Long.parseLong(element.getCui()) < Long.parseLong(node.leftChild.element.getCui()))
                    node = rotateWithLeftChild(node);
                else
                    node = doubleWithLeftChild(node);
        }
        else if(Long.parseLong(element.getCui()) > Long.parseLong(node.element.getCui())){
            node.rightChild = insertElement(element, node.rightChild);
            if(getHeight(node.rightChild) - getHeight(node.leftChild) == 2)
                if(Long.parseLong(element.getCui()) > Long.parseLong(node.rightChild.element.getCui()))
                    node = rotateWithRightChild(node);
                else
                    node = doubleWithRightChild(node);
        }
        else;
        node.height = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        System.out.println(node.toGraphviz());
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
    private AVLNode delete(AVLNode node, long key){
        if(node == null){
            return node;
        }else if(Long.parseLong(node.element.getCui()) > key){
            node.leftChild = delete(node.leftChild, key);
        }else if(Long.parseLong(node.element.getCui()) < key){
            node.rightChild = delete(node.rightChild, key);
        }else{
            if(node.leftChild == null || node.rightChild == null){
                node = (node.leftChild == null) ? node.rightChild : node.leftChild;
            }else{
                AVLNode tmp = mostLeftChild(node.rightChild);
                node.element.setCui(tmp.element.getCui());
                node.rightChild = delete(node.rightChild, Long.parseLong(node.element.getCui()));
            }
        }
        if(node != null){
            node = rebalance(node);
        }
        return node;
    }
    
    private AVLNode mostLeftChild(AVLNode node) {
        AVLNode current = node;
        while (current.leftChild != null) {
            current = current.leftChild;
        }
        return current;
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
