package org.example.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.Vehicle;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Locale.filter;
import static org.example.Main.*;
import static org.example.models.Vehicle.*;


public class VehicleHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        handleCORS(exchange);
        if (path.equals(("/getVehicles")) && method.equals("GET")){
            handleGetVehicles(exchange);
        }
        if (path.equals(("/getVehicle")) && method.equals("GET")){
            handleGetVehicles(exchange);
        }
    }
    private void handleGetVehicles(HttpExchange exchange)  {
        System.out.println("handleGetVehicles");
        ArrayList<Vehicle> vhs = selectAll();
        try {

        String response = gson.toJson(vhs);
//        response = "{\"miau\":\"true\"}";
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void handleGetVehicle(HttpExchange exchange) throws IOException {
        //something something tadaa! (getQuery)
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = queryToMap(query);

            long id = Long.parseLong(params.get("id"));
            Vehicle vehicle = getOne(id);

            String response = gson.toJson(vehicle);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
        String response = gson.toJson(getOne(/*tas id kuri istraukei is getQuery*/ 7 ));
        System.out.println(response);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Vehicle requestVacation(HttpExchange exchange)  {
        Vehicle vehicle = new Vehicle();
        try {
            InputStream requestBody = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            String dataString = "";
            String line;
            while ((line = reader.readLine()) != null) {
                dataString += line;
            }
            reader.close();
            System.out.println(dataString);
            vehicle = gson.fromJson(dataString, Vehicle.class);
        }catch (Exception e){
            System.out.println(e);
        }
        return vehicle;
    }

    private void handleDeleteVacation(HttpExchange exchange) throws IOException {
        Vehicle vacationToDelete = requestVacation(exchange);
    //    boolean removed = handleGetVehicles().removeIf(v -> v.getId() == ToDelete.getId());
        try {
         //   if (removed) {
                //saveVacations();
                String response = "Vacation has been deleted successfully";
                System.out.println(response);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
         //   }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleCORS(HttpExchange exchange) {
        // Allow requests from all origins
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        // Allow specific methods
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        // Allow specific headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
        // Allow credentials, if needed
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
    }
    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
            } else {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), "");
            }
        }
        return result;
    }
}
