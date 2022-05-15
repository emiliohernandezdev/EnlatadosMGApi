/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.dto.ClientDto;
import com.emiliohernandez.enlatadosapi.service.ClientService;
import com.emiliohernandez.enlatadosapi.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/client")
public class ClientController {

    private ObjectMapper om = new ObjectMapper();
    private ClientService service = new ClientService();
    public HttpHeaders responseHeaders = new HttpHeaders();

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadClients(@RequestParam("file") MultipartFile file) throws IOException {
        String json = "";
        Response rsp = new Response();
        responseHeaders.add("Content-Type", "application/json");
        List<Client> result = service.upload(file.getInputStream());
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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Client>> getClients(@RequestParam(name = "order", defaultValue = "inOrder") String order) throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(
                service.getClients(order),
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteClient(@PathVariable Long id) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        boolean deletion = service.delete(id);
        Response rsp = new Response();
        rsp.setMessage("Cliente eliminado!");
        rsp.setResult(deletion);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Client> getClient(@PathVariable Long id) throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json");
        Client find = service.find(id);
        Response rsp = new Response();

        return new ResponseEntity<>(
                find,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateClient(@PathVariable Long id) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        service.delete(id);
        Response rsp = new Response();
        rsp.setMessage("Cliente eliminado!");
        rsp.setResult(null);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addClient(@RequestBody() ClientDto body) throws JsonProcessingException, Exception {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        try {
            Client client = new Client();
            client.setCui(body.cui);
            client.setName(body.name);
            client.setSurname(body.surname);
            client.setPhone(body.phone);
            client.setAddress(body.address);
            Response rsp = new Response();
            Client result = service.add(client);
            if (result instanceof Client) {
                rsp.setMessage("Cliente agregado correctamente.");
                rsp.setResult(client);
                rsp.setSuccess(Boolean.TRUE);
            } else {
                rsp.setMessage("Cliente no agregado, registro duplicado.");
                rsp.setResult(null);
                rsp.setSuccess(Boolean.FALSE);
            }

            json = om.writeValueAsString(rsp);

        } catch (JsonProcessingException js) {
            System.out.println(js);
            json = om.writeValueAsString(js.getMessage());
        } catch (Exception ex) {
            System.out.println(ex);
            json = om.writeValueAsString(ex.getMessage());
        }
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/graphviz", method = RequestMethod.GET)
    public ResponseEntity<String> getGraph() throws JsonProcessingException, Exception {
        Object json = "";
        String data = service.graph();
        responseHeaders.add("Content-Type", "application/json");
        Response rsp = new Response();

        json = om.writeValueAsString(data);
        return new ResponseEntity<>(
                json.toString(),
                responseHeaders,
                HttpStatus.OK
        );
    }
}
