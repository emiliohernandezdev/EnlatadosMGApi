/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;
import java.io.*;
/**
 *
 * @author emilio.hernandez
 */
public class Node<T>{
    T data;
    
    Node<T> next;
    
    public Node(T data){
        this.data = data;
        this.next = null;
    }
    
    public Node(T data, Node<T> next){
        setData(data);
        setNext(next);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
    
    
}