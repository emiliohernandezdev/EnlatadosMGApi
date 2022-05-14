/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.util.CsvHelper;
import com.emiliohernandez.enlatadosapi.util.JwtUtil;
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
public class DealerService {

    private Queue<Dealer> dealers = new Queue<Dealer>();

    public ArrayList<Dealer> getDealers() {
        return dealers.all();
    }

    public boolean exists(Dealer d) {
        boolean find = false;
        if (!dealers.isEmpty()) {
            for (Dealer dlr : getDealers()) {
                if (dlr.getCui().equals(d.getCui())) {
                    find = true;
                }
            }
        }
        return find;
    }

    public boolean existsById(String cui) {
        boolean find = false;
        if (!dealers.isEmpty()) {
            for (Dealer dlr : getDealers()) {
                if (dlr.getCui().equals(cui)) {
                    find = true;
                }
            }
        }
        return find;
    }

    public Dealer add(String cui, String name, String surname, String license, String phone) {
        Dealer dealer = new Dealer();
        dealer.setCui(cui);
        dealer.setName(name);
        dealer.setSurname(surname);
        dealer.setLicense(license);
        dealer.setPhone(phone);
        if (!existsById(cui) == true) {
            dealers.enqueue(dealer);
            return dealer;
        }   
        return null;
    }
    
    public Dealer remove() {
        return dealers.dequeue();
    }

    public int size() {
        return dealers.size();
    }
    
    public List<Dealer> upload(InputStream is) {
        ArrayList<Dealer> inserteds = new ArrayList<>();

        try {
            CsvHelper helper = new CsvHelper();
            ArrayList<Dealer> cl = (ArrayList<Dealer>) helper.csvToDealers(is);
            cl.forEach((u) -> {
                if (!exists(u)) {
                    try {
                        add(u.getCui(), u.getName(), u.getSurname(), u.getLicense(), u.getPhone());
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
