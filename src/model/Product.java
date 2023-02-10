package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class for product objects
 * FUTURE ENHANCEMENT - A method to fill the associatedParts list with test data
 *
 * @author Evan
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    /**
     * Constructor for products
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param max
     * @param min 
     */
    public Product(int id, String name, double price, int stock, int max, int min) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.max = max;
        this.min = min;
    }
    
    /**
     * Sets the product's id
     * 
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Sets the product's name
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * sets the product's price
     * 
     * @param price 
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * sets the product's stock
     * 
     * @param stock 
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    /**
     * sets the product's minimum
     * 
     * @param min 
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * sets the product's max
     * 
     * @param max 
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * gets the product's id
     * 
     * @return 
     */
    public int getId() {
        return id;
    }
    
    /**
     * gets the product's name
     * 
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * gets the product's price
     * 
     * @return 
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * gets the product's stock
     * 
     * @return 
     */
    public int getStock() {
        return stock;
    }
    
    /**
     * gets the product's min
     * 
     * @return 
     */
    public int getMin() {
        return min;
    }
    
    /**
     * gets the product's max
     * 
     * @return 
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Adds a part to the product's associated parts list
     * 
     * @param part 
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    /**
     * deletes an associated part from the product
     * returns true if successful, false if not
     * RUNTIME ERROR - Did not return true after removing the part
     * 
     * @param selectedAssociatedPart
     * @return 
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        //return true if deletion was successful, otherwise false
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else
            return false;
    }
    
    /**
     * returns the product's associated parts list
     * 
     * @return 
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}