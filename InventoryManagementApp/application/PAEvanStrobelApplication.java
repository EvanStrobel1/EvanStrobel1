/**
 * Program controls the initialization and launching of other controllers
 * 
 * @author Evan Strobel
 * @version 1.0
 */
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

/**
 * JAVADOC files are located in .../PAEvanStrobel/dist/javadoc
 * @author Evan
 */
public class PAEvanStrobelApplication extends Application{
    /**
     * Starts and shows the main form FXML document
     * 
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Initializes any testing or starting data then launches the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //TEST DATA 
        Part testPart = new Outsourced(1, "test part", 9.99, 4, 0, 10, "testco");
        Inventory.addPart(testPart);
        Product testProduct = new Product(1, "test product", 10.00, 8, 10, 0);
        testProduct.addAssociatedPart(testPart);
        Inventory.addProduct(testProduct);
        
        launch(args);
    }
}