package model;

/**
 * Data provider for Outsourced parts
 * extends the Part class
 * FUTURE ENHANCEMENT - Method that checks for spelling errors in company names
 *
 * @author Evan
 */
public class Outsourced extends Part {
    private String companyName;
    
    /**
     * Constructor for Outsourced parts
     * 
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName 
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id,name,price,stock,min,max);
        this.companyName = companyName;
    }
    
    /**
     * sets the Outsourced part's company name
     * 
     * @param companyName 
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * gets the Outsourced part's companyName variable
     * 
     * @return 
     */
    public String getCompanyName() {
        return companyName;
    }
}
