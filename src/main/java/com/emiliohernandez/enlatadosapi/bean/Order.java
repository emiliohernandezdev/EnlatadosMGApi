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
public class Order {
    private String orderNumber;
    private String originDepartment;
    private String destinationDepartment;
    private Date startDate;
    private Client client;
    private Dealer dealer;
    private Vehicle vehicle;
    private Stock boxes;
    private String status;
    
    public Order(String orderNumber, String originDepartment, String destinationDepartment, Date startDate, Client client, Dealer dealer, Vehicle vehicle, String status) {
        this.orderNumber = orderNumber;
        this.originDepartment = originDepartment;
        this.destinationDepartment = destinationDepartment;
        this.startDate = startDate;
        this.client = client;
        this.dealer = dealer;
        this.vehicle = vehicle;
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOriginDepartment() {
        return originDepartment;
    }

    public void setOriginDepartment(String originDepartment) {
        this.originDepartment = originDepartment;
    }

    public String getDestinationDepartment() {
        return destinationDepartment;
    }

    public void setDestinationDepartment(String destinationDepartment) {
        this.destinationDepartment = destinationDepartment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
