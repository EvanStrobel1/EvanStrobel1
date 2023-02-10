package controller;

import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

/**
 * Controller for the Appointment Form.
 * @author Evan
 */
public class AppointmentFormController implements Initializable{

    Stage stage;
    Parent scene;
    ResourceBundle rb;
    boolean inFrench = false;
    
    
    @FXML
    private Label labelAppointments;
    
    @FXML
    private Button buttonAddAppointment;

    @FXML
    private Button buttonDeleteAppointment;

    @FXML
    private Button buttonUpdateAppointment;
    
    @FXML
    private Button buttonCustomers;

    @FXML
    private Button btnReport;
    
    @FXML
    private Button buttonSchedule;
    
    @FXML
    private ComboBox<String> comboSelectContact;
    
    @FXML
    private RadioButton radioMonthly;

    @FXML
    private RadioButton radioWeekly;
    
    @FXML
    private TableColumn<Appointment, Integer> tablecolumnAppointmentID;

    @FXML
    private TableColumn<Appointment, String> tablecolumnContact;

    @FXML
    private TableColumn<Appointment, Integer> tablecolumnCustomerID;

    @FXML
    private TableColumn<Appointment, String> tablecolumnDescription;

    @FXML
    private TableColumn<Appointment, String> tablecolumnEndDateTime;

    @FXML
    private TableColumn<Appointment, String> tablecolumnLocation;

    @FXML
    private TableColumn<Appointment, String> tablecolumnStartDateTime;

    @FXML
    private TableColumn<Appointment, String> tablecolumnTitle;

    @FXML
    private TableColumn<Appointment, String> tablecolumnType;

    @FXML
    private TableColumn<Appointment, Integer> tablecolumnUserID;

    @FXML
    private TableView<Appointment> tableviewAppointments;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tableviewAppointments.setItems(Appointment.getMonthAppointments());
            tablecolumnAppointmentID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            tablecolumnContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            tablecolumnCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            tablecolumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tablecolumnEndDateTime.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
            tablecolumnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            tablecolumnStartDateTime.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
            tablecolumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            tablecolumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
            tablecolumnUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            comboSelectContact.setItems(Contact.getAllContactNames());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            inFrench = true;
        }
        if (inFrench) {
            rb = Localization.getResourceBundle();
            labelAppointments.setText(rb.getString("Appointment"));
            buttonCustomers.setText(rb.getString("Customers") + " >");
            tablecolumnAppointmentID.setText(rb.getString("Appointment") + " ID");
            tablecolumnTitle.setText(rb.getString("Title"));
            tablecolumnDescription.setText(rb.getString("Description"));
            tablecolumnLocation.setText(rb.getString("Location"));
            tablecolumnContact.setText(rb.getString("Contact"));
            tablecolumnType.setText(rb.getString("Type"));
            tablecolumnStartDateTime.setText(rb.getString("Start Date/Time"));
            tablecolumnEndDateTime.setText(rb.getString("End Date/Time"));
            tablecolumnCustomerID.setText(rb.getString("Customer") + " ID");
            tablecolumnUserID.setText(rb.getString("User") + " ID");
            radioMonthly.setText(rb.getString("Monthly"));
            radioWeekly.setText(rb.getString("Weekly"));
            buttonAddAppointment.setText(rb.getString("Add"));
            buttonUpdateAppointment.setText(rb.getString("Update"));
            buttonDeleteAppointment.setText(rb.getString("Delete"));
            btnReport.setText(rb.getString("Report"));
            buttonSchedule.setText(rb.getString("View Schedule"));
            comboSelectContact.setPromptText(rb.getString("Select Contact..."));
        }
    }
    
    /**
     * Switches to the Add Appointment form.
     * @param event
     * @throws IOException 
     */
    @FXML
    void onAction_buttonAddAppointment(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Deletes a selected appointment
     * Shows an error if the user does not have an appointment selected
     * Shows a confirmation message before deleting
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_buttonDeleteAppointment(ActionEvent event) throws SQLException {
        Appointment app = tableviewAppointments.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Delete selected appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Appointment.DeleteAppointment(app.getID());
            if (radioMonthly.isSelected()) {
                tableviewAppointments.setItems(Appointment.getMonthAppointments());
            }
            else {
                tableviewAppointments.setItems(Appointment.getWeekAppointments());
            }
        }
    }

    /**
     * Switches to the Update Appointment form. 
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    void onAction_buttonUpdateAppointment(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = tableviewAppointments.getSelectionModel().getSelectedItem();
        
        if (selectedAppointment != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateAppointmentForm.fxml"));
            loader.load();
            UpdateAppointmentFormController updateAppointmentController = loader.getController();
            updateAppointmentController.sendAppointment(selectedAppointment);

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select an appointment to update.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
    
    /**
     * Displays a report to the user that shows appointment types, locations and months 
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_btnReport(ActionEvent event) throws SQLException {
        class Type {
            String name;
            int amt;
            
            Type(String name, int amt) {
                this.name = name;
                this.amt = amt;
            }
        }
        
        class Months {
            int month;
            int amt;
            
            Months(int month, int amt) {
                this.month = month;
                this.amt = amt;
            }
        }
        
        class Location {
            String name;
            int amt;

            public Location(String name, int amt) {
                this.name = name;
                this.amt = amt;
            }
        }
        
        List<Type> types = new ArrayList();
        List<Months> months = new ArrayList();
        List<Location> locations = new ArrayList();
        
        if (inFrench) {
            rb = Localization.getResourceBundle();
        }
        
        for (Appointment appointment : Appointment.getAllAppointments()) {
            if (!types.isEmpty()) {
                for (Type type : types) {
                    if (type.name.equals(appointment.getType())) {
                        type.amt++;
                    }
                    else {
                        types.add(new Type(appointment.getType(), 1));
                        break;
                    }
                }
            }
            else {
                types.add(new Type(appointment.getType(), 1));
            }
            
            if (!locations.isEmpty()) {
                for (Location location : locations) {
                    if (location.name.equals(appointment.getLocation())) {
                        location.amt++;
                    }
                    else {
                        locations.add(new Location(appointment.getLocation(), 1));
                        break;
                    }
                }
            }
            else {
                locations.add(new Location(appointment.getLocation(), 1));
            }
            
            int monthValue = Appointment.ConvertToLocalDateTime(appointment.getStartDateTime()).getMonthValue();
            if (!months.isEmpty()) {
                for (Months month : months) {
                    if (month.month == monthValue) {
                        month.amt++;
                    }
                    else {
                        months.add(new Months(monthValue, 1));
                        break;
                    }
                }
            }
            else {
                months.add(new Months(monthValue, 1));
            }
        }
        
        String reportText = "";
        for (Type type : types) {
            if (inFrench) {
                reportText += rb.getString("Appointments of type ");
            }
            else {
                reportText += ("Appointments of type ");
            }
            reportText += type.name + ": " + type.amt + "\n";
        }
        
        for (Location location : locations) {
            if (inFrench) {
                reportText += rb.getString("Appointments of location ");
            }
            else {
                reportText += ("Appointments of location ");
            }
            reportText += location.name + ": " + location.amt + "\n";
        }
        
        for (Months month : months) {
            if (inFrench) {
                reportText += rb.getString("Appointments in month ");
            }
            else {
                reportText += ("Appointments in month ");
            }
            reportText += month.month + ": " + month.amt + "\n";
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (inFrench) {
            alert.setTitle(rb.getString("Report"));
            alert.setHeaderText(rb.getString("Appointments Report"));
        }
        else {
            alert.setTitle("Report");
            alert.setHeaderText("Appointments Report");
        }
        alert.setContentText(reportText);
        alert.showAndWait();
    }
    
    /**
     * Displays a schedule to the user for a selected contact 
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_buttonSchedule(ActionEvent event) throws SQLException {
        String contactName = comboSelectContact.getSelectionModel().getSelectedItem();
        ArrayList<Appointment> appointments = Contact.getAppointments(contactName);
        String alertText = "";
        
        if (inFrench) {
            rb = Localization.getResourceBundle();
            if (appointments.isEmpty()) {
                alertText = contactName + rb.getString(" has no scheduled appointments.");
            }
            else {
                for (Appointment appointment : appointments) {
                    alertText += rb.getString("Appointment ID: ") + appointment.getID() + "\n" +
                            rb.getString("Title: ") + appointment.getTitle() + "\n" + 
                            rb.getString("Type: ") + appointment.getType() + "\n" + 
                            rb.getString("Description: ") + appointment.getDescription() + "\n" + 
                            rb.getString("Start: ") + appointment.getStartDateTime() + "\n" + 
                            rb.getString("End: ") + appointment.getEndDateTime() + "\n" + 
                            rb.getString("Customer ID: ") + appointment.getCustomerID() + "\n\n";
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("Schedule"));
            alert.setHeaderText(rb.getString("Schedule for ") + contactName);
            alert.setContentText(alertText);
            alert.showAndWait();
        }
        else {
            if (appointments.isEmpty()) {
                alertText = contactName + " has no scheduled appointments.";
            }
            else {
                for (Appointment appointment : appointments) {
                    alertText += "Appointment ID: " + appointment.getID() + "\n" +
                            "Title: " + appointment.getTitle() + "\n" + 
                            "Type: " + appointment.getType() + "\n" + 
                            "Description: " + appointment.getDescription() + "\n" + 
                            "Start: " + appointment.getStartDateTime() + "\n" + 
                            "End: " + appointment.getEndDateTime() + "\n" + 
                            "Customer ID: " + appointment.getCustomerID() + "\n\n";
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Schedule");
            alert.setHeaderText("Schedule for " + contactName);
            alert.setContentText(alertText);
            alert.showAndWait();
        }
        
    }
    
    /**
     * Switches to the Customers form. 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onAction_buttonCustomers(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**
     * Switches to show only appointments within the next month
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_radioMonthly(ActionEvent event) throws SQLException {
        tableviewAppointments.setItems(Appointment.getMonthAppointments());
    }

    /**
     * Switches to show only appointments within the next week 
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_radioWeekly(ActionEvent event) throws SQLException {
        tableviewAppointments.setItems(Appointment.getWeekAppointments());
    }
}