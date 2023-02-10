package controller;

import static controller.AddAppointmentFormController.lambda2;
import helper.LambdaInterface2;
import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.User;

/**
 * Controller for the Update Appointment Form.
 * @author Evan
 */
public class UpdateAppointmentFormController implements Initializable {

    Stage stage;
    Parent scene;
    
    String originalCreateDate;
    String originalCreatedBy;
    
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
    private TextField textfieldAppointmentID;

    @FXML
    private TextField textfieldCustomerID;

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
    private Label labelAppointmentID;

    @FXML
    private Label labelContact;

    @FXML
    private Label labelCustomerID;

    @FXML
    private Label labelDesc;

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
    private Label labelUpdateAppointment;

    @FXML
    private Label labelUserID;
    
    /**
     * Initializes the controller class
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            labelUpdateAppointment.setText(rb.getString("Update") + " " + rb.getString("Appointment"));
            labelTitle.setText(rb.getString("Title"));
            labelDesc.setText(rb.getString("Description"));
            labelLocation.setText(rb.getString("Location"));
            labelContact.setText(rb.getString("Contact"));
            comboContact.setPromptText(rb.getString("Select"));
            try {
                comboContact.setItems(Contact.getAllContactNames());
            } catch (SQLException ex) {
                Logger.getLogger(UpdateAppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * Switches back to the appointment form after showing a confirmation alert
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
     * Updates an appointment's data in the database after verifying it
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void onAction_buttonSave(ActionEvent event) throws SQLException, IOException {
        //Retrieve values from input devices
        Integer apptID = Integer.parseInt(textfieldAppointmentID.getText());
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
        if (newStartEST.toLocalTime().isBefore(LocalTime.of(8, 0)) || newEndEST.toLocalTime().isAfter(LocalTime.of(22,0))) {
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
                newCustomerID, apptID)) {
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
            Appointment newAppointment = new Appointment(apptID, newTitle,
                newDesc, newLocation, newType, newStartDateTime, newEndDateTime,
                createDate, userCreatedBy, createDate, userCreatedBy, newCustomerID, 
                newUserID, newContactID, newContact);
            Appointment.Update(apptID, newAppointment);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Receives an appointment's data from the Appointments form 
     * @param newAppt
     * @throws SQLException 
     */
    public void sendAppointment (Appointment newAppt) throws SQLException {
        textfieldTitle.setText(newAppt.getTitle());
        textfieldDescription.setText(newAppt.getDescription());
        textfieldLocation.setText(newAppt.getLocation());
        comboContact.setValue(newAppt.getContactName());
        comboContact.setItems(Contact.getAllContactNames());
        textfieldType.setText(newAppt.getType());
        datepickerStart.setValue(LocalDate.parse(newAppt.getStartDate()));
        
        comboStartHour.setItems(Appointment.hours);
        comboStartHour.setValue(Integer.parseInt(newAppt.getStartTime().substring(0,2)));
        comboStartMinute.setItems(Appointment.minutes);
        comboStartMinute.setValue(Integer.parseInt(newAppt.getStartTime().substring(3,5)));
        
        datepickerEnd.setValue(LocalDate.parse(newAppt.getEndDate()));
        comboEndHour.setItems(Appointment.hours);
        comboEndHour.setValue(Integer.parseInt(newAppt.getEndTime().substring(0,2)));
        comboEndMinute.setItems(Appointment.minutes);
        comboEndMinute.setValue(Integer.parseInt(newAppt.getEndTime().substring(3,5)));
        
        Integer apptID = newAppt.getID();
        textfieldAppointmentID.setText(apptID.toString());
        Integer custID = newAppt.getCustomerID();
        textfieldCustomerID.setText(custID.toString());
        Integer userID = newAppt.getUserID();
        textfieldUserID.setText(userID.toString());
        originalCreateDate = newAppt.getCreateDate();
        originalCreatedBy = newAppt.getCreatedBy();
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
