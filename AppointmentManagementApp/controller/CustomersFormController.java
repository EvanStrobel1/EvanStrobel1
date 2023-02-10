package controller;

import helper.Localization;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

/**
 * Controller for the Customers Form.
 * @author Evan
 */
public class CustomersFormController implements Initializable{

    Stage stage;
    Parent scene;
    
    @FXML
    private Label labelCustomers;
    
    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonAppointments;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonUpdate;
    
    @FXML
    private TableView<Customer> tableviewCustomers;
    
    @FXML
    private TableColumn<String, Customer> tablecolumnAddress;

    @FXML
    private TableColumn<String, Customer> tablecolumnCreateDate;

    @FXML
    private TableColumn<String, Customer> tablecolumnCreatedBy;

    @FXML
    private TableColumn<Integer, Customer> tablecolumnCustomerID;

    @FXML
    private TableColumn<String, Customer> tablecolumnCustomerName;

    @FXML
    private TableColumn<Integer, Customer> tablecolumnDivisionID;

    @FXML
    private TableColumn<String, Customer> tablecolumnLastUpdate;

    @FXML
    private TableColumn<String, Customer> tablecolumnLastUpdatedBy;

    @FXML
    private TableColumn<String, Customer> tablecolumnPhone;

    @FXML
    private TableColumn<String, Customer> tablecolumnPostalCode;

    /**
     * Initializes the Customers form.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tableviewCustomers.setItems(Customer.getAllCustomers());
            tablecolumnCustomerID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            tablecolumnCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tablecolumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            tablecolumnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            tablecolumnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            tablecolumnCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
            tablecolumnCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            tablecolumnLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
            tablecolumnLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
            tablecolumnDivisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        } catch (SQLException ex) {
            Logger.getLogger(CustomersFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Localize
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            labelCustomers.setText(rb.getString("Customers"));
            buttonAppointments.setText(rb.getString("Appointment") + " >");
            tablecolumnCustomerID.setText(rb.getString("Customer") + " ID");
            tablecolumnCustomerName.setText(rb.getString("Customer Name"));
            tablecolumnAddress.setText(rb.getString("Address"));
            tablecolumnPostalCode.setText(rb.getString("Postal Code"));
            tablecolumnPhone.setText(rb.getString("Phone"));
            tablecolumnCreateDate.setText(rb.getString("Create Date"));
            tablecolumnCreatedBy.setText(rb.getString("Created By"));
            tablecolumnLastUpdate.setText(rb.getString("Last Update"));
            tablecolumnLastUpdatedBy.setText(rb.getString("Last Updated By"));
            tablecolumnDivisionID.setText(rb.getString("Division") + " ID");
            buttonAdd.setText(rb.getString("Add"));
            buttonUpdate.setText(rb.getString("Update"));
            buttonDelete.setText(rb.getString("Delete"));
        }
    }
    
    /**
     * Switches to the Add Customer form
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionButtonAdd(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Deletes a selected customer after displaying a confirmation message 
     * @param event
     * @throws SQLException 
     */
    @FXML
    void onActionButtonDelete(ActionEvent event) throws SQLException {
        int selected = tableviewCustomers.getSelectionModel().getSelectedItem().getID();
        
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Delete selected customer?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Customer.DeleteCustomer(selected);
        }
    }

    /**
     * Switches to the Update Customer form
     * @param event
     * @throws IOException
     * @throws SQLException 
     */
    @FXML
    void onActionButtonUpdate(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = tableviewCustomers.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateCustomerForm.fxml"));
            loader.load();
            UpdateCustomerFormController updateCustomerController = loader.getController();
            updateCustomerController.sendCustomer(selectedCustomer);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select an customer to update.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
    
    /**
     * Switches to the Appointments form
     * @param event
     * @throws IOException 
     */
    @FXML
    void onAction_buttonAppointments(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}