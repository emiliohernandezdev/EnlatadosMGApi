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
public class Stack<T>{

    private Node<T> start, end;
    private int length;
    public Stack(){
        start = end = null;
        length = 0;
    }
    
    
    public boolean isEmpty(){
        return null == start;
    }
    
    public int length(){
        return length;
    }
    
    public void push(T data){
        if(isEmpty()){
            start = end = new Node(data);
        }else{
            start = new Node(data, start);
        }
        length++;
    }
    
    public T pop(){
        if(isEmpty()) return null;
        T data = start.data;
        if(start == end){
            start = end = null;
        }else{
            start = start.next;
        }
        length--;
        return data;
    }
    
    public ArrayList<T> all(){
        ArrayList<T> data = new ArrayList<>();
        
        if(!isEmpty()){
            Node<T> aux = start;
            
            int i = 0;
            
            while(aux != null){
                data.add(aux.getData());
                aux = aux.getNext();
                i++;
            }
        }
        return data;
    }
}
