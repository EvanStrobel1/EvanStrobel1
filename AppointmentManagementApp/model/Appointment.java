package model;

import helper.JDBC;
import helper.LambdaInterface;
import helper.Localization;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Data model for appointments.
 * Utilizes a lambda expression for determining the duration in time between two timestamps.
 * @author Evan
 */
public class Appointment {
    static ResourceBundle rb;
    
    private int ID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    //private ZonedDateTime startZDT = ZonedDateTime.of(startDateTime, ZoneId.systemDefault());
    private LocalDateTime endDateTime;
    //private ZonedDateTime endZDT = ZonedDateTime.of(endDateTime, ZoneId.systemDefault());
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    
    public static ObservableList<Integer> hours = FXCollections.observableArrayList(
            0, 1, 2, 3, 4, 5, 6, 
            7, 8, 9, 10, 11, 12, 
            13, 14, 15, 16, 17, 18, 
            19, 20, 21, 22, 23);
    public static ObservableList<Integer> minutes = FXCollections.observableArrayList(0, 15, 30, 45);
    
    static LambdaInterface lambdaDuration = (time1, time2) -> Duration.between(time1, time2);
    
    /**
     * Constructor method for Appointments
     * @param newID
     * @param newTitle
     * @param newDescription
     * @param newLocation
     * @param newType
     * @param newStart
     * @param newEnd
     * @param newCreateDate
     * @param newCreatedBy
     * @param newLastUpdate
     * @param newLastUpdatedBy
     * @param newCustomerID
     * @param newUserID
     * @param newContactID
     * @param newContactName 
     */
    public Appointment(int newID, String newTitle, String newDescription, 
            String newLocation, String newType, LocalDateTime newStart, LocalDateTime newEnd, 
            LocalDateTime newCreateDate, String newCreatedBy, LocalDateTime newLastUpdate, 
            String newLastUpdatedBy, int newCustomerID, int newUserID, int newContactID,
            String newContactName) {
        this.ID = newID;
        this.title = newTitle;
        this.description = newDescription;
        this.location = newLocation;
        this.type = newType;
        this.startDateTime = newStart;
        this.endDateTime = newEnd;
        this.createDate = newCreateDate;
        this.createdBy = newCreatedBy;
        this.lastUpdate = newLastUpdate;
        this.lastUpdatedBy = newLastUpdatedBy;
        this.customerID = newCustomerID;
        this.userID = newUserID;
        this.contactID = newContactID;
        this.contactName = newContactName;
    }
    
    /**
     * Creates an observable list of all the appointments in the database
     * @return
     * @throws SQLException 
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        allAppointments.clear();
        
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
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
            
            // Convert LocalDateTimes from UTC to the local date time (they are stored as UTC in the database)
            newStart = ConvertToTimeZone(ZonedDateTime.of(newStart, ZoneId.of("UTC")), ZoneId.systemDefault());
            newEnd = ConvertToTimeZone(ZonedDateTime.of(newEnd, ZoneId.of("UTC")), ZoneId.systemDefault());
            newCreateDate = ConvertToTimeZone(ZonedDateTime.of(newCreateDate, ZoneId.of("UTC")), ZoneId.systemDefault());
            newLastUpdate = ConvertToTimeZone(ZonedDateTime.of(newLastUpdate, ZoneId.of("UTC")), ZoneId.systemDefault());
            
            Appointment newAppointment = new Appointment(newID, newTitle, 
                    newDescription, newLocation, newType, newStart, newEnd, 
                    newCreateDate, newCreatedBy, newLastUpdate, 
                    newLastUpdatedBy, newCustomerID, newUserID, newContactID, newContactName);
            allAppointments.add(newAppointment);
        }
        return allAppointments;
    }
    
    /**
     * Filters the allAppointments list to only include appointments within the next month.
     * @return
     * @throws SQLException 
     */
    public static ObservableList<Appointment> getMonthAppointments() throws SQLException {
        
        Duration duration;
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
        allAppointments = getAllAppointments();
        
        for (Appointment appt : allAppointments) {
            duration = lambdaDuration.getDuration(LocalDateTime.now(), appt.startDateTime);
            //duration = Duration.between(LocalDateTime.now(), appt.startDateTime);
            //Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(newStart));
            if (duration.toDays() <= 30) {
                monthAppointments.add(appt);
            }
        }
            
        return monthAppointments;
    }
    
    /**
     * Filters the allAppointments list to only include appointments within the next week.
     * @return
     * @throws SQLException 
     */
    public static ObservableList<Appointment> getWeekAppointments() throws SQLException {
        
        Duration duration;
        ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
        allAppointments = getAllAppointments();
        
        for (Appointment appt : allAppointments) {
            duration = lambdaDuration.getDuration(LocalDateTime.now(), appt.startDateTime);
            //duration = Duration.between(LocalDateTime.now(), appt.startDateTime);
            //Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.parse(newStart));
            if (duration.toDays() <= 7) {
                weekAppointments.add(appt);
            }
        }
            
        return weekAppointments;
    }
    
    /**
     * Shows alerts for appointments within 15 minutes for a certain user. Ran upon login.
     * @param userID
     * @throws SQLException 
     */
    public static void showAppointmentsOfUserWithin15 (int userID) throws SQLException {
        
        int apptCount = 0;
        
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ZonedDateTime startZDT = ZonedDateTime.of(ConvertToLocalDateTime(rs.getString("Start")), ZoneId.of("UTC"));
            LocalDateTime appointmentTime = ConvertToTimeZone(startZDT, ZoneId.systemDefault());
            Duration timeToAppointment = Duration.between(LocalDateTime.now(), appointmentTime);
            if (timeToAppointment.toMinutes() <= 15 && timeToAppointment.toMinutes() >= 0) {
                apptCount++;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Alert");
                alert.setContentText("Upcoming Appointment: ID " + 
                        rs.getInt("Appointment_ID") + 
                        " Date: " + appointmentTime.toLocalDate() + 
                        " Time: " + appointmentTime.toLocalTime());
                alert.showAndWait();
            }
        }
        if (apptCount == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (Locale.getDefault().getLanguage().equals("fr")) {
                rb = Localization.getResourceBundle();
                alert.setTitle(rb.getString("Appointment Alert"));
                alert.setContentText(rb.getString("No upcoming appointments within 15 minutes."));
            }
            else {
                alert.setTitle("Appointment Alert");
                alert.setContentText("No upcoming appointments within 15 minutes.");
            }
            alert.showAndWait();
        }
    }
    
    /**
     * Deletes an appointment with an ID of appID from the database
     * @param appID
     * @throws SQLException 
     */
    public static void DeleteAppointment(int appID) throws SQLException {
        for (Appointment app : allAppointments) {
            if (app.ID == appID) {
                allAppointments.remove(app);
                
                String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, appID);
                if (ps.executeUpdate() > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setContentText("Appointment successfully deleted | ID: " + app.getID() + " | Type: " + app.getType());
                    alert.show();
                }
                break;
            }
        }
        
        
    }
    
    /**
     * Generates a unique ID for an appointment.
     * @return
     * @throws SQLException 
     */
    public static Integer generateID() throws SQLException {
        int newID = 1;
        allAppointments = getAllAppointments();
        for (Appointment appointment : allAppointments) {
            if (newID == appointment.ID) {
                newID++;
            }
            else {
                return newID;
            }
        }
        return newID;
    }
    
    /**
     * Inserts a new appointment into the database
     * @param appointment
     * @throws SQLException 
     */
    public static void Insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointment.ID);
        ps.setString(2, appointment.title);
        ps.setString(3, appointment.description);
        ps.setString(4, appointment.location);
        ps.setString(5, appointment.type);
        ZonedDateTime newStartZDT = ZonedDateTime.of(appointment.startDateTime, ZoneId.systemDefault());
        ps.setString(6, ConvertLDTToString(ConvertToTimeZone(newStartZDT, ZoneId.of("UTC"))));
        ZonedDateTime newEndZDT = ZonedDateTime.of(appointment.endDateTime, ZoneId.systemDefault());
        ps.setString(7, ConvertLDTToString(ConvertToTimeZone(newEndZDT, ZoneId.of("UTC"))));
        ZonedDateTime newCreateDateZDT = ZonedDateTime.of(appointment.createDate, ZoneId.systemDefault());
        ps.setString(8, ConvertLDTToString(ConvertToTimeZone(newCreateDateZDT, ZoneId.of("UTC"))));
        ps.setString(9, appointment.createdBy);
        ZonedDateTime newLastUpdateZDT = ZonedDateTime.of(appointment.lastUpdate, ZoneId.systemDefault());
        ps.setString(10, ConvertLDTToString(ConvertToTimeZone(newLastUpdateZDT, ZoneId.of("UTC"))));
        ps.setString(11, appointment.lastUpdatedBy);
        ps.setInt(12, appointment.customerID);
        ps.setInt(13, appointment.userID);
        ps.setInt(14, appointment.contactID);
        ps.executeUpdate();
        allAppointments.add(appointment);
    }
    
    /**
     * Updates an appointment of ID oldAppointmentID to a new appointment newAppt
     * @param oldAppointmentID
     * @param newAppt
     * @throws SQLException 
     */
    public static void Update(int oldAppointmentID, Appointment newAppt) throws SQLException {
        String sql = "UPDATE appointments SET Appointment_ID = ?, Title = ?, "
                + "Description = ?, Location = ?, Type = ?, Start = ?, End = ?, "
                + "Create_Date = ?, Created_By = ?, Last_Update = ?, "
                + "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, "
                + "Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, newAppt.ID);
        ps.setString(2, newAppt.title);
        ps.setString(3, newAppt.description);
        ps.setString(4, newAppt.location);
        ps.setString(5, newAppt.type);
        ZonedDateTime newStartZDT = ZonedDateTime.of(newAppt.startDateTime, ZoneId.systemDefault());
        ps.setString(6, ConvertLDTToString(ConvertToTimeZone(newStartZDT, ZoneId.of("UTC"))));
        ZonedDateTime newEndZDT = ZonedDateTime.of(newAppt.endDateTime, ZoneId.systemDefault());
        ps.setString(7, ConvertLDTToString(ConvertToTimeZone(newEndZDT, ZoneId.of("UTC"))));
        ps.setString(8, ConvertLDTToString(newAppt.createDate));
        ps.setString(9, newAppt.createdBy);
        ZonedDateTime newLastUpdateZDT = ZonedDateTime.of(newAppt.lastUpdate, ZoneId.systemDefault());
        ps.setString(10, ConvertLDTToString(ConvertToTimeZone(newLastUpdateZDT, ZoneId.of("UTC"))));
        ps.setString(11, newAppt.lastUpdatedBy);
        ps.setInt(12, newAppt.customerID);
        ps.setInt(13, newAppt.userID);
        ps.setInt(14, newAppt.contactID);
        ps.setInt(15, oldAppointmentID);
        ps.executeUpdate();
    }
    
    /**
     * Checks for overlap between two LocalDateTime variables.
     * @param start
     * @param end
     * @param customerID
     * @return
     * @throws SQLException 
     */
    public static boolean CheckForOverlap (LocalDateTime start, LocalDateTime end, int customerID, int apptID) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getInt("Appointment_ID") != apptID) {
                LocalDateTime start2 = LocalDateTime.parse(rs.getString("Start"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ZonedDateTime start2ZDT = ZonedDateTime.of(start2, ZoneId.of("UTC"));
                LocalDateTime end2 = LocalDateTime.parse(rs.getString("End"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ZonedDateTime end2ZDT = ZonedDateTime.of(end2, ZoneId.of("UTC"));
                start2 = ConvertToTimeZone(start2ZDT, ZoneId.systemDefault());
                end2 = ConvertToTimeZone(end2ZDT, ZoneId.systemDefault());
                if (start.isBefore(end2) && start2.isBefore(end)) {
                    return true;
                }
                if (start.isEqual(end) || start.isEqual(end2) || start2.isEqual(end) || start2.isEqual(end2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Converts a formatted string into a LocalDateTime.
     * @param string
     * @return 
     */
    public static LocalDateTime ConvertToLocalDateTime (String string) {
        LocalDateTime newLDT = LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return newLDT;
    }
    
    /**
     * Converts a ZonedDateTime variable to a specified timezone
     * @param zdt
     * @param newTimezone
     * @return 
     */
    public static LocalDateTime ConvertToTimeZone (ZonedDateTime zdt, ZoneId newTimezone) {
        ZonedDateTime newZdt = ZonedDateTime.ofInstant(zdt.toInstant(), newTimezone);
        return newZdt.toLocalDateTime();
    }
    
    /**
     * Converts a LocalDateTime variable to a formatted string for entering into the database.
     * @param dateTime
     * @return 
     */
    public static String ConvertLDTToString (LocalDateTime dateTime) {
        String ldtString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return ldtString;
    }
    
    /**
     * getter for ID
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
     * Getter for title
     * @return 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description
     * @return 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for location
     * @return 
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location
     * @param location 
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for type
     * @return 
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type
     * @param type 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start date time as a string
     * @return 
     */
    public String getStartDateTime() {
        return ConvertLDTToString(startDateTime);
    }

    /**
     * Setter for startdatetime
     * @param startDateTime 
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    /**
     * Gets the start date as a string
     * @return 
     */
    public String getStartDate() {
        return startDateTime.toLocalDate().toString();
    }
    
    /**
     * Gets the start time as a string
     * @return 
     */
    public String getStartTime() {
        return startDateTime.toLocalTime().toString();
    }

    /**
     * Gets the enddatetime as a string
     * @return 
     */
    public String getEndDateTime() {
        return ConvertLDTToString(endDateTime);
    }

    /**
     * setter for enddatetime
     * @param endDateTime 
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    
    /**
     * Gets the end date as a string
     * @return 
     */
    public String getEndDate() {
        return endDateTime.toLocalDate().toString();
    }
    
    /**
     * Gets the end time as a string
     * @return 
     */
    public String getEndTime() {
        return endDateTime.toLocalTime().toString();
    }

    /**
     * gets the create date as a string
     * @return 
     */
    public String getCreateDate() {
        return ConvertLDTToString(createDate);
    }

    /**
     * Setter for createdate
     * @param createDate 
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter for createdby
     * @return 
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for createadby
     * @param createdBy 
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets lastupdate as a string
     * @return 
     */
    public String getLastUpdate() {
        return ConvertLDTToString(lastUpdate);
    }

    /**
     * Setter for lastupdate
     * @param lastUpdate 
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for lastupdatedby
     * @return 
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Setter for lastupdatedby
     * @param lastUpdatedBy 
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for customerID
     * @return 
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Setter for customerID
     * @param customerID 
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Getter for userID
     * @return 
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Setter for userID
     * @param userID 
     */
    public void setUserID(int userID) {
        this.userID = userID;
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
}