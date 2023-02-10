package controller;

import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Countries;
import model.Customer;
import model.User;

/**
 * Controller for the Add Customer Form.
 * @author Evan
 */
public class AddCustomerFormController implements Initializable {

    int newID;
    Stage stage;
    Parent scene;
    
    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonSave;

    @FXML
    private ComboBox<String> comboCountry;

    @FXML
    private ComboBox<String> comboDivision;

    @FXML
    private TextField textfieldAddress;

    @FXML
    private TextField textfieldID;

    @FXML
    private TextField textfieldName;

    @FXML
    private TextField textfieldPhone;

    @FXML
    private TextField textfieldPostal;

    @FXML
    private Label labelAddCustomer;

    @FXML
    private Label labelAddress;

    @FXML
    private Label labelCountry;

    @FXML
    private Label labelFirstLevelDivision;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelPostal;
    
    /**
     * Initializes the Add Customer form.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            comboCountry.setItems(Countries.getCountries());
            newID = Customer.generateID();
            textfieldID.setText(Customer.generateID().toString());
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            labelAddCustomer.setText(rb.getString("Add") + " " + rb.getString("Customer"));
            labelName.setText(rb.getString("Name"));
            labelCountry.setText(rb.getString("Country"));
            comboCountry.setPromptText(rb.getString("Select"));
            labelFirstLevelDivision.setText(rb.getString("First-level") + " " + rb.getString("Division"));
            comboDivision.setPromptText(rb.getString("Select"));
            labelAddress.setText(rb.getString("Address"));
            labelPostal.setText(rb.getString("Postal") + " " + rb.getString("Code"));
            labelPhone.setText(rb.getString("Phone") + " " + rb.getString("Number"));
            buttonSave.setText(rb.getString("Save"));
            buttonCancel.setText(rb.getString("Cancel"));
        }
    }
    
    /**
     * Controls the logic for when the user changes the country ID which also changes the options for divisions
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onAction_comboCountry(ActionEvent event) throws SQLException {
        int newCountryID = Countries.getCountryID(comboCountry.getSelectionModel().getSelectedItem());
        comboDivision.setItems(Countries.getDivisions(newCountryID));
    }

    @FXML
    void onAction_comboDivision(ActionEvent event) {

    }
    
    /**
     * Shows an alert to the user before returning to the main form 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onAction_Cancel(ActionEvent event) throws IOException {
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
            scene = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Adds the new customer to the database
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void onAction_Save(ActionEvent event) throws SQLException, IOException {
        //Retrieve values from input devices
        String newName = textfieldName.getText();
        String newAddress = textfieldAddress.getText();
        String newPostal = textfieldPostal.getText();
        String newPhone = textfieldPhone.getText();
        LocalDateTime newCreateDate = LocalDateTime.now();
        String newCreatedBy = User.getCurrentUserName();
        LocalDateTime newLastUpdate = LocalDateTime.now();
        String newLastUpdatedBy = User.getCurrentUserName();
        int newDivisionID = Countries.getDivisionID(comboDivision.getSelectionModel().getSelectedItem());
        
        
        
        Customer newCustomer = new Customer(newID, newName, newAddress, 
                newPostal, newPhone, newCreateDate, newCreatedBy, 
                newLastUpdate, newLastUpdatedBy, newDivisionID);
        Customer.Insert(newCustomer);
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}