package controller;

import helper.LambdaInterface2;
import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.User;

/**
 * Controller for the Add Appointment Form.
 * Contains a lambda expression for checking if the day of the week of a LocalDate variable matches a string, which is used to check if the user tries to schedule an appointment on a weekend.
 * @author Evan
 */
public class AddAppointmentFormController implements Initializable {

    Stage stage;
    Parent scene;
    Integer newAppointmentID;
    
    static LambdaInterface2 lambda2 = (date, day) -> date.getDayOfWeek().toString().equals(day);
    
    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonSave;

    @FXML
    private ComboBox<String> comboContact;
    
    @FXML
    private ComboBox<Integer> comboEndHour;

    @FXML
    private ComboBox<Integer> comboEndMinute;

    @FXML
    private ComboBox<Integer> comboStartHour;

    @FXML
    private ComboBox<Integer> comboStartMinute;

    @FXML
    private DatePicker datepickerEnd;

    @FXML
    private DatePicker datepickerStart;

    @FXML
    private TextField textfieldDescription;

    @FXML
    private TextField textfieldLocation;

    @FXML
    private TextField textfieldTitle;

    @FXML
    private TextField textfieldType;
    
    @FXML
    private TextField textfieldUserID;
    
    @FXML
    private TextField textfieldAppointmentID;

    @FXML
    private TextField textfieldCustomerID;

    @FXML
    private Label labelAddAppointment;

    @FXML
    private Label labelAppointmentID;

    @FXML
    private Label labelContact;

    @FXML
    private Label labelCustomerID;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelEndDate;

    @FXML
    private Label labelEndTime;

    @FXML
    private Label labelLocation;

    @FXML
    private Label labelStartDate;

    @FXML
    private Label labelStartTime;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelType;

    @FXML
    private Label labelUserID;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboContact.setItems(Contact.getAllContactNames());
            newAppointmentID = Appointment.generateID();
        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboStartHour.setItems(Appointment.hours);
        comboStartMinute.setItems(Appointment.minutes);
        comboEndHour.setItems(Appointment.hours);
        comboEndMinute.setItems(Appointment.minutes);
        textfieldAppointmentID.setText(newAppointmentID.toString());
        
        //Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            labelAddAppointment.setText(rb.getString("Add") + " " + rb.getString("Appointment"));
            labelTitle.setText(rb.getString("Title"));
            labelDescription.setText(rb.getString("Description"));
            labelLocation.setText(rb.getString("Location"));
            labelContact.setText(rb.getString("Contact"));
            comboContact.setPromptText(rb.getString("Select"));
            labelType.setText(rb.getString("Type"));
            labelStartDate.setText(rb.getString("Start") + " " + rb.getString("Date"));
            labelStartTime.setText(rb.getString("Start") + " " + rb.getString("Time"));
            comboStartHour.setPromptText(rb.getString("Hour"));
            comboStartMinute.setPromptText(rb.getString("Minute"));
            labelEndDate.setText(rb.getString("End") + " " + rb.getString("Date"));
            labelEndTime.setText(rb.getString("End") + " " + rb.getString("Time"));
            comboEndHour.setPromptText(rb.getString("Hour"));
            comboEndMinute.setPromptText(rb.getString("Minute"));
            labelAppointmentID.setText(rb.getString("Appointment") + " ID");
            labelCustomerID.setText(rb.getString("Customer") + " ID");
            labelUserID.setText(rb.getString("User") + " ID");
            buttonSave.setText(rb.getString("Save"));
            buttonCancel.setText(rb.getString("Cancel"));
        }
    }
    
    @FXML
    void onAction_ComboContact(ActionEvent event) {

    }

    /**
     * Logic for the Cancel button
     * Shows a confirmation message before returning to the main form
     * @param event
     * @throws IOException 
     */
    @FXML
    void onAction_buttonCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        if (Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle rb = Localization.getResourceBundle();
            alert.setContentText(rb.getString("This will clear all fields. Continue?"));
        }
        else {
            alert.setContentText("This will clear all fields. Continue?");
        }
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Creates a new appointment from the data in the input fields
     * Checks if the new appointment is within business hours and does not overlap another appointment
     * If not, saves the appointment to the database
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void onAction_buttonSave(ActionEvent event) throws SQLException, IOException {
        //Retrieve values from input devices
        String newTitle = textfieldTitle.getText();
        String newDesc = textfieldDescription.getText();
        String newLocation = textfieldLocation.getText();
        String newContact = comboContact.getSelectionModel().getSelectedItem();
        String newType = textfieldType.getText();
        LocalDate newStartDate = datepickerStart.getValue();
        int newStartHour = comboStartHour.getSelectionModel().getSelectedItem();
        int newStartMinute = comboStartMinute.getSelectionModel().getSelectedItem();
        LocalDate newEndDate = datepickerEnd.getValue();
        int newEndHour = comboEndHour.getSelectionModel().getSelectedItem();
        int newEndMinute = comboEndMinute.getSelectionModel().getSelectedItem();
        int newCustomerID = Integer.parseInt(textfieldCustomerID.getText());
        int newUserID = Integer.parseInt(textfieldUserID.getText());
        int newContactID = Contact.getContact(newContact).getContactID();
        //Create Start and End DateTimes
        LocalTime newStartTime = LocalTime.of(newStartHour, newStartMinute);
        LocalDateTime newStartDateTime = LocalDateTime.of(newStartDate, newStartTime);
        LocalTime newEndTime = LocalTime.of(newEndHour, newEndMinute);
        LocalDateTime newEndDateTime = LocalDateTime.of(newEndDate, newEndTime);
        
        LocalDateTime createDate = LocalDateTime.now();
        String userCreatedBy = User.getCurrentUserName();
        
        ZonedDateTime newStartZDT = ZonedDateTime.of(newStartDateTime, ZoneId.systemDefault());
        LocalDateTime newStartEST = Appointment.ConvertToTimeZone(newStartZDT, ZoneId.of("America/New_York"));
        
        ZonedDateTime newEndZDT = ZonedDateTime.of(newEndDateTime, ZoneId.systemDefault());
        LocalDateTime newEndEST = Appointment.ConvertToTimeZone(newEndZDT, ZoneId.of("America/New_York"));
        
        // Check if appointment is within business hours
        if (newStartEST.toLocalTime().isBefore(LocalTime.of(8, 0)) || 
                newStartEST.toLocalTime().isAfter(LocalTime.of(22, 0)) || 
                newEndEST.toLocalTime().isBefore(LocalTime.of(8, 0)) ||
                newEndEST.toLocalTime().isAfter(LocalTime.of(22,0))) {
            ShowOutsideBusinessTimeAlert();
            return;
        }
        if (newStartEST.toLocalDate().getDayOfYear() != newEndEST.toLocalDate().getDayOfYear())
        {
            ShowOutsideBusinessTimeAlert();
            return;
        }
        if (lambda2.checkIfDayEquals(newStartDate, "SATURDAY") ||
                lambda2.checkIfDayEquals(newStartDate, "SUNDAY") ||
                lambda2.checkIfDayEquals(newEndDate, "SATURDAY") ||
                lambda2.checkIfDayEquals(newEndDate, "SUNDAY")) {
            ShowOutsideBusinessTimeAlert();
            return;
        }
        
        // Check if appointment overlaps an existing appointment
        if (Appointment.CheckForOverlap(
                LocalDateTime.of(datepickerStart.getValue(), LocalTime.of(comboStartHour.getValue(), comboStartMinute.getValue())),
                LocalDateTime.of(datepickerEnd.getValue(), LocalTime.of(comboEndHour.getValue(), comboEndMinute.getValue())),
                newCustomerID, newAppointmentID)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            
            if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle rb = Localization.getResourceBundle();
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Cannot overlap appointments."));
            }
            else {
                alert.setTitle("Error");
                alert.setContentText("Cannot overlap appointments.");
            }
            alert.showAndWait();
        }
        else {
            Appointment newAppointment = new Appointment(newAppointmentID, newTitle,
                newDesc, newLocation, newType, newStartDateTime, newEndDateTime,
                createDate, userCreatedBy, createDate, userCreatedBy, newCustomerID, 
                newUserID, newContactID, newContact);
            Appointment.Insert(newAppointment);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     * Displays an alert to tell the user they cannot schedule an appointment outside business hours.
     */
    void ShowOutsideBusinessTimeAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
            
            if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle rb = Localization.getResourceBundle();
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Cannot schedule outside business hours (8AM to 10PM EST) or on weekends."));
            }
            else {
                alert.setTitle("Error");
                alert.setContentText("Cannot schedule outside business hours (8AM to 10PM EST) or on weekends.");
            }
            alert.showAndWait();
    }
}