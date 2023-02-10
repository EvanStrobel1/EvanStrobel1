/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import helper.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Data model for countries.
 * @author Evan
 */
public class Countries {
    public static ObservableList<String> countries = FXCollections.observableArrayList();
    public static ObservableList<String> divisions = FXCollections.observableArrayList();
    
    /**
     * Gets all the countries from the database
     * @return
     * @throws SQLException 
     */
    public static ObservableList<String> getCountries () throws SQLException {
        countries.clear();
        String sql = "SELECT Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countries.add(rs.getString("Country"));
        }
        return countries;
    }
    
    /**
     * Gets a country with a certain division ID
     * @param divisionID
     * @return
     * @throws SQLException 
     */
    public static String getCountry (int divisionID) throws SQLException {
        int countryID = -1;
        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countryID = rs.getInt("Country_ID");
        }
        
        sql = "SELECT Country FROM countries WHERE Country_ID = ?";
        ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getString("Country");
        }
        return "Not found!";
    }
    
    /**
     * Gets a country's ID based off its name
     * @param countryName
     * @return
     * @throws SQLException 
     */
    public static Integer getCountryID (String countryName) throws SQLException {
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("Country_ID");
        }
        return -1;
    }
    
    /**
     * Gets the divisions of a certain country
     * @param countryID
     * @return
     * @throws SQLException 
     */
    public static ObservableList<String> getDivisions (int countryID) throws SQLException {
        divisions.clear();
        String sql = "SELECT Division FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            divisions.add(rs.getString("Division"));
        }
        return divisions;
    }
    
    /**
     * Gets a division's ID based on its name
     * @param divisionName
     * @return
     * @throws SQLException 
     */
    public static Integer getDivisionID (String divisionName) throws SQLException {
        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, divisionName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("Division_ID");
        }
        return -1;
    }
    
    /**
     * Gets a division's name based on its ID
     * @param divisionID
     * @return
     * @throws SQLException 
     */
    public static String getDivision (int divisionID) throws SQLException {
        String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getString("Division");
        }
        return "NOT FOUND!";
    }
}
