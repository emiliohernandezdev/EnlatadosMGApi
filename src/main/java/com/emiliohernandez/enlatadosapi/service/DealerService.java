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
public class DealerService {
    private static DealerService instance = null;
    private Queue<Dealer> dealers;
    private DealerService(){
        dealers = new Queue<Dealer>();
    }

    public static DealerService getInstance() {
        if(instance == null)
            instance = new DealerService();

        return instance;
    }


    public ArrayList<Dealer> getDealers() {
        return dealers.all();
    }

    public Dealer exists(String cui) {
        for (Dealer dea : getDealers()) {
            if (dea.getCui().equals(cui)) {
                return dea;
            }
        }
        return null;
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

    public void requeue(Dealer dealer){
        dealers.enqueue(dealer);
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

    public Dealer update(String cui, Dealer update){
        Dealer find = exists(cui);
        Dealer alter = dealers.update(find, update);
        return alter;
    }

    public boolean delete(String cui){
        Dealer find = exists(cui);
        dealers.remove(find);
        return true;
    }
    
    public Dealer remove() {
        return dealers.dequeue();
    }

    public int size() {
        return dealers.size();
    }


    public String getGraphviz(){
        String result = "digraph Dealers{\n" +
                "rankdir=TB;\n"
                + "node [shape = box, style=filled];\n";
        int i=0;
        for(Dealer dlr : dealers.all()){
            i+=1;
            result += i+ " " +"[ label =\""+dlr.getCui() + " - " + dlr.getName() + " " +dlr.getSurname() + "\"];\n";
        }

        for(int cont=size(); cont>1; cont--){
            result += (cont) + "->" + (cont-1) + "; \n"+ "\n";
        }
        result += "}";
        System.out.println(result);
        return result;
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
