package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Data provider for the entire inventory
 * holds lists of all parts and products
 * FUTURE ENHANCEMENT - A method for displaying all parts and products
 * 
 * @author Evan
 */
public class Inventory {
    
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    /**
     * Adds a part to the allParts list
     * 
     * @param newPart 
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    /**
     * Adds a product to the allProducts list
     * 
     * @param newProduct 
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    /**
     * Searches for a part with a given ID and returns it
     * 
     * @param partID
     * @return 
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }
    
    /**
     * Searches for a product with a given ID and returns it
     * 
     * @param productID
     * @return 
     */
    public static Product lookupProduct(int productID) {
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }
    
    /**
     * Searches for a part with a given name and returns it
     * 
     * @param partName
     * @return 
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        
        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }
    
    /**
     * Searches for a product with a given name and returns it
     * 
     * @param productName
     * @return 
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        
        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                matchingProducts.add(product);
            }
        }
        return matchingProducts;
    }
    
    /**
     * Sets a part in allParts at a given index to the selectedPart parameter
     * 
     * @param index
     * @param selectedPart 
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    /**
     * Sets a product in allProducts at a given index to the selectedProduct parameter
     * 
     * @param index
     * @param selectedProduct 
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    
    /**
     * deletes a part from the allParts list
     * returns true if successful, false if not
     * 
     * @param selectedPart
     * @return 
     */
    public static boolean deletePart (Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else
            return false;
    }
    
    /**
     * deletes a product from the allProducts list
     * returns true if successful, false if not
     * 
     * @param selectedProduct
     * @return 
     */
    public static boolean deleteProduct (Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else
            return false;
    }
    
    /**
     * Returns the allParts list
     * 
     * @return 
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    /**
     * returns the allProducts list
     * 
     * @return 
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}