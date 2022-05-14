/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.controller;

import com.emiliohernandez.enlatadosapi.bean.Stock;
import com.emiliohernandez.enlatadosapi.dto.StockDto;
import com.emiliohernandez.enlatadosapi.service.StockService;
import com.emiliohernandez.enlatadosapi.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author emilio.hernandez
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    private StockService service = new StockService();
    private ObjectMapper om = new ObjectMapper();
    public HttpHeaders responseHeaders = new HttpHeaders();

    @RequestMapping(value = "/pop", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeStock() throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json");
        String json = "";
        Response rsp = new Response();
        Stock deletion = service.remove();
        if (deletion instanceof Stock) {
            rsp.setMessage("Stock removido!");
            rsp.setSuccess(Boolean.TRUE);
            rsp.setResult(deletion);
        } else {
            rsp.setMessage("Stock no removido!");
            rsp.setSuccess(Boolean.FALSE);
            rsp.setResult(null);
        }

        json = om.writeValueAsString(rsp);
        return new ResponseEntity<>(
                json,
                responseHeaders,
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addStock(@RequestBody() StockDto body) throws JsonProcessingException {
        responseHeaders.add("Content-Type", "application/json");
        String json = "";
        try {
            Response rsp = new Response();

            Stock stock = new Stock();
            stock.setEntryDate(body.entryDate);
            Stock result = service.add(stock);

            if (result instanceof Stock) {
                rsp.setMessage("Stock agregado!");
                rsp.setSuccess(Boolean.TRUE);
                rsp.setResult(result);
            } else {
                rsp.setMessage("Error al agregar el stock");
                rsp.setSuccess(Boolean.FALSE);
                rsp.setResult(null);
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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Stock>> getStock(HttpServletRequest req) throws JsonProcessingException {
        String json = "";
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(
                service.getAll(),
                responseHeaders,
                HttpStatus.OK
        );
    }
}
