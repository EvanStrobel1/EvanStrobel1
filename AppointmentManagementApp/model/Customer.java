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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * Data model for customers.
 * @author Evan
 */
public class Customer {
    private int ID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Constructor for customers
     * @param ID
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionID 
     */
    public Customer(int ID, String name, String address, String postalCode, 
            String phoneNumber, LocalDateTime createDate, String createdBy, 
            LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }
    
    /**
     * Gets all customers from the database
     * @return
     * @throws SQLException 
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        allCustomers.clear();
        
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int newID = rs.getInt("Customer_ID");
            String newName = rs.getString("Customer_Name");
            String newAddress = rs.getString("Address");
            String newPostalCode = rs.getString("Postal_Code");
            String newPhoneNumber = rs.getString("Phone");
            LocalDateTime newCreateDate = Appointment.ConvertToLocalDateTime(rs.getString("Create_Date"));
            String newCreatedBy = rs.getString("Created_By");
            LocalDateTime newLastUpdate = Appointment.ConvertToLocalDateTime(rs.getString("Last_Update"));
            String newLastUpdatedBy = rs.getString("Last_Updated_By");
            int newDivisionID = rs.getInt("Division_ID");
            
            Customer newCustomer = new Customer(newID, newName, newAddress, 
                    newPostalCode, newPhoneNumber, newCreateDate, newCreatedBy, 
                    newLastUpdate, newLastUpdatedBy, newDivisionID);
            allCustomers.add(newCustomer);
        }
        return allCustomers;
    }
    
    /**
     * Generates a unique ID for a customer
     * @return 
     */
    public static Integer generateID() {
        int newID = 1;
        
        for (Customer customer : allCustomers) {
            if (newID == customer.ID) {
                newID++;
            }
            else {
                return newID;
            }
        }
        return newID;
    }
    
    /**
     * Inserts a customer into the database
     * @param customer
     * @throws SQLException 
     */
    public static void Insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.ID);
        ps.setString(2, customer.name);
        ps.setString(3, customer.address);
        ps.setString(4, customer.postalCode);
        ps.setString(5, customer.phoneNumber);
        ZonedDateTime createZDT = ZonedDateTime.of(customer.createDate, ZoneId.systemDefault());
        ps.setString(6, Appointment.ConvertLDTToString(Appointment.ConvertToTimeZone(createZDT, ZoneId.of("UTC"))));
        ps.setString(7, customer.createdBy);
        ZonedDateTime lastUpdateZDT = ZonedDateTime.of(customer.lastUpdate, ZoneId.systemDefault());
        ps.setString(8, Appointment.ConvertLDTToString(Appointment.ConvertToTimeZone(lastUpdateZDT, ZoneId.of("UTC"))));
        ps.setString(9, customer.lastUpdatedBy);
        ps.setInt(10, customer.divisionID);
        ps.executeUpdate();
        allCustomers.add(customer);
    }
    
    /**
     * Updates a customer in the database
     * @param oldCustomerID
     * @param newCust
     * @throws SQLException 
     */
    public static void Update(int oldCustomerID, Customer newCust) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, "
                + "Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, "
                + "Last_Update = ?, Last_Updated_By= ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, newCust.name);
        ps.setString(2, newCust.address);
        ps.setString(3, newCust.postalCode);
        ps.setString(4, newCust.phoneNumber);
        ZonedDateTime createZDT = ZonedDateTime.of(newCust.createDate, ZoneId.systemDefault());
        ps.setString(5, Appointment.ConvertLDTToString(Appointment.ConvertToTimeZone(createZDT, ZoneId.of("UTC"))));
        ps.setString(6, newCust.createdBy);
        ps.setString(7, Appointment.ConvertToTimeZone(ZonedDateTime.now(), ZoneId.of("UTC")).toString());
        ps.setString(8, User.getCurrentUserName());
        ps.setInt(9, newCust.divisionID);
        ps.setInt(10, oldCustomerID);
        ps.executeUpdate();
    }
    
    /**
     * Deletes a customer with ID customerID from the database
     * @param customerID
     * @throws SQLException 
     */
    public static void DeleteCustomer(int customerID) throws SQLException {
        for (Customer customer : allCustomers) {
            if (customer.ID == customerID) {
                allCustomers.remove(customer);
                break;
            }
        }
        
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        if (ps.executeUpdate() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setContentText("Customer successfully deleted");
            alert.show();
        }
    }
    
    /**
     * Getter for ID
     * @return 
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Setter for ID
     * @param ID 
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Getter for name
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for address
     * @return 
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for address
     * @param address 
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for postalCode
     * @return 
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Setter for postalCode
     * @param postalCode 
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Getter for phoneNumber
     * @return 
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for phoneNumber
     * @param phoneNumber 
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Getter for createDate
     * @return 
     */
    public String getCreateDate() {
        ZonedDateTime createZDT = ZonedDateTime.of(createDate, ZoneId.of("UTC"));
        return Appointment.ConvertLDTToString(Appointment.ConvertToTimeZone(createZDT, ZoneId.systemDefault()));
    }

    /**
     * Setter for createDate
     * @param createDate 
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter for createdBy
     * @return 
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for createdBy
     * @param createdBy 
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for lastUpdate
     * @return 
     */
    public String getLastUpdate() {
        ZonedDateTime lastUpdateZDT = ZonedDateTime.of(lastUpdate, ZoneId.of("UTC"));
        return Appointment.ConvertLDTToString(Appointment.ConvertToTimeZone(lastUpdateZDT, ZoneId.systemDefault()));
    }

    /**
     * Setter for lastUpdate
     * @param lastUpdate 
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for lastUpdatedBy
     * @return 
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Setter for lastUpdatedBy
     * @param lastUpdatedBy 
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for divisionID
     * @return 
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Setter for divisionID
     * @param divisionID 
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
}