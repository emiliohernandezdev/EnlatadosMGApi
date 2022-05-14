/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.Stock;
import com.emiliohernandez.enlatadosapi.util.Stack;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author emilio.hernandez
 */
public class StockService {
    private Stack<Stock> stock = new Stack<>();
    
    public ArrayList<Stock> getAll(){
        return stock.all();
    }
    
    public Stock add(Stock stck){
        Stock stk = new Stock();
        
        UUID uuid = UUID.randomUUID();
        stk.setCorrelative(uuid.toString().toUpperCase());
        stk.setEntryDate(stck.getEntryDate());
        stock.push(stk);
        return stk;
    }
    
    public Stock remove(){
        return stock.pop();
    }

    
}
