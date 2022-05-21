/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;
import com.emiliohernandez.enlatadosapi.bean.*;
import com.emiliohernandez.enlatadosapi.dto.OrderDto;
import com.emiliohernandez.enlatadosapi.dto.VehicleDto;
import com.emiliohernandez.enlatadosapi.service.ClientService;
import com.emiliohernandez.enlatadosapi.service.DealerService;
import com.emiliohernandez.enlatadosapi.service.OrderService;
import com.emiliohernandez.enlatadosapi.service.VehicleService;
import com.emiliohernandez.enlatadosapi.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService service =  OrderService.getInstance();

    private ClientService clientService = ClientService.getInstance();

    private VehicleService vehicleService = VehicleService.getInstance();

    private DealerService dealerService = DealerService.getInstance();
    public HttpHeaders responseHeaders = new HttpHeaders();

    private ObjectMapper om = new ObjectMapper();

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Order>> getOrders() throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(
                service.getOrders(),
                responseHeaders,
                HttpStatus.OK
        );
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addOrder(@RequestBody() OrderDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        try {
            Order order = new Order();
            Response rsp = new Response();
            order.setNumber("#-" + service.hexEncode(String.valueOf(System.identityHashCode(order)).getBytes()));
            order.setOrigin(body.origin);
            order.setDestination(body.destination);
            order.setStatus(body.status);
            order.setDate(body.date);
            order.setBoxes(body.boxes);
            Client find = clientService.find(Long.parseLong(body.client));
            order.setClient(find);

            Vehicle vehicleFind = vehicleService.exists(body.vehicle);
            order.setVehicle(vehicleFind);

            Dealer dealerFind = dealerService.exists(body.dealer);
            order.setDealer(dealerFind);


            //apartado que remueve de la cola, hasta que estén disponibles
            vehicleService.delete(vehicleFind.getLicensePlate());
            dealerService.delete(dealerFind.getCui());
            Order addition = service.add(order);
            if(addition instanceof Order){
                rsp.setSuccess(Boolean.TRUE);
                rsp.setMessage("Pedido: " + order.getNumber() + " agregado!");
                rsp.setResult(order);
            }else{
                rsp.setSuccess(Boolean.FALSE);
                rsp.setMessage("Error al agregar el pedido");
                rsp.setResult(null);
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

    @RequestMapping(value = "/update/{number}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateStatus(@PathVariable("number") String number, @RequestBody() OrderDto body) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json");
        Response rsp = new Response();
        number = "#-"+number;
        System.out.println(number);
        Order find = service.exists(number);
        if(find != null){
            Order update = new Order();
            update.setStatus(body.status);
            update.setNumber(find.getNumber());
            update.setStock(find.getStock());
            update.setBoxes(find.getBoxes());
            update.setDealer(find.getDealer());
            update.setClient(find.getClient());
            update.setVehicle(find.getVehicle());
            update.setDate(find.getDate());
            update.setOrigin(find.getOrigin());
            update.setDestination(find.getDestination());
            Order result = service.update(number, update);
            

            if(result instanceof Order){
                rsp.setMessage("Estado actualizado!");
                rsp.setResult(result);
                rsp.setSuccess(Boolean.TRUE);
            }else{
                rsp.setMessage("Error al actualizar el estado.");
                rsp.setResult(null);
                rsp.setSuccess(Boolean.TRUE);
            }
        }else{
            rsp.setMessage("Orden no encontrada.");
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
        System.out.println(graph);
        return new ResponseEntity<>(
                URLEncoder.encode(graph, StandardCharsets.UTF_8.toString()),
                responseHeaders,
                HttpStatus.OK
        );
    }
    
}