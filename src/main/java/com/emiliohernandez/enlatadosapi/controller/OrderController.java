/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;
import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.bean.Order;
import com.emiliohernandez.enlatadosapi.dto.OrderDto;
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

import java.util.ArrayList;

/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService service = new OrderService();
    public HttpHeaders responseHeaders = new HttpHeaders();


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
            order.setNumber("#-" + service.hexEncode(String.valueOf(System.identityHashCode(order)).getBytes()));
            order.setOrigin(body.origin);
            order.setDestination(body.destination);
            order.setStatus(body.status);
            order.setDate(body.date);


            json = om.writeValueAsString(order);

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