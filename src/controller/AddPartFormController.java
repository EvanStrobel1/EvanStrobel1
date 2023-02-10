package controller;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * Controls the FXML form for adding a part to the list of all parts
 * FUTURE ENHANCEMENT - A button to auto-generate data for unfilled fields
 * 
 * @author Evan
 */
public class AddPartFormController {
    
    Stage stage;
    Parent scene;
    boolean outsourced = false;
    
    @FXML
    private Label labelAddPartCompanyName;

    @FXML
    private Label labelAddPartMachineID;

    @FXML
    private TextField textfieldAddPartID;

    @FXML
    private TextField textfieldAddPartInv;

    @FXML
    private TextField textfieldAddPartMachineID;

    @FXML
    private TextField textfieldAddPartMax;

    @FXML
    private TextField textfieldAddPartMin;

    @FXML
    private TextField textfieldAddPartName;

    @FXML
    private TextField textfieldAddPartPrice;
    
    @FXML
    private TextField textfieldAddPartCompanyName;

    /**
     * Controls the Cancel button on the Add Part form.
     * Shows a confirmation alert and sends the user back to the Main Form
     * if they click the OK button
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddPartCancel(ActionEvent event) throws IOException{
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("This will clear all fields. Continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    /**
     * Logic for the In House radio button.
     * Hides company name fields and shows machine ID fields.
     * Sets the outsourced boolean to false 
     * 
     * @param event 
     */
    @FXML
    void onActionAddPartInHouse(ActionEvent event) {
        labelAddPartMachineID.setVisible(true);
        textfieldAddPartMachineID.setVisible(true);
        labelAddPartCompanyName.setVisible(false);
        textfieldAddPartCompanyName.setVisible(false);
        outsourced = false;
    }

    /**
     * Logic for the Outsourced radio button.
     * Hides the machine ID fields and shows company name fields
     * set the outsourced boolean to true
     * 
     * @param event 
     */
    @FXML
    void onActionAddPartOutsourced(ActionEvent event) {
        labelAddPartMachineID.setVisible(false);
        textfieldAddPartMachineID.setVisible(false);
        labelAddPartCompanyName.setVisible(true);
        textfieldAddPartCompanyName.setVisible(true);
        outsourced = true;
    }

    /**
     * Controls the save button.
     * Gets the values of the text fields then error checks them.
     * Shows an ERROR alert if any input is invalid.
     * Adds the part to the parts list if input is valid then returns
     *  the user to the main form.
     * RUNTIME ERROR - 'MainForm.fxml' was spelled incorrectly but fixed
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddPartSave(ActionEvent event) throws IOException{
        
        try {
            int id = Integer.parseInt(textfieldAddPartID.getText());
            String name = textfieldAddPartName.getText();
            int stock = Integer.parseInt(textfieldAddPartInv.getText());
            double price = Double.parseDouble(textfieldAddPartPrice.getText());
            int min = Integer.parseInt(textfieldAddPartMin.getText());
            int max = Integer.parseInt(textfieldAddPartMax.getText());

            //Error checking
            if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Entry Error");
                alert.setContentText("Min must be less than Max.");
                alert.showAndWait();
                return;
            }
            else if (stock < min || stock > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Entry Error");
                alert.setContentText("Inventory must be between min and max values.");
                alert.showAndWait();
                return;
            }
            
            if(outsourced) {
                String companyName = textfieldAddPartCompanyName.getText();
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }
            else {
                int machineID = Integer.parseInt(textfieldAddPartMachineID.getText());
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
            }

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User Entry Error");
            alert.setContentText("Please enter valid values for each field.");
            alert.showAndWait();
        }
    }
    
    /**
     * Generates a unique ID for a created part based on the part IDs
     *  that already exist
     */
    public void createUniqueID() {
        int newID = 1;
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() == newID) {
                newID++;
            }
        }
        textfieldAddPartID.setText(String.valueOf(newID));
    }
}