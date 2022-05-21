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

    public boolean find(T obj){
        Node<T> aux = start;

        boolean finded = false;

        while(aux != null && finded != true){
            if(obj == aux.getData()){
                finded = true;
            }else{
                aux = aux.getNext();
            }
        }
        return finded;
    }

    public T update(T data, T update){
        Node<T> aux = start;

        if(find(data)){
            while(aux.getData() != data){
                aux = aux.getNext();
            }
            aux.setData(update);
        }
        return aux.getData();
    }

    public void remove(T data){
        if(find(data) == true){
            if(start.getData() == data){
                start = start.getNext();
            }else{
                Node<T> aux = start;

                while(aux.getNext().getData() != data){
                    aux = aux.getNext();
                }

                Node next = aux.getNext().getNext();

                aux.setNext(next);
            }
            length--;
        }
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
