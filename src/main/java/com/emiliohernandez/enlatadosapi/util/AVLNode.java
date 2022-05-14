/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;

import com.emiliohernandez.enlatadosapi.bean.Client;


/**
 *
 * @author emilio.hernandez
 */
public class AVLNode{
    Client element;
    int height;
    AVLNode leftChild;
    AVLNode rightChild;
    private final int id;
    private static int correlativo=1;
    
    public AVLNode(){
        leftChild = null;
        rightChild = null;
        element = null;
        height = 0;
        this.id = 0;
    }
    
    public Client getElement(){
        return this.element;
    }
    
    public AVLNode(Client elem){
        leftChild = null;
        rightChild = null;
        this.element = elem;
        this.height = 0;
        this.id = correlativo++;
    }
    
    
    public String toGraphviz(){
        return "digraph Clients{\n" + 
                "rankdir=TB;\n"
                + "node [shape = record, style=filled];\n"
                + generateGraphvizObjects()
                + "}\n";
    }
    
    public String generateGraphvizObjects(){
        String label = "";
        Client client = new Client(element);
        if(leftChild == null && rightChild == null){
            label = "nodo"+id+" [ label =\""+client.getName()+ " " + client.getSurname() + " - " + client.getCui() + "\"];\n";
        }else{
            label = "nodo"+id+" [ label =\"<C0>|"+client.getName()+ " " + client.getSurname() + " - " + client.getCui() +"|<C1>\"];\n";
        }
        if(leftChild != null){
            label=label + leftChild.generateGraphvizObjects() +
               "nodo"+id+":C0->nodo"+leftChild.id+"\n";
        }
        if(rightChild != null){
            label=label + rightChild.generateGraphvizObjects() +
               "nodo"+id+":C1->nodo"+rightChild.id+"\n";  
        }
        return label;
    }
}