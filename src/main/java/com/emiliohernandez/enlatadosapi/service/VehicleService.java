/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.Vehicle;
import com.emiliohernandez.enlatadosapi.util.CsvHelper;
import com.emiliohernandez.enlatadosapi.util.Queue;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author emilio.hernandez
 */
public class VehicleService {
    private Queue<Vehicle> vehicles = new Queue<Vehicle>();
    
    
    public ArrayList<Vehicle> getVehicles() {
        return vehicles.all();
    }
    
    public boolean existsById(String licensePlate) {
        boolean find = false;
        if (!vehicles.isEmpty()) {
            for (Vehicle veh : getVehicles()) {
                if (veh.getLicensePlate().equals(licensePlate)) {
                    find = true;
                }
            }
        }
        return find;
    }
    
    public boolean exists(Vehicle v) {
        boolean find = false;
        if (!vehicles.isEmpty()) {
            for (Vehicle vh : getVehicles()) {
                if (vh.getLicensePlate().equals(v.getLicensePlate())) {
                    find = true;
                }
            }
        }
        return find;
    }
    
    public Vehicle add(String licensePlate, String brand, String model, String color, int year){
        Vehicle vehi = new Vehicle();
        vehi.setLicensePlate(licensePlate);
        vehi.setBrand(brand);
        vehi.setModel(model);
        vehi.setColor(color);
        vehi.setYear(year);
        if (!existsById(vehi.getLicensePlate()) == true) {
            vehicles.enqueue(vehi);
            return vehi;
        }  
        return null;
    }
    
    public List<Vehicle> upload(InputStream is) {
        ArrayList<Vehicle> inserteds = new ArrayList<>();

        try {
            CsvHelper helper = new CsvHelper();
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) helper.csvToVehicles(is);
            vehicles.forEach((u) -> {
                if (!exists(u)) {
                    try {
                        add(u.getLicensePlate(), u.getBrand(), u.getModel(), u.getColor(), u.getYear());
                        inserteds.add(u);
                    } catch (Exception ex) {
                        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return inserteds;
    }
    
    
}
