package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.example.handlers.VehicleHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Gson gson = new Gson();
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/createVehicle", new VehicleHandler());
        server.createContext("/getVehicles", new VehicleHandler());
        server.createContext("/getVehicle", new VehicleHandler());
        server.createContext("/updateVehicle", new VehicleHandler());
        server.createContext("/deleteVehicle", new VehicleHandler());
        server.setExecutor(null);
        server.start();
    }
    public static Connection connect(){
        Connection con = null;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/autogarazas","root", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }
}