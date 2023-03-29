package controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jdbc.DBConnection;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Controller for the CustomerDetails screen
 * @author Derek Brown
 */
public class CustomerDetails implements Initializable {
    /**
     * this holds the values for the customer to update
     */
    public static Customer customer;
    /**
     * Label for displaying an error/status message
     */
    public Label errorMessage;
    /**
     * boolean will be true if updating a customer
     */
    public static boolean update = false;

    /**
     * TextField for the Division ID
     */
    public ComboBox <String> divisionCB;
    /**
     * Combobox for the country ID
     */
    public ComboBox <String> countryCB;
    /**
     * TextField for the customer Name
     */
    public TextField customerNameField;
    /**
     * TextField for the address
     */
    public TextField addressField;
    /**
     * TextField for the postal code
     */
    public TextField postalCodeField;
    /**
     * TextField for the phone number
     */
    public TextField phoneNumberField;
    /**
     * TextField for the customer ID
     */
    public TextField customerIDField;

    /**
     * Initializes the controller for the Customer details class,
     * Lambda used in countryCB changeListener to quickly filter the division combobox at the time of selection.
     * @param url the location/url for the fxml file
     * @param resourceBundle not used in this controller, but required for method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");

        populateCountryCB();
        populateDivisionCB();

        if (customer != null){
            update = true;

            customerNameField.setText(customer.getCustomerName());
            customerIDField.setText(Integer.toString(customer.getCustomerID()));
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            phoneNumberField.setText(customer.getPhone());
            countryCB.setValue(customer.getCountry());
            divisionCB.setValue(customer.getDivisionID());
        }

        /**
         * Lambda method here to implement a change listener for effective and quick filtering of the combobox
         */
        countryCB.valueProperty().addListener((ChangeListener<String>) (observableValue, o, t1) -> divisionCB.setItems(filterDivCB()));


    }
    private ObservableList <String> filterDivCB(){
        ObservableList <String> filteredList = FXCollections.observableArrayList();

        try {
            int countryID = -1;
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Country_ID from countries where Country = ?;");
            preparedStatement.setString(1, countryCB.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                countryID = resultSet.getInt("Country_ID");
            }

            preparedStatement = DBConnection.connection.prepareStatement("select Division from first_level_divisions where Country_ID = ?;");
            preparedStatement.setString(1, Integer.toString(countryID));
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                filteredList.add(resultSet.getString("Division"));
            }
            return filteredList;
        } catch (SQLException e) {
            System.out.println("Unable to update Division Combobox");
        }
        return null;
    }
    /**
     * this populates the division combobox
     */
    private void populateDivisionCB() {
        try {
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Division from first_level_divisions order by Division asc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> divisionList = FXCollections.observableArrayList();
            while(resultSet.next()){
                divisionList.add(resultSet.getString("Division"));
            }
            divisionCB.setItems(divisionList);
        } catch (SQLException e) {
            System.out.println("Unable to populate Division Combobox");
        }
    }

    /**
     * this populates the country combobox
     */
    private void populateCountryCB() {
        try {
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Country from countries order by Country asc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> countryList = FXCollections.observableArrayList();
            while(resultSet.next()){
                countryList.add(resultSet.getString("Country"));
            }
            countryCB.setItems(countryList);
        } catch (SQLException e) {
            System.out.println("Unable to populate Country Combobox");
        }
    }

    /**
     * Saves the customer to database
     * @param actionEvent when the button is activated
     */
    public void saveCustomer(ActionEvent actionEvent) throws IOException {

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String divID = "";
        try {
            preparedStatement = DBConnection.connection.prepareStatement("select Division_ID from first_level_divisions where Division = ?;");
            preparedStatement.setString(1, divisionCB.getValue());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int divIDInt = resultSet.getInt("Division_ID");
                divID = Integer.toString(divIDInt);
            }
        }catch(SQLException e){
            System.out.println("Failed to get division ID from combobox");
        }

        if(checkFields()){
            try{
                if (update){
                    preparedStatement = DBConnection.connection.prepareStatement("update customers set Customer_Name = ?, "
                            + "Address = ?, Postal_Code = ?, Phone = ?, Last_Update = CURRENT_TIMESTAMP, "
                            + "Last_Updated_By = ?, Division_ID = ? where Customer_ID = ?");

                    preparedStatement.setString(1, customerNameField.getText());
                    preparedStatement.setString(2, addressField.getText());
                    preparedStatement.setString(3, postalCodeField.getText());
                    preparedStatement.setString(4, phoneNumberField.getText());
                    preparedStatement.setString(5, Login.username);
                    preparedStatement.setString(6, divID);
                    preparedStatement.setString(7, customerIDField.getText());

                    preparedStatement.executeUpdate();
                    errorMessage.setText("Customer updated");
                    System.out.println("Updated customer successfully");

                }
                else{
                    preparedStatement = DBConnection.connection.prepareStatement("insert into customers "
                            + "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) "
                            + "Values (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?)");

                    preparedStatement.setString(1, customerNameField.getText());
                    preparedStatement.setString(2, addressField.getText());
                    preparedStatement.setString(3, postalCodeField.getText());
                    preparedStatement.setString(4, phoneNumberField.getText());
                    preparedStatement.setString(5, Login.username);
                    preparedStatement.setString(6, Login.username);
                    preparedStatement.setString(7, divID);

                    preparedStatement.executeUpdate();
                    errorMessage.setText("Customer created");
                    System.out.println("Inserted customer successfully. ");


                }
                Landing.updateCustomersList();
                Landing.updateAppointmentsList();
                Landing landing = new Landing();

                landing.toCustomers(actionEvent);
            }
            catch (SQLException e){
                errorMessage.setText("Customer could not be saved - SQL error");
                System.out.println("Failed to save customer. ");
            }
        }
    }

    /**
     * @return true if all fields are valid
     */
    public boolean checkFields(){
        String errorList = "";

        if (customerNameField == null || customerNameField.getText().length() < 1){
            errorList += "Invalid Customer Name. ";
        }
        if (addressField.getText().length() < 1 || addressField.getText() == null){
            errorList += "Invalid Address. ";
        }
        if (postalCodeField.getText().length() < 1 || postalCodeField.getText() == null){
            errorList += "Invalid Postal Code. ";
        }
        if (phoneNumberField.getText().length() < 1 || phoneNumberField.getText() == null){
            errorList += "Invalid Phone Number. ";
        }
        if (countryCB.getValue() == null){
            errorList += "Invalid Country. ";
        }
        if (divisionCB.getValue() == null){
            errorList += "Invalid First Level Division. ";
        }
        else{
            //this section checks whether the selected division id is within the country selected
            try{
                int countryID = -1;
                boolean validDivision = false;

                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Country_ID from first_level_divisions where Division = ?;");
                preparedStatement.setString(1, divisionCB.getValue());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    countryID = resultSet.getInt("Country_ID");
                }

                preparedStatement = DBConnection.connection.prepareStatement("select Country from countries where Country_ID = ?;");
                preparedStatement.setString(1, Integer.toString(countryID));
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    if (countryCB.getValue().contains(resultSet.getString("Country"))){
                        validDivision = true;
                    }
                }

                if (!validDivision){
                    errorList += "Division " + divisionCB.getValue() + " is not located in " + countryCB.getValue() + ". ";
                }
            } catch (SQLException e){
                errorList += "Division ID check failed - SQL error. ";
                System.out.println("Division ID check failed - SQL error. ");
            }

        }

        if (!errorList.equals("")){
            errorList += "Unable to save customer.";
            errorMessage.setText(errorList);
            return false;
        }

        return true;
    }

    /**
     * this returns to customer page
     * @param actionEvent when cancel/back button is activated
     * @throws IOException if .fxml file is missing
     */
    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.NONE, "Return to Customers? (Any unsaved information will be lost)", ButtonType.YES, ButtonType.NO);
        ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            Landing landing = new Landing();
            landing.toCustomers(actionEvent);
        }



    }
}
