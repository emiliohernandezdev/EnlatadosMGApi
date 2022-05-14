/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.bean;

import java.util.Date;

/**
 *
 * @author emilio.hernandez
 */
public class Stock {
    private String correlative;
    private Date entryDate;

    public Stock(){
        
    }
    public Stock(String correlative, Date entryDate) {

        this.correlative = correlative;
        this.entryDate = entryDate;
    }

    public String getCorrelative() {
        return correlative;
    }

    public void setCorrelative(String correlative) {
        this.correlative = correlative;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
}
