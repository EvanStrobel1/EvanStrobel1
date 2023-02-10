package controller;

import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
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
import model.Countries;
import model.Customer;
import model.User;

/**
 * Controller for the Update Customer Form.
 * @author Evan
 */
public class UpdateCustomerFormController implements Initializable {

    Customer updatingCustomer;
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
    private Label labelAddress;

    @FXML
    private Label labelCountry;

    @FXML
    private Label labelDivision;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelPostal;

    @FXML
    private Label labelUpdateCustomer;
    
    /**
     * Initializes the Update Customer form 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            labelUpdateCustomer.setText(rb.getString("Update") + " " + rb.getString("Customer"));
            labelName.setText(rb.getString("Name"));
            labelCountry.setText(rb.getString("Country"));
            comboCountry.setPromptText(rb.getString("Select"));
            labelDivision.setText(rb.getString("First-level") + " " + rb.getString("Division"));
            comboDivision.setPromptText(rb.getString("Select"));
            labelAddress.setText(rb.getString("Address"));
            labelPostal.setText(rb.getString("Postal") + " " + rb.getString("Code"));
            labelPhone.setText(rb.getString("Phone") + " " + rb.getString("Number"));
            buttonSave.setText(rb.getString("Save"));
            buttonCancel.setText(rb.getString("Cancel"));
        }
    }
    
    /**
     * Returns to the Customers form after showing a confirmation message 
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
     * Saves the new customer
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void onAction_Save(ActionEvent event) throws SQLException, IOException {
        
        String newName = textfieldName.getText();
        String newAddress = textfieldAddress.getText();
        String newPostal = textfieldPostal.getText();
        String newPhone = textfieldPhone.getText();
        LocalDateTime createDate = Appointment.ConvertToLocalDateTime(updatingCustomer.getCreateDate());
        String createdBy = updatingCustomer.getCreatedBy();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String lastUpdatedBy = User.getCurrentUserName();
        int newDivisionID = Countries.getDivisionID(comboDivision.getValue());
        
        Customer newCustomer = new Customer(updatingCustomer.getID(), newName,
                newAddress, newPostal, newPhone, createDate, createdBy, 
                lastUpdate, lastUpdatedBy, newDivisionID);
        
        Customer.Update(updatingCustomer.getID(), newCustomer);
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomersForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Changes the contents of the division combo box based on the selected country 
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
     * Receives a customer's data 
     * @param newCust
     * @throws SQLException 
     */
    public void sendCustomer(Customer newCust) throws SQLException {
        updatingCustomer = newCust;
        textfieldID.setText(Integer.toString(newCust.getID()));
        textfieldName.setText(newCust.getName());
        String country = Countries.getCountry(newCust.getDivisionID());
        comboCountry.setItems(Countries.getCountries());
        comboCountry.setValue(country);
        int countryID = Countries.getCountryID(country);
        comboDivision.setItems(Countries.getDivisions(countryID));
        comboDivision.setValue(Countries.getDivision(newCust.getDivisionID()));
        textfieldAddress.setText(newCust.getAddress());
        textfieldPostal.setText(newCust.getPostalCode());
        textfieldPhone.setText(newCust.getPhoneNumber());
    }
}