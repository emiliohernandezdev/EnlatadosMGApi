/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.bean.User;
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

    public String getGraphviz(){
        String result = "digraph Vehicle{\n" +
                "rankdir=TB;\n"
                + "node [shape = box, style=filled];\n";
        int i=0;
        for(Vehicle vh : vehicles.all()){
            i+=1;
            result += i+ " " +"[ label =\""+vh.getLicensePlate() + " - " + vh.getBrand() + " " + vh.getModel() + "\"];\n";
        }

        for(int cont=vehicles.size(); cont>1; cont--){
            result += (cont) + "->" + (cont-1) + "; \n"+ "\n";
        }
        result += "}";
        System.out.println(result);
        return result;
    }

    public Vehicle findById(String licensePlate){
        if(vehicles.isEmpty()) return null;
        Vehicle isFinded = null;
        for(Vehicle v: vehicles.all()){
            if(v.getLicensePlate().equals(licensePlate)){
                isFinded = v;
            }
            isFinded = findById(licensePlate);
        }
        return isFinded;
    }


    public Vehicle exists(String licensePlate) {
        System.out.println("Param: " + licensePlate);
        for (Vehicle veh : vehicles.all()) {
            if (veh.getLicensePlate().equals(licensePlate)) {
                return veh;
            }
        }
        return null;
    }

    public boolean exists(Vehicle vh) {
        boolean result = false;
        for (Vehicle veh : vehicles.all()) {
            if (veh.getLicensePlate().equals(vh.getLicensePlate())) {
                 result= true;
            }
        }
        return result;
    }
    
    public Vehicle add(String licensePlate, String brand, String model, String color, int year){
        Vehicle vh = new Vehicle();
        vh.setLicensePlate(licensePlate);
        vh.setBrand(brand);
        vh.setModel(model);
        vh.setColor(color);
        vh.setYear(year);

        Vehicle exists = exists(vh.getLicensePlate());
        if (exists == null) {
            vehicles.enqueue(vh);
            return vh;
        }  
        return null;
    }

    public Vehicle update(String license, Vehicle update){
        Vehicle find = exists(license);
        Vehicle alter = vehicles.update(find, update);
        return alter;
    }

    public boolean delete(String licensePlate){
        Vehicle find = exists(licensePlate);
        vehicles.remove(find);
        return true;
    }

    public List<Vehicle> upload(InputStream is) {
        ArrayList<Vehicle> inserteds = new ArrayList<>();

        try {
            CsvHelper helper = new CsvHelper();
            ArrayList<Vehicle> uploadedVehicles = (ArrayList<Vehicle>) helper.csvToVehicles(is);
            uploadedVehicles.forEach((v) -> {
                if (!exists(v)) {
                    try {
                        add(v.getLicensePlate(), v.getBrand(), v.getModel(), v.getColor(), v.getYear());
                        inserteds.add(v);
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
