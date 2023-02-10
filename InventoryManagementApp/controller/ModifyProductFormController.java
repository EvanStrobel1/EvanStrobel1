package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * Controller for the Modify Product Form.
 * FUTURE ENHANCEMENT - A way to quickly edit parts with incorrect data that are associated with the product 
 * 
 * @author Evan
 */
public class ModifyProductFormController implements Initializable{
    
    Stage stage;
    Parent scene;
    Product selectedProduct;
    int originalID;
    ObservableList<Part> newAssociatedParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> tableParts;

    @FXML
    private TableView<Part> tableParts1;

    @FXML
    private TableColumn<Part, Integer> tablecolumnAssociatedPartID;

    @FXML
    private TableColumn<Part, Integer> tablecolumnAssociatedPartInv;

    @FXML
    private TableColumn<Part, String> tablecolumnAssociatedPartName;

    @FXML
    private TableColumn<Part, Double> tablecolumnAssociatedPartPrice;

    @FXML
    private TableColumn<Part, Integer> tablecolumnPartID;

    @FXML
    private TableColumn<Part, Integer> tablecolumnPartInv;

    @FXML
    private TableColumn<Part, String> tablecolumnPartName;

    @FXML
    private TableColumn<Part, Double> tablecolumnPartPrice;
    
    @FXML
    private TextField textFieldModifyProductSearch;

    @FXML
    private TextField textfieldModifyProductID;

    @FXML
    private TextField textfieldModifyProductInv;

    @FXML
    private TextField textfieldModifyProductMax;

    @FXML
    private TextField textfieldModifyProductMin;

    @FXML
    private TextField textfieldModifyProductName;

    @FXML
    private TextField textfieldModifyProductPrice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tableParts.setItems(Inventory.getAllParts());
        tablecolumnPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablecolumnPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tablecolumnPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablecolumnPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        tableParts1.setItems(newAssociatedParts);
        tablecolumnAssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablecolumnAssociatedPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tablecolumnAssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablecolumnAssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }  
    
    /**
     * Logic for the Add Part button
     * Adds the selected part to the product's associated parts list
     * 
     * @param event 
     */
    @FXML
    void onActionModifyProductAdd(ActionEvent event) {
        
        newAssociatedParts.add(tableParts.getSelectionModel().getSelectedItem());
        
    }

    /**
     * Logic for the cancel button
     * Shows a confirmation message
     * sends the user to the main form if they click OK
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyProductCancel(ActionEvent event) throws IOException{
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("This will clear all fields. Continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            newAssociatedParts.clear();
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Logic for the remove associated part button
     * removes an associated part from the product's newAssociatedParts list
     * Shows a confirmation message to the user before executing 
     * 
     * @param event 
     */
    @FXML
    void onActionModifyProductRemovePart(ActionEvent event) {
        
        Part removingPart = tableParts1.getSelectionModel().getSelectedItem();
        
        if (removingPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Remove associated part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                
                newAssociatedParts.remove(removingPart);
                
            }
        }
    }
    
    /**
     * Logic for the Save button
     * Validates input and shows an error if input is invalid
     * Sends the user back to the main form
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyProductSave(ActionEvent event) throws IOException{
        
        try {
            int id = Integer.parseInt(textfieldModifyProductID.getText());
            String name = textfieldModifyProductName.getText();
            double price = Double.parseDouble(textfieldModifyProductPrice.getText());
            int stock = Integer.parseInt(textfieldModifyProductInv.getText());
            int min = Integer.parseInt(textfieldModifyProductMin.getText());
            int max = Integer.parseInt(textfieldModifyProductMax.getText());
            selectedProduct = new Product(id, name, price, stock, max, min);
            for (Part part : newAssociatedParts) {
                selectedProduct.addAssociatedPart(part);
            }

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
            
            Product oldProduct = Inventory.lookupProduct(originalID);
            int index = Inventory.getAllProducts().indexOf(oldProduct);
            Inventory.updateProduct(index, selectedProduct);

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
     * Logic for the search text field
     * filters the parts list based on the user's search
     * shows everything if the search is empty
     * 
     * @param event 
     */
    @FXML
    void onActionModifyProductSearch(ActionEvent event) {
        String search = textFieldModifyProductSearch.getText();
        
        for(Part part : Inventory.getAllParts()) {
            if(search.equals(Integer.toString(part.getId()))) {
                tableParts.getSelectionModel().select(part);
                return;
            }
            else if(part.getName().contains(search)) {
                tableParts.getSelectionModel().select(part);
                return;
            }
        }
    }
    
    /**
     * Sets the selectedProduct variable
     * 
     * @param product 
     */
    void setSelectedProduct(Product product) {
        selectedProduct = product;
    }
    
    /**
     * Getter for the selectedProduct variable
     * 
     * @return 
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }
    
    /**
     * Takes a product sent by the main form
     * fills out the input fields with the selected product's info
     * 
     * @param product 
     */
    public void sendProduct(Product product) {
        
        selectedProduct = product;
        originalID = product.getId();
        textfieldModifyProductID.setText(String.valueOf(product.getId()));
        textfieldModifyProductName.setText(product.getName());
        textfieldModifyProductInv.setText(String.valueOf(product.getStock()));
        textfieldModifyProductMin.setText(String.valueOf(product.getMin()));
        textfieldModifyProductMax.setText(String.valueOf(product.getMax()));
        textfieldModifyProductPrice.setText(String.valueOf(product.getPrice()));
        for (Part part : selectedProduct.getAllAssociatedParts()) {
            newAssociatedParts.add(part);
        }
        tableParts1.setItems(newAssociatedParts);
        
    }
}