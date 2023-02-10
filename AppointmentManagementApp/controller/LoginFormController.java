package controller;

import helper.JDBC;
import helper.Localization;
import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

/**
 * Controller for the Login Form.
 * @author Evan
 */
public class LoginFormController implements Initializable {

    ResourceBundle rb;
    PrintWriter outputFile;
    
    @FXML
    private Button buttonLogIn;

    @FXML
    private Label labelLocation;

    @FXML
    private TextField textfieldPassword;

    @FXML
    private TextField textfieldUserID;
    
    @FXML
    private Label labelPassword;

    @FXML
    private Label labelUserID;

    /**
     * Initializes the Login form
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            String filename = "login_activity.txt";
            //File file = new File(filename);
            FileWriter fWriter = new FileWriter(filename, true);
            outputFile = new PrintWriter(fWriter);
        }
        catch (IOException ioexception) {
            
        }
        
        
        labelLocation.setText(ZoneId.systemDefault().toString());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            System.out.println("locale is french");
            labelUserID.setText(rb.getString("User") + " ID");
            labelPassword.setText(rb.getString("Password"));
            buttonLogIn.setText(rb.getString("Login"));
        }
    }
    
    /**
     * Checks if the user's login credentials are correct
     * If correct, switches to the Appointments form
     * If not, displays a localized error message 
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    void onAction_buttonLogIn(ActionEvent event) throws SQLException, IOException {
        String enteredUsername = textfieldUserID.getText();
        String enteredPassword = textfieldPassword.getText();
        
        String sql = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, enteredUsername);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getString("Password").equals(enteredPassword)) {
                //correct username and password
                int userID = rs.getInt("User_ID");
                User.setCurrentUser(userID);
                User.setCurrentUserName(rs.getString("User_Name"));
                
                outputFile.println("SUCCESSFUL LOGIN | USER ID " + userID + " | " + LocalDateTime.now());
                outputFile.close();
                
                Appointment.showAppointmentsOfUserWithin15(rs.getInt("User_ID"));
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/AppointmentForm.fxml"));
                loader.load();
                AppointmentFormController appointmentFormController = loader.getController();

                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
                return;
            }
        }
        //incorrect
        //make an alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (Locale.getDefault().getLanguage().equals("fr")) {
            rb = Localization.getResourceBundle();
            alert.setTitle(rb.getString("Error"));
            alert.setContentText(rb.getString("Incorrect Username and/or Password."));
            
            // LOCALIZE THIS !
            outputFile.println("UNSUCCESSFUL LOGIN | " + LocalDateTime.now());
        }
        else {
            alert.setTitle("Error");
            alert.setContentText("Incorrect Username and/or Password.");
            
            outputFile.println("UNSUCCESSFUL LOGIN | " + LocalDateTime.now());
        }
        outputFile.close();
        Optional<ButtonType> result = alert.showAndWait();
    }
}