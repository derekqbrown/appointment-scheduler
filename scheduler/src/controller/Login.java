package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jdbc.DBConnection;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

import static javafx.fxml.FXMLLoader.load;

/**
 * Controller for the Login screen
 * @author Derek Brown
 */
public class Login implements Initializable {
    /**
     * textfield to obtain username for login to application
     */
    public TextField usernameLogin;
    /**
     * textfield to obtain password for login to application
     */
    public TextField passwordLogin;
    /**
     * Label displaying message if password is empty
     */
    public Label invalidPassword;
    /**
     * Label displaying message if username is empty
     */
    public Label invalidUsername;
    /**
     * Label displaying message if username and password do not match user database
     */
    public Label invalidCredentials;

    /**
     * username of user logging in, static for use in other controllers
     */
    public static String username = "";
    /**
     * determines whether it's the first time running initializable method
     */
    public boolean firstInitialize = true;
    /**
     * Exit button - to exit program
     */
    public Button exitBtn;
    /**
     * button to attempt to log in
     */
    public Button loginBtn;
    /**
     * label for password (for translation to french)
     */
    public Label passwordLabel;
    /**
     * label for username (for translation to french)
     */
    public Label usernameLabel;
    /**
     * label for Login text (for translation to french)
     */
    public Label userLoginLabel;
    /**
     * label for the locale
     */
    public Label localeLbl;
    /**
     * label for the locale
     */
    public String exitMsg;

    /**
     * Initializes the Login controller
     * @param url the location/url for the fxml file
     * @param resourceBundle used for translating between languages
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        invalidCredentials.setVisible(false);
        invalidUsername.setVisible(false);
        invalidPassword.setVisible(false);
        String zoneString = ZoneId.systemDefault().toString();

        try {
            resourceBundle = ResourceBundle.getBundle( "resource", Locale.getDefault());
            String localeString = resourceBundle.getString("locale") + " " + zoneString;
            localeLbl.setText(localeString);
            userLoginLabel.setText(resourceBundle.getString("userlogin"));
            passwordLabel.setText(resourceBundle.getString("password"));
            passwordLogin.setPromptText(resourceBundle.getString("password"));
            usernameLabel.setText(resourceBundle.getString("username"));
            usernameLogin.setPromptText(resourceBundle.getString("username"));
            loginBtn.setText(resourceBundle.getString("login"));
            exitBtn.setText(resourceBundle.getString("exit"));
            exitMsg = resourceBundle.getString("exitMessage");
            invalidCredentials.setText(resourceBundle.getString("invCred"));
            invalidPassword.setText(resourceBundle.getString("invPass"));
            invalidUsername.setText(resourceBundle.getString("invUser"));

        }catch (MissingResourceException e){
            System.out.println("Missing resource bundle");
        }

//        usernameLogin.setText("test");
//        passwordLogin.setText("test");

        if(firstInitialize){
            firstInitialize = false;
            try {
                DBConnection.connection = DBConnection.getConnection();
                System.out.println("Successful connection");
            }
            catch (SQLException e) {
                System.out.println("Failed to connect - SQL error");
            }
            catch (ClassNotFoundException e) {
                System.out.println("Failed to connect - Class error");
            }
        }

    }

    /**
     * calls method to check credentials, logs the attempt with timestamp, and goes to landing screen if valid credentials
     * @param actionEvent from the "login" button
     * @throws IOException if the fxml file is not found
     * @throws SQLException if there is a problem retrieving the password and username due to SQL errors
     */
    public void login(ActionEvent actionEvent) throws IOException, SQLException {

        username = usernameLogin.getText();
        String password = passwordLogin.getText();

        boolean validLogin = checkLoginCredentials(username, password);
        loginLogger(username, validLogin);
        if (validLogin){
            toLanding(actionEvent);
        }

    }

    /**
     * Navigates to the landing page
     * @param actionEvent passed from other methods
     * @throws IOException if the fxml file is not found
     */
    public void toLanding(ActionEvent actionEvent) throws IOException {
        Parent root = load(Objects.requireNonNull(getClass().getResource("/view/Landing.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Welcome back " + username);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This logs the time and status of the login attempt in login_activity.txt (file is created if it doesn't already exist)
     * @param username the username provided by user
     * @param validLogin result of the credential check - true if the credentials are valid
     */
    private void loginLogger(String username, boolean validLogin) throws IOException {


        //create file if not present
        File file = new File("login_activity.txt");
        if (file.createNewFile()){
            System.out.println("login_activity file created\n");

        }
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);

        if (validLogin && fileWriter != null){
            fileWriter.append("User " + username + " logged in successfully on: " + new Date() + "\n");
            System.out.println("User " + username + " logged in successfully on: " + new Date());
        }
        else {
            if (username.length() < 1){
                username = "Unknown";
            }
            fileWriter.append("User " + username + " unsuccessfully attempted to log in on: " + new Date() + "\n");
            System.out.println("User " + username + " unsuccessfully attempted to log in on: " + new Date());
        }
        fileWriter.close();

    }

    /**
     * validates login credentials against user database
     * @param username the username provided by user
     * @param password the password provided by user
     * @return true if username and password are valid
     */
    private boolean checkLoginCredentials(String username, String password) throws SQLException {
        if (username.length() < 1){
            invalidUsername.setVisible(true);
            return false;
        }
        else if (password.length() < 1){
            invalidPassword.setVisible(true);;
            return false;
        }
        else{
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Password from users where User_Name = ?;");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                if (resultSet.getString("Password").equals(password)){
                    return true;

                }
                else {
                    System.out.println(resultSet.getString("Password"));
                }
            }
        }
        invalidCredentials.setVisible(true);
        return false;
    }

    /**
     * Exits the program - static to allow other controllers to use it
     */
    public void exitProgram(){
        if (exitMsg != null || exitMsg.equals("")){
            exitMsg = "Would you like to close the program?";
        }

        Alert exitAlert = new Alert(Alert.AlertType.NONE, exitMsg, ButtonType.YES, ButtonType.NO);
        ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            try {
                if (DBConnection.connection != null) {
                    DBConnection.connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Couldn't close connection?");
            }
            System.exit(0);
        }
    }

}
