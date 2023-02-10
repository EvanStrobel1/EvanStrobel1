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
 * FXML Controller class
 * FUTURE ENHANCEMENT - Buttons to clear all parts or products
 *
 * @author Evan Strobel
 */
public class MainFormController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private Button btnAddPart;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Button btnDeletePart;

    @FXML
    private Button btnDeleteProduct;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnModifyPart;

    @FXML
    private Button btnModifyProduct;
    
    @FXML
    private TableView<Part> tableParts;

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Part, Integer> tablecolumnPartID;

    @FXML
    private TableColumn<Part, Integer> tablecolumnPartInv;

    @FXML
    private TableColumn<Part, String> tablecolumnPartName;

    @FXML
    private TableColumn<Part, Double> tablecolumnPartPrice;

    @FXML
    private TableColumn<Product, Integer> tablecolumnProductID;

    @FXML
    private TableColumn<Product, Integer> tablecolumnProductInv;

    @FXML
    private TableColumn<Product, String> tablecolumnProductName;

    @FXML
    private TableColumn<Product, Double> tablecolumnProductPrice;
    
    @FXML
    private TextField textfieldSearchPart;

    @FXML
    private TextField textfieldSearchProduct;

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
        
        tableProducts.setItems(Inventory.getAllProducts());
        tablecolumnProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablecolumnProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tablecolumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablecolumnProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    /**
     * Logic for the Add Part button. Loads the Add Part Form and then sends the user to it
     * RUNTIME ERROR - getResource used the wrong file destination
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddPartForm.fxml"));
        loader.load();
        AddPartFormController addPartController = loader.getController();
        addPartController.createUniqueID();
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Logic for the Add Product button.
     * Loads the Add Product form then sends the user to it.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddProductForm.fxml"));
        loader.load();
        AddProductFormController addProductController = loader.getController();
        addProductController.createUniqueID();
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Logic for the Delete Part button
     * Shows a CONFIRMATION warning
     * removes the part from the parts list if the user clicks OK on the warning
     * 
     * @param event 
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Delete selected part?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part deletingPart = tableParts.getSelectionModel().getSelectedItem();
            Inventory.deletePart(deletingPart);
        }
    }

    /**
     * Logic for the Delete Product button
     * Shows a CONFIRMATION warning
     * removes the product from the products list if the user clicks OK on the warning
     * Cannot remove a product that has associated parts
     * RUNTIME ERROR - result variable null pointer exception, fixed by moving the result button check if statement to line 198 inside the size check if statement
     * 
     * @param event 
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result;
        
        if (selectedProduct != null) {
            if (selectedProduct.getAllAssociatedParts().size() <= 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Delete selected product?");
                result = alert.showAndWait();
                
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Product deletingProduct = tableProducts.getSelectionModel().getSelectedItem();
                    Inventory.deleteProduct(deletingProduct);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Cannot delete a product with associated parts.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Logic for the Exit button
     * Shows a CONFIRMATION warning then exits the program if the user clicks OK
     * 
     * @param event 
     */
    @FXML
    void onActionExit(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Exit program?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Logic for the Modify Part button
     * Loads the Modify Part form then sends the user to it
     * sends the selected part to the modify part controller
     * RUNTIME ERROR - file destination for the getResource command was incorrect
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException{
        
        Part selectedPart = tableParts.getSelectionModel().getSelectedItem();
        
        if (selectedPart != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
            loader.load();
            ModifyPartFormController modifyPartController = loader.getController();
            modifyPartController.sendPart(selectedPart);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a part to modify");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Logic for the Modify Product button
     * Loads the Modify Product form then sends the user to it
     * sends the selected product to the modify product controller
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException{
        
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();
        
        if (selectedProduct != null) {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
            loader.load();
            ModifyProductFormController modifyProductController = loader.getController();
            modifyProductController.sendProduct(selectedProduct);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product to modify");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Logic for the Parts search field
     * takes the users input and filters the parts table
     * 
     * @param event 
     */
    @FXML
    void onActionTextFieldSearchPart(ActionEvent event) {
        String search = textfieldSearchPart.getText();
        
        if (search.equals("")) {
            tableParts.setItems(Inventory.getAllParts());
        }
        else {
            
            ObservableList<Part> filteredParts = FXCollections.observableArrayList();
            for(Part part : Inventory.getAllParts()) {
                if(search.equals(Integer.toString(part.getId()))) {
                    filteredParts.add(part);
                }
                else if(part.getName().contains(search)) {
                    filteredParts.add(part);
                }
            }

            if (filteredParts.size() > 0) {
                tableParts.setItems(filteredParts);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nothing found");
                alert.setContentText("No parts found!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Logic for the Products search field
     * takes the users input and filters the products table
     * RUNTIME ERROR - product's ID was not converted to a string, fixed with Integer.toString
     * 
     * @param event 
     */
    @FXML
    void onActionTextFieldSearchProduct(ActionEvent event) {
        String search = textfieldSearchProduct.getText();
        
        if (search.equals("")) {
            tableProducts.setItems(Inventory.getAllProducts());
        }
        else {
            
            ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
            for(Product product : Inventory.getAllProducts()) {
                if(search.equals(Integer.toString(product.getId()))) {
                    filteredProducts.add(product);
                }
                else if(product.getName().contains(search)) {
                    filteredProducts.add(product);
                }
            }

            if (filteredProducts.size() > 0) {
                tableProducts.setItems(filteredProducts);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Nothing found");
                alert.setContentText("No products found!");
                alert.showAndWait();
            }
        }
    }
}