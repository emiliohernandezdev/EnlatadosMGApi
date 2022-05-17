/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.User;
import com.emiliohernandez.enlatadosapi.dto.UserDto;
import com.emiliohernandez.enlatadosapi.service.UserService;
import com.emiliohernandez.enlatadosapi.util.AuthResponse;
import com.emiliohernandez.enlatadosapi.util.JwtUtil;
import com.emiliohernandez.enlatadosapi.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService service = new UserService();
    private ObjectMapper om = new ObjectMapper();
    public HttpHeaders responseHeaders = new HttpHeaders();

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public ResponseEntity<String> userProfile(HttpServletRequest req) throws  JsonProcessingException{
        //Obtener token
        JwtUtil util = new JwtUtil();
        String token = req.getHeader("Authorization");
        Object data = util.getInfoToken(token);
        User verify = service.exists(data.id);
        String json = "";
        User result = null;
        if(verify != null){
            result = verify;
        }else{
            result = null;
        }

        json = om.writeValueAsString(result);
        return  new ResponseEntity<>(
          json,
          responseHeaders,
          HttpStatus.OK
        );
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> obtenerUsuario(HttpServletRequest req) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        Response resp = new Response();
        resp.setResult(service.getUsers());
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Obtained users!");

        json = om.writeValueAsString(resp);
        return new ResponseEntity<>(
                service.getUsers(),
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestBody() UserDto body) throws JsonProcessingException, Exception {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        try {
            User usr = new User();
            usr.setId(body.id);
            usr.setName(body.name);
            usr.setSurname(body.surname);
            usr.setPassword(body.password);
            Response rsp = new Response();
            User created = this.service.addUser(usr.getId(), usr.getName(), usr.getSurname(), usr.getPassword());
            if (created instanceof User) {
                rsp.setMessage("Usuario agregado!");
                rsp.setResult(usr);
                rsp.setSuccess(Boolean.TRUE);
            } else {
                rsp.setMessage("El usuario ya existe!");
                rsp.setResult(null);
                rsp.setSuccess(Boolean.FALSE);
            }
            json = om.writeValueAsString(rsp);

        } catch (JsonProcessingException js) {
            json = om.writeValueAsString(js.getMessage());
        } catch (Exception ex) {
            json = om.writeValueAsString(ex.getMessage());
        }
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody() UserDto body) throws JsonProcessingException {
        String json = "";

        AuthResponse result = service.doLogin(String.valueOf(body.id), body.password);
        User exists = service.exists(String.valueOf(body.id));
        if (exists != null) {
            Response rsp = new Response();
            rsp.setSuccess(result.isAuth());
            rsp.setResult(result.getToken());
            rsp.setMessage(result.getMessage());

            json = om.writeValueAsString(rsp);
        }else{
            Response rsp = new Response();
            rsp.setSuccess(result.isAuth());
            rsp.setResult(null);
            rsp.setMessage(result.getMessage());
            
            json = om.writeValueAsString(rsp);
        }

        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
        String json = "";
        Response rsp = new Response();
        responseHeaders.add("Content-Type", "application/json");
        List<User> result = service.upload(file.getInputStream());
        rsp.setMessage("Carga masiva realizada!");
        rsp.setResult(result);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

}
