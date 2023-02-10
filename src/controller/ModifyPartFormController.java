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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * Controller for the Modify Part form.
 * FUTURE ENHANCEMENT - A button to clear all fields
 * 
 * @author Evan
 */
public class ModifyPartFormController {
    
    Stage stage;
    Parent scene;
    Part selectedPart;

    @FXML
    private RadioButton radioModifyPartInHouse;

    @FXML
    private RadioButton radioModifyPartOutsourced;
    
    @FXML
    private Label labelCompanyName;

    @FXML
    private Label labelMachineID;

    @FXML
    private TextField textfieldModifyPartID;

    @FXML
    private TextField textfieldModifyPartInv;

    @FXML
    private TextField textfieldModifyPartMachineID;
    
    @FXML
    private TextField textfieldModifyPartCompanyName;

    @FXML
    private TextField textfieldModifyPartMax;

    @FXML
    private TextField textfieldModifyPartMin;

    @FXML
    private TextField textfieldModifyPartName;

    @FXML
    private TextField textfieldModifyPartPrice;

    /**
     * Logic for the Cancel button.
     * Shows a confirmation warning to the user before executing.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyPartCancel(ActionEvent event) throws IOException {
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
     * Hides the company name fields
     * Shows the machine ID fields 
     * 
     * @param event 
     */
    @FXML
    void onActionModifyPartInHouse(ActionEvent event) {
        textfieldModifyPartCompanyName.setVisible(false);
        labelCompanyName.setVisible(false);
        textfieldModifyPartMachineID.setVisible(true);
        labelMachineID.setVisible(true);
        radioModifyPartOutsourced.setSelected(false);
    }

    /**
     * Logic for the Outsourced radio button.
     * Shows the company name fields
     * Hides the machine ID fields 
     * 
     * @param event 
     */
    @FXML
    void onActionModifyPartOutsourced(ActionEvent event) {
        textfieldModifyPartCompanyName.setVisible(true);
        labelCompanyName.setVisible(true);
        textfieldModifyPartMachineID.setVisible(false);
        labelMachineID.setVisible(false);
        radioModifyPartInHouse.setSelected(false);
    }

    /**
     * Logic for the Save button.
     * Validates input before adding the part to the parts list
     * Sends the user back to the main form
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyPartSave(ActionEvent event) throws IOException{
       
        try {
            Part newPart;
            int id = Integer.parseInt(textfieldModifyPartID.getText());
            String name = textfieldModifyPartName.getText();
            int inv = Integer.parseInt(textfieldModifyPartInv.getText());
            int min = Integer.parseInt(textfieldModifyPartMin.getText());
            int max = Integer.parseInt(textfieldModifyPartMax.getText());
            Double price = Double.parseDouble(textfieldModifyPartPrice.getText());

            //Error checking
            if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Entry Error");
                alert.setContentText("Min must be less than Max.");
                alert.showAndWait();
                return;
            }
            else if (inv < min || inv > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Entry Error");
                alert.setContentText("Inventory must be between min and max values.");
                alert.showAndWait();
                return;
            }
            
            if(radioModifyPartOutsourced.isSelected()) {
                String companyName = textfieldModifyPartCompanyName.getText();
                newPart = new Outsourced(id, name, price, inv, min, max, companyName);
                Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), newPart);
            }
            else {
                int machineID = Integer.parseInt(textfieldModifyPartMachineID.getText());
                newPart = new InHouse(id, name, price, inv, min, max, machineID);
                Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), newPart);
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
     * Receives a part sent by the Main Form
     * fills out the fields based on the sent part's data
     * 
     * @param part 
     */
    public void sendPart(Part part) {
        selectedPart = part;
        textfieldModifyPartID.setText(String.valueOf(part.getId()));
        textfieldModifyPartName.setText(part.getName());
        textfieldModifyPartInv.setText(String.valueOf(part.getStock()));
        textfieldModifyPartMin.setText(String.valueOf(part.getMin()));
        textfieldModifyPartMax.setText(String.valueOf(part.getMax()));
        textfieldModifyPartPrice.setText(String.valueOf(part.getPrice()));
        
        if(part instanceof Outsourced) {
            textfieldModifyPartCompanyName.setVisible(true);
            labelCompanyName.setVisible(true);
            textfieldModifyPartMachineID.setVisible(false);
            labelMachineID.setVisible(false);
            textfieldModifyPartCompanyName.setText(((Outsourced)part).getCompanyName());
            radioModifyPartOutsourced.setSelected(true);
            radioModifyPartInHouse.setSelected(false);
        }
        else {
            textfieldModifyPartCompanyName.setVisible(false);
            labelCompanyName.setVisible(false);
            textfieldModifyPartMachineID.setVisible(true);
            labelMachineID.setVisible(true);
            textfieldModifyPartMachineID.setText(String.valueOf(((InHouse)part).getMachineId()));
            radioModifyPartInHouse.setSelected(true);
            radioModifyPartOutsourced.setSelected(false);
        }
    }
}