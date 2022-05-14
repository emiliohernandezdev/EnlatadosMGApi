package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.dto.DealerDto;
import com.emiliohernandez.enlatadosapi.service.DealerService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/dealer")
public class DealerController {

    private DealerService service = new DealerService();
    private ObjectMapper om = new ObjectMapper();
    public HttpHeaders responseHeaders = new HttpHeaders();
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadDealers(@RequestParam("file") MultipartFile file) throws IOException {
        String json = "";
        Response rsp = new Response();
        responseHeaders.add("Content-Type", "application/json");
        List<Dealer> result = service.upload(file.getInputStream());
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
    public ResponseEntity<ArrayList<Dealer>> getDealers(HttpServletRequest req) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        Response resp = new Response();
        resp.setResult(service.getDealers());
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Obtained dealers!");

        json = om.writeValueAsString(resp);
        return new ResponseEntity<>(
                service.getDealers(),
                responseHeaders,
                HttpStatus.OK
        );
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addCourier(@RequestBody() DealerDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        try {
            Dealer dealer = new Dealer();
            dealer.setCui(body.cui);
            dealer.setName(body.name);
            dealer.setSurname(body.surname);
            dealer.setLicense(body.license);
            dealer.setPhone(body.phone);
            Response rsp = new Response();
            Dealer dealerCreation = this.service.add(dealer.getCui(), dealer.getName(), dealer.getSurname(), dealer.getLicense(), dealer.getPhone());
            System.out.println(service.size());
            if (dealerCreation instanceof Dealer) {
                rsp.setMessage("Repartidor agregado correctamente");
                rsp.setResult(dealer);
                rsp.setSuccess(Boolean.TRUE);
            } else {
                rsp.setMessage("El repartidor ya existe!");
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

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeDealer() throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        Response resp = new Response();
        resp.setResult(service.remove());
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Dequeued dealer!");

        json = om.writeValueAsString(resp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }
}
