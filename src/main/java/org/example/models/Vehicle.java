package org.example.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.example.Main.connect;

public class Vehicle {
    private long id;
    private String manufacturer;
    private int releaseYear;
    private String model;

    public Vehicle(long id, String manufacturer, int releaseYear, String model) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.releaseYear = releaseYear;
        this.model = model;
    }

    public static ArrayList<Vehicle> selectAll() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try {
            Connection con = connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Vehicle vh = new Vehicle(
                        rs.getLong("id"),
                        rs.getString("manufacturer"),
                        rs.getInt("release_year"),
                        rs.getString("model"));
                vehicles.add(vh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", releaseYear=" + releaseYear +
                ", model='" + model + '\'' +
                '}';
    }
}

