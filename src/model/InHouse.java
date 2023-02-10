/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/** 
 * Data provider for In House parts
 * extends the Part class
 * FUTURE ENHANCEMENT - Auto-generated machine IDs
 *
 * @author Evan
 */
public class InHouse extends Part {
    private int machineId;
    
    /**
     * Constructor for In House parts
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId 
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    /**
     * Setter for machineId
     * 
     * @param machineId 
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
    /**
     * Getter for machineId
     * 
     * @return 
     */
    public int getMachineId() {
        return machineId;
    }
}
