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
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.util.Enumeration;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                System.out.println("Header: " + httpRequest.getHeader(headerNames.nextElement()));
            }
        }
        String json = "";
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(java.util.Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        System.out.println("requesturi: " + req.getRequestURI());
        String token = req.getHeader("Authorization");
        System.out.println("Inicia token");
        System.out.println(token);
        System.out.println("Finaliza token");
        Long sub = Long.parseLong(Jwts.parser()
                .setSigningKey(hmacKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());

        System.out.println("Sub: " + sub);

        User find = service.exists(sub.toString());
        Response rsp = new Response();
        if(find != null){
            rsp.setMessage("Perfil obtenido");
            rsp.setSuccess(Boolean.TRUE);
            rsp.setResult(find);
        }else{
            rsp.setMessage("Usuario inexistente");
            rsp.setSuccess(Boolean.FALSE);
            rsp.setResult(null);
        }

        json = om.writeValueAsString(rsp);
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

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        int userId = id.intValue();
        service.deleteUser(userId);
        Response rsp = new Response();
        rsp.setMessage("Usuario eliminado!");
        rsp.setResult(null);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody() UserDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        int userId = id.intValue();
        Response rsp = new Response();
        if(userId >0){
            User find = service.exists(String.valueOf(userId));
            if(find != null){
                User update = new User();
                update.setId(userId);
                update.setName(body.name);
                update.setSurname(body.surname);
                update.setPassword(body.password);
                User result = service.update(userId, update);

                if(result instanceof User){
                    rsp.setMessage("Usuario actualizado!");
                    rsp.setResult(result);
                    rsp.setSuccess(Boolean.TRUE);
                }else{
                    rsp.setMessage("Error al actualizar el usuario!");
                    rsp.setResult(null);
                    rsp.setSuccess(Boolean.TRUE);
                }
            }else{
                rsp.setMessage("Usuario inexistente.");
                rsp.setResult(null);
                rsp.setSuccess(Boolean.FALSE);
            }
        }else{
            rsp.setMessage("El ID del usuario no es válido");
            rsp.setResult(null);
            rsp.setSuccess(Boolean.FALSE);
        }

        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }


    @RequestMapping(value = "/graphviz", method = RequestMethod.GET)
    public ResponseEntity<String> getGraphviz() throws JsonProcessingException, Exception{

        responseHeaders.add("Content-Type", "application/json");

        return new ResponseEntity<>(
                service.getGraphviz(),
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
