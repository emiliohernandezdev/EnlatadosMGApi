/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.bean.User;
import com.emiliohernandez.enlatadosapi.bean.Vehicle;
import com.emiliohernandez.enlatadosapi.dto.UserDto;
import com.emiliohernandez.enlatadosapi.dto.VehicleDto;
import com.emiliohernandez.enlatadosapi.service.VehicleService;
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
    public ResponseEntity<String> uploadVehicles(@RequestParam("file") MultipartFile file) throws IOException {
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        String json = "";
        List<Vehicle> result = service.upload(file.getInputStream());
        Response resp = new Response();
        resp.setResult(result);
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Uploaded vehicles!");
        json = om.writeValueAsString(resp);


        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Vehicle>> getVehicles(HttpServletRequest req){
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        Response resp = new Response();
        resp.setResult(service.getVehicles());
        resp.setSuccess(Boolean.TRUE);
        resp.setMessage("Obtained vehicles!");

        return new ResponseEntity<>(
                service.getVehicles(),
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/{licensePlate}", method = RequestMethod.GET)
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String licensePlate) throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json");
        Vehicle find = service.exists(licensePlate);
        return new ResponseEntity<>(
                find,
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

    @RequestMapping(value = "/update/{licensePlate}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateVehicle(@PathVariable("licensePlate") String licensePlate, @RequestBody() VehicleDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        Response rsp = new Response();
            Vehicle find = service.exists(licensePlate);
            if(find != null){
                Vehicle update = new Vehicle();
                update.setLicensePlate(body.licensePlate);
                update.setBrand(body.brand);
                update.setColor(body.color);
                update.setYear(body.year);
                update.setModel(body.model);

                Vehicle result = service.update(licensePlate, update);

                if(result instanceof Vehicle){
                    rsp.setMessage("Vehículo actualizado!");
                    rsp.setResult(result);
                    rsp.setSuccess(Boolean.TRUE);
                }else{
                    rsp.setMessage("Error al actualizar el vehículo!");
                    rsp.setResult(null);
                    rsp.setSuccess(Boolean.TRUE);
                }
            }else{
                rsp.setMessage("Vehículo no encontrado.");
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

        String graph = service.getGraphviz();

        return new ResponseEntity<>(
                URLEncoder.encode(graph, StandardCharsets.UTF_8.toString()),
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/delete/{licensePlate}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("licensePlate") String licensePlate) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        service.delete(licensePlate);
        Response rsp = new Response();
        rsp.setMessage("Vehículo eliminado!");
        rsp.setResult(null);
        rsp.setSuccess(Boolean.TRUE);
        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

}
