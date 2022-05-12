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
    private int correlative;
    private Date entryDate;

    public int getCorrelative() {
        return correlative;
    }

    public void setCorrelative(int correlative) {
        this.correlative = correlative;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
}
