/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;

import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.bean.User;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */

class EmptyListException extends RuntimeException {

    public EmptyListException() {
        this("list");
    }

    public EmptyListException(String nombre) {
        super(nombre + " is empty!");
    }
}


public class LinkedList<T> {
    Node<T> head;
    
    private int length = 0;
    
    public LinkedList(){
        this.head = null;
        this.length = 0;
    }
    


    public boolean find(T obj){
        Node<T> aux = head;

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
        Node<T> aux = head;

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
            if(head.getData() == data){
                head = head.getNext();
            }else{
                Node<T> aux = head;
                
                while(aux.getNext().getData() != data){
                    aux = aux.getNext();
                }
                
                Node next = aux.getNext().getNext();
                
                aux.setNext(next);
            }
            length--;
        }
    }
    
    public void clear(){
        head = null;
        length = 0;
    }
    
    public void add(T data){
        Node<T> temp = new Node<>(data);
        
        if(this.head == null){
            head = temp;
        }
        
        else{
            Node<T> x = head;
            
            while(x.next != null){
                x = x.next;
            }
            
            x.next = temp;
        }
        length++;
        
    }
    
    public int getPosition(T reference) throws Exception{
        int cont = 0;
        if(find(reference) == true){
            Node<T> aux = head;
            
            while(reference != aux.getData()){
                cont++;
                
                aux = aux.getNext();
            }
        }
        return cont;
    }
    
    public ArrayList<T> all(){
        ArrayList<T> data = new ArrayList<>();
        
        if(!isEmpty()){
            Node<T> aux = head;
            
            int i = 0;
            
            while(aux != null){
                data.add(aux.getData());
                aux = aux.getNext();
                i++;
            }
        }
        return data;
    }
    
    public void addByPosition(int position, T data) throws Exception{
        if(position > length + 1 ){
            throw new Exception("Posicion invalida");
        }
        
        
        if(position == 1){
            Node<T> temp = head;
            
            head = new Node<T>(data);
            
            head.next = temp;
            
            return;
        }
        
        Node<T> temp = head;
        
        Node<T> prev = new Node<T>(null);
        
        while(position - 1 > 0){
            prev = temp;
            
            temp = temp.next;
            
            position--;
        }
        
        prev.next = new Node<T>(data);
        
        prev.next.next = temp;
    }
    
    public boolean isEmpty(){
        if(head == null){
            return true;
        }
        return false;
    }
    
    public int length(){
        return this.length;
    }

}



