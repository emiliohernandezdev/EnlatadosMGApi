/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.service;

import com.emiliohernandez.enlatadosapi.bean.User;
import com.emiliohernandez.enlatadosapi.util.AuthResponse;
import com.emiliohernandez.enlatadosapi.util.CsvHelper;
import com.emiliohernandez.enlatadosapi.util.JwtUtil;
import com.emiliohernandez.enlatadosapi.util.LinkedList;
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
public class UserService {

    private LinkedList<User> usersList = new LinkedList();

    public ArrayList<User> getUsers() {
        ArrayList<User> result= usersList.all();
        if(result.size() > 0){
            return usersList.all();
        }else{
            return new ArrayList<User>();
        }
        
    }

    public boolean exists(User u) {
        boolean find = false;
        for (User usr : usersList.all()) {
            if (usr.getId() == u.getId()) {
                find = true;
            }
        }
        return find;
    }

    public User exists(String id) {
        for (User usr : usersList.all()) {
            if (usr.getId() == Integer.parseInt(id)) {
                return usr;
        }  
    }
        return null;
}

    public User addUser(int id, String name, String surname, String password) throws Exception {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(password);
        if (!exists(user)) {
            usersList.add(user);
            return user;
        }
        return null;

    }

    public AuthResponse doLogin(String id, String password) {
        JwtUtil util = new JwtUtil();
        User find = exists(id);
        if (find instanceof User) {
            if (find.getPassword().equals(password)) {
                //Verificar contraseña 
                return new AuthResponse("Sesión iniciada", util.createToken(id, find.getName()), true);
            } else {
                 return new AuthResponse("Credenciales inválidas", null, false);
            }
        } else {
            return new AuthResponse("El usuario no existe.", null, false);
        }
    }

    public List<User> upload(InputStream is) {
        ArrayList<User> inserteds = new ArrayList<>();

        try {
            CsvHelper helper = new CsvHelper();
            ArrayList<User> users = (ArrayList<User>) helper.csvToUsers(is);
            users.forEach((u) -> {
                if (!exists(u)) {
                    try {
                        addUser(u.getId(), u.getName(), u.getSurname(), u.getPassword());
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
