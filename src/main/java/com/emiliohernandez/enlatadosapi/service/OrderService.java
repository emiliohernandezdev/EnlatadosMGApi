package com.emiliohernandez.enlatadosapi.service;


import com.emiliohernandez.enlatadosapi.bean.*;
import com.emiliohernandez.enlatadosapi.util.LinkedList;

import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */
public class OrderService {
    private LinkedList<Order> orders = new LinkedList();
    private ClientService clientService = new ClientService();

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

    public Order add(Order order, String client, String dealer, String vehicle){
        Order ord = new Order();
        ord.setNumber("#-" + hexEncode(String.valueOf(System.identityHashCode(ord)).getBytes()));
        ord.setOrigin(order.getOrigin());
        ord.setDestination(order.getDestination());
        ord.setStatus(order.getStatus());
        ord.setDate(order.getDate());

        orders.add(ord);
        return ord;
    }
}
