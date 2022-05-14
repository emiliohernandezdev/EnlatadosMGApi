/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Vehicle;
import com.emiliohernandez.enlatadosapi.dto.VehicleDto;
import com.emiliohernandez.enlatadosapi.service.VehicleService;
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

/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    private VehicleService service = new VehicleService();
    private ObjectMapper om = new ObjectMapper();
    public HttpHeaders responseHeaders = new HttpHeaders();
    
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<Vehicle>> uploadVehicles(@RequestParam("file") MultipartFile file) throws IOException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        List<Vehicle> result = service.upload(file.getInputStream());
        Response resp = new Response();
        resp.setResult(result);
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Uploaded vehicles!");

        json = om.writeValueAsString(resp);
        return new ResponseEntity<>(
                service.getVehicles(),
                responseHeaders,
                HttpStatus.OK
        );
    }
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Vehicle>> getVehicles(HttpServletRequest req) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        Response resp = new Response();
        resp.setResult(service.getVehicles());
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Obtained vehicles!");

        json = om.writeValueAsString(resp);
        return new ResponseEntity<>(
                service.getVehicles(),
                responseHeaders,
                HttpStatus.OK
        );
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addVehicle(@RequestBody() VehicleDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(body.licensePlate);
            vehicle.setBrand(body.brand);
            vehicle.setColor(body.color);
            vehicle.setModel(body.model);
            vehicle.setYear(body.year);
            Response rsp = new Response();
            Vehicle creation = this.service.add(vehicle.getLicensePlate(), vehicle.getBrand(), vehicle.getModel(), vehicle.getColor(), vehicle.getYear());
            if (creation instanceof Vehicle) {
                rsp.setMessage("Vehículo agregado correctamente.");
                rsp.setResult(creation);
                rsp.setSuccess(Boolean.TRUE);
            } else {
                rsp.setMessage("El vehículo ya existe!");
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

}
