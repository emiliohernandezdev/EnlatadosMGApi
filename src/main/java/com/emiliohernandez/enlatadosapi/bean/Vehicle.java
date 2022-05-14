/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.bean;

/**
 *
 * @author emilio.hernandez
 */
public class Vehicle {
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private int year;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public String toJson(){
        return "{'licensePlate': + "
                + " licensePlate + " 
                + "'brand': " + brand + ""
                + "'model': " + model + ""
                + "'color': " + color + ""
                + "'year': " + year + "}"; 
    }
}
