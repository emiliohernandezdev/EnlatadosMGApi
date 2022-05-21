/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;

import com.emiliohernandez.enlatadosapi.bean.Client;
import com.emiliohernandez.enlatadosapi.bean.Dealer;
import com.emiliohernandez.enlatadosapi.bean.User;
import com.emiliohernandez.enlatadosapi.bean.Vehicle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author emilio.hernandez
 */
public class CsvHelper {

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
    


    public List<User> csvToUsers(InputStream is) throws IOException {
        List<User> users = new ArrayList<>();
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withAllowMissingColumnNames(true).withTrim(true).parse(fileReader);

            for(CSVRecord record: records){
                User usr = new User();
                String[] split = record.get(0).split(";");
                usr.setId(Integer.parseInt(split[0]));
                usr.setName(split[1]);
                usr.setSurname(split[2]);
                usr.setPassword(split[3]);
                users.add(usr);
            }
        } catch (IOException io) {
            System.out.println("Error al leer el archivo: " + io.getMessage());
        }
        return users;
    }
    
    public List<Dealer> csvToDealers(InputStream is) throws IOException {
        List<Dealer> dealers = new ArrayList<>();
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withAllowMissingColumnNames(true).withTrim(true).parse(fileReader);

            for(CSVRecord record: records){
                Dealer dealer = new Dealer();
                String[] split = record.get(0).split(";");
                dealer.setCui(split[0]);
                dealer.setName(split[1]);
                dealer.setSurname(split[2]);
                dealer.setLicense(split[3]);
                dealer.setPhone(split[4]);
                dealers.add(dealer);
            }
        } catch (IOException io) {
            System.out.println("Error al leer el archivo: " + io.getMessage());
        }
        return dealers;
    }
    
    public List<Client> csvToClients(InputStream is) throws IOException {
        List<Client> clients = new ArrayList<>();
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withAllowMissingColumnNames(true).withTrim(true).parse(fileReader);

            for(CSVRecord record: records){
                Client client = new Client();
                String[] split = record.get(0).split(";");
                client.setCui(split[0]);
                client.setName(split[1]);
                client.setSurname(split[2]);
                client.setPhone(split[3]);
                client.setAddress(split[4]);
                clients.add(client);
            }
        } catch (IOException io) {
            System.out.println("Error al leer el archivo: " + io.getMessage());
        }
        return clients;
    }
    
    public List<Vehicle> csvToVehicles(InputStream is) throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withAllowMissingColumnNames(true).withTrim(true).parse(fileReader);

            for(CSVRecord record: records){
                Vehicle vh = new Vehicle();
                String[] split = record.get(0).split(";");
                vh.setLicensePlate(split[0]);
                vh.setBrand(split[1]);
                vh.setModel(split[2]);
                vh.setColor(split[3]);
                vh.setYear(Integer.parseInt(split[4]));
                vehicles.add(vh);
            }
        } catch (IOException io) {
            System.out.println("Error al leer el archivo: " + io.getMessage());
        }
        return vehicles;
    }

}
