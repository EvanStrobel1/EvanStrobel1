/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195assessmentstrobel;

import helper.JDBC;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Evan
 */
public class C195AssessmentStrobel extends Application {

    /**
     * Starts and shows the main form FXML document
     * 
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //connect to the database before calling JavaFX launch(args)
        JDBC.openConnection();
        
        //Locale.setDefault(Locale.FRANCE);
        
        //ResourceBundle rb = ResourceBundle.getBundle("locale/Nat",Locale.getDefault());
        
        //DEBUG
        System.out.println(ZoneId.systemDefault());
        
        launch(args);
    }
    
}
