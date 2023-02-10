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
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static model.Appointment.ConvertToLocalDateTime;

/**
 * Data model for contacts.
 * @author Evan
 */
public class Contact {
    private int contactID;
    private String contactName;
    private String contactEmail;
    
    public static ObservableList<Contact> getAllContacts () throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        Contact newContact = new Contact(-1, null, null);
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            newContact.contactID = rs.getInt("Contact_ID");
            newContact.contactName = rs.getString("Contact_Name");
            newContact.contactEmail = rs.getString("Email");
            allContacts.add(newContact);
        }
        return allContacts;
    }
    
    /**
     * Creates an observablelist of strings of all the contact names
     * @return
     * @throws SQLException 
     */
    public static ObservableList<String> getAllContactNames () throws SQLException {
        ObservableList<String> allContactNames = FXCollections.observableArrayList();
        String sql = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            allContactNames.add(rs.getString("Contact_Name"));
        }
        return allContactNames;
    }
    
    /**
     * Gets a contact with an ID ID from the database
     * @param ID
     * @return
     * @throws SQLException 
     */
    public static Contact getContact(int ID) throws SQLException {
        Contact newContact = new Contact(-1, null, null);
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            newContact.contactID = rs.getInt("Contact_ID");
            newContact.contactName = rs.getString("Contact_Name");
            newContact.contactEmail = rs.getString("Email");
        }
        return newContact;
    }
    
    /**
     * Gets a contact with a name name from the database
     * @param name
     * @return
     * @throws SQLException 
     */
    public static Contact getContact(String name) throws SQLException {
        Contact newContact = new Contact(-1, null, null);
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            newContact.contactID = rs.getInt("Contact_ID");
            newContact.contactName = rs.getString("Contact_Name");
            newContact.contactEmail = rs.getString("Email");
        }
        return newContact;
    }
    
    /**
     * Gets all the appointments from the database belonging to a contact with a name name
     * @param name
     * @return
     * @throws SQLException 
     */
    public static ArrayList<Appointment> getAppointments (String name) throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, getContact(name).getContactID());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int newID = rs.getInt("Appointment_ID");
            String newTitle = rs.getString("Title");
            String newDescription = rs.getString("Description");
            String newLocation = rs.getString("Location");
            String newType = rs.getString("Type");
            LocalDateTime newStart = ConvertToLocalDateTime(rs.getString("Start"));
            LocalDateTime newEnd = ConvertToLocalDateTime(rs.getString("End"));
            LocalDateTime newCreateDate = ConvertToLocalDateTime(rs.getString("Create_Date"));
            String newCreatedBy = rs.getString("Created_By");
            LocalDateTime newLastUpdate = ConvertToLocalDateTime(rs.getString("Last_Update"));
            String newLastUpdatedBy = rs.getString("Last_Updated_By");
            int newCustomerID = rs.getInt("Customer_ID");
            int newUserID = rs.getInt("User_ID");
            int newContactID = rs.getInt("Contact_ID");
            String newContactName = Contact.getContact(newContactID).getContactName();
            
            Appointment newAppointment = new Appointment(newID, newTitle, 
                    newDescription, newLocation, newType, newStart, newEnd, 
                    newCreateDate, newCreatedBy, newLastUpdate, 
                    newLastUpdatedBy, newCustomerID, newUserID, newContactID, newContactName);
            appointments.add(newAppointment);
        }
        return appointments;
    }

    /**
     * Constructor for contacts
     * @param contactID
     * @param contactName
     * @param contactEmail 
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    
    /**
     * Getter for contactID
     * @return 
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Setter for contactID
     * @param contactID 
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Getter for contactName
     * @return 
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Setter for contactName
     * @param contactName 
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Getter for contactEmail
     * @return 
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Setter for contactEmail
     * @param contactEmail 
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}