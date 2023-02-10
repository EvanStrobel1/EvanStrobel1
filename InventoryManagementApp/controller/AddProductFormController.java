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
 * Controls the Add Product form.
 * FUTURE ENHANCEMENT - A button to create an add part window while keeping this window open
 * @author Evan
 */
public class AddProductFormController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    ObservableList<Part> newAssociatedParts = FXCollections.observableArrayList();

    @FXML
    private Button buttonAddProductAdd;

    @FXML
    private Button buttonAddProductCancel;

    @FXML
    private Button buttonAddProductRemovePart;

    @FXML
    private Button buttonAddProductSave;
    
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
    private TextField textFieldAddProductSearch;

    @FXML
    private TextField textfieldAddProductID;

    @FXML
    private TextField textfieldAddProductInv;

    @FXML
    private TextField textfieldAddProductMax;

    @FXML
    private TextField textfieldAddProductMin;

    @FXML
    private TextField textfieldAddProductName;

    @FXML
    private TextField textfieldAddProductPrice;

    /**
     * Initializes the controller class.
     * RUNTIME ERROR - "price" was misspelled but corrected by renaming correctly.
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
     * Logic for the Add button.
     * Adds the selected part to the product's associated parts list.
     * RUNTIME ERROR - Did not check if there was no selection beforehand
     * @param event 
     */
    @FXML
    void onActionAddProductAdd(ActionEvent event) {
        if (tableParts.getSelectionModel().getSelectedItem() != null)
            newAssociatedParts.add(tableParts.getSelectionModel().getSelectedItem());
    }

    /**
     * Logic for the Cancel button.
     * Shows a CONFIRMATION warning
     * Sends the user back to the main form
     * RUNTIME ERROR - getResource command used an incorrect file destination
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddProductCancel(ActionEvent event) throws IOException {
        
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
     * Logic for the Remove button
     * Removes a part from the new product's associated parts list
     * RUNTIME ERROR - removingPart was initialized as a Product, fixed by initializing as a Part
     * @param event 
     */
    @FXML
    void onActionAddProductRemovePart(ActionEvent event) {
        
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
     * Checks input validity, showing errors if needed
     * Adds the new product to the products list
     * returns the user to the main form
     * RUNTIME ERROR - inv was initialized as a double, fixed by changing to int
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddProductSave(ActionEvent event) throws IOException{
        try {
            Product newProduct;
            int id = Integer.parseInt(textfieldAddProductID.getText());
            String name = textfieldAddProductName.getText();
            int inv = Integer.parseInt(textfieldAddProductInv.getText());
            double price = Double.parseDouble(textfieldAddProductPrice.getText());
            int min = Integer.parseInt(textfieldAddProductMin.getText());
            int max = Integer.parseInt(textfieldAddProductMax.getText());

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
            
            newProduct = new Product(id, name, price, inv, max, min);
            for(Part part : newAssociatedParts) {
                newProduct.addAssociatedPart(part);
            }

            Inventory.addProduct(newProduct);

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
     * Logic for the Search text field.
     * Filters the list of products based on the user's search
     * 
     * @param event 
     */
    @FXML
    void onActionAddProductSearch(ActionEvent event) {
        String search = textFieldAddProductSearch.getText();
        
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
     * Creates a unique ID for the new product.
     */
    public void createUniqueID() {
        int newID = 1;
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == newID) {
                newID++;
            }
        }
        textfieldAddProductID.setText(String.valueOf(newID));
    }
}