/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */
public class Queue<T> {
    
    private int length;
    private Node<T> start, end;
    
    public Queue(){
        start = end = null;
        length = 0;
    }
    
    public int size(){
        return length;
    }
    
    public boolean isEmpty(){
        return null == start;
    }
    
    public void enqueue(T data){
        if(isEmpty()){
            start = end = new Node<T>(data);
        }else{
            end = end.next = new Node<T>(data);
        }
        length++;
    }
    
    public T dequeue(){
        if(isEmpty()) return null;
        T dato = start.data;
        if(start == end){
            start = end = null;
        }else{
            start = start.next;
        }
        length--;
        return dato;
    }
    
    public ArrayList<T> all(){
        ArrayList<T> data = new ArrayList<>();
        if(isEmpty()){
            return new ArrayList<T>();
        }
        Node<T> actual = start;
        while (actual != null) {
            data.add(actual.data);
            actual = actual.next;
        }
        return data;
    }

}
