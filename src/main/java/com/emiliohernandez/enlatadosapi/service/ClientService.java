/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.util.AVLNode;
import com.emiliohernandez.enlatadosapi.util.AVLTree;
import com.emiliohernandez.enlatadosapi.util.CsvHelper;
import com.emiliohernandez.enlatadosapi.util.Node;
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
public class ClientService {

    private static ClientService instance = null;
    private AVLTree clients;
    private ClientService(){
        clients = new AVLTree();
    }

    public static ClientService getInstance() {
        if(instance == null)
            instance = new ClientService();

        return instance;
    }

    public ArrayList<Client> getClients(String order) {
        return clients.getAllByOrder(order);
    }



    public Client add(Client client) {
        Client cl = new Client();
        cl.setCui(client.getCui());
        cl.setName(client.getName());
        cl.setSurname(client.getSurname());
        cl.setPhone(client.getPhone());
        cl.setAddress(client.getAddress());
        if (clients.searchElement(cl) != true) {
            clients.insertElement(cl);
            return cl;
        }
        return null;
    }

    public String graph() {
        return clients.Graph();
    }

    public Client find(Long key) {

        AVLNode data = clients.find(key);
        if (data != null) {
            Client obtained = data.getElement();
            if (obtained != null && obtained instanceof Client) {
                return data.getElement();
            }
        }

        return null;
    }

    public boolean delete(Long key) {
        if(clients.find(key) != null){
            clients.deleteElement(key);
            return true;
        }
        return false;
    }

    public boolean exists(Client c) {
        boolean find = false;
        for (Client client : clients.getAllByOrder()) {
            if (client.getCui().equals(c.getCui())) {
                find = true;
            }
        }
        return find;
    }


    public List<Client> upload(InputStream is) {
        ArrayList<Client> inserteds = new ArrayList<>();

        try {
            CsvHelper helper = new CsvHelper();
            ArrayList<Client> cl = (ArrayList<Client>) helper.csvToClients(is);
            cl.forEach((u) -> {
                if (!exists(u)) {
                    try {
                        add(u);
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
