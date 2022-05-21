package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.bean.Vehicle;
import com.emiliohernandez.enlatadosapi.dto.DealerDto;
import com.emiliohernandez.enlatadosapi.dto.VehicleDto;
import com.emiliohernandez.enlatadosapi.service.DealerService;
import com.emiliohernandez.enlatadosapi.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private DealerService service = DealerService.getInstance();
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
    public ResponseEntity<String> addDealer(@RequestBody() DealerDto body) throws JsonProcessingException {
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

    @RequestMapping(value = "/update/{cui}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateDealer(@PathVariable("cui") String cui, @RequestBody() DealerDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        Response rsp = new Response();
        Dealer find = service.exists(cui);
        if(find != null){
            Dealer dealer = new Dealer();
            dealer.setCui(body.cui);
            dealer.setName(body.name);
            dealer.setSurname(body.surname);
            dealer.setCui(body.cui);
            dealer.setPhone(body.phone);
            dealer.setLicense(body.license);

            Dealer result = service.update(cui, dealer);

            if(result instanceof Dealer){
                rsp.setMessage("Repartidor actualizado!");
                rsp.setResult(result);
                rsp.setSuccess(Boolean.TRUE);
            }else{
                rsp.setMessage("Error al actualizar el repartidor!");
                rsp.setResult(null);
                rsp.setSuccess(Boolean.TRUE);
            }
        }else{
            rsp.setMessage("Repartidor no encontrado.");
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Dealer> getDealer(@PathVariable Long id) throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json");
        Dealer find = service.exists(String.valueOf(id));
        return new ResponseEntity<>(
                find,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/graphviz", method = RequestMethod.GET)
    public ResponseEntity<String> getGraphviz() throws JsonProcessingException, Exception{

        responseHeaders.add("Content-Type", "application/json");

        String graph = service.getGraphviz();

        return new ResponseEntity<>(
                URLEncoder.encode(graph, StandardCharsets.UTF_8.toString()),
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/delete/{cui}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteDealer(@PathVariable("cui") String cui) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        service.delete(cui);
        Response rsp = new Response();
        rsp.setMessage("Repartidor eliminado!");
        rsp.setResult(null);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
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
