package com.emiliohernandez.enlatadosapi.service;


import com.emiliohernandez.enlatadosapi.bean.*;
import com.emiliohernandez.enlatadosapi.util.LinkedList;

import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */
public class OrderService {
    private static OrderService instance = null;
    private LinkedList<Order> orders;

    public OrderService(){
        orders = new LinkedList();
    }

    public static OrderService getInstance(){
        if(instance == null)
            instance = new OrderService();
        return instance;
    }

    public Order exists(String id) {
        for (Order ord : orders.all()) {
            if (ord.getNumber().equals(id)) {
                return ord;
            }
        }
        return null;
    }

    public Order update(String id, Order update){
        Order find = exists(id);

        Order alter = orders.update(find, update);
        return alter;
    }

    public ArrayList<Order> getOrders() {
        return orders.all();
    }

    public String hexEncode(byte[] input){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
        for (int idx = 0; idx < input.length; ++idx) {
            byte b = input[idx];
            result.append(digits[ (b&0xf0) >> 4 ]);
            result.append(digits[ b&0x0f]);
        }
        return result.toString();
    }

    public Order add(Order order){

        Order ord = new Order();
        ord.setNumber(order.getNumber());
        ord.setOrigin(order.getOrigin());
        ord.setDestination(order.getDestination());
        ord.setStatus(order.getStatus());
        ord.setDate(order.getDate());
        ord.setVehicle(order.getVehicle());
        ord.setDealer(order.getDealer());
        ord.setClient(order.getClient());
        ord.setStock(new Stock());

        orders.add(ord);
        return ord;
    }

    public String getGraphviz(){
        String result = "digraph Orders{\n" +
                "rankdir=TB;\n"
                + "node [shape = box, style=filled];\n";
        int i=0;
        for(Order ord : orders.all()){
            i+=1;
            result += i+ " " +"[ label =\""+ord.getNumber() + " - " + ord.getOrigin() + " " +ord.getDestination() + "\"];\n";
        }
        for(int k=2; k<=orders.length(); k++){
            result += (k-1) + "->" + k + "; \n"+ "\n";
        }
        result += "}";
        return result;
    }
}
