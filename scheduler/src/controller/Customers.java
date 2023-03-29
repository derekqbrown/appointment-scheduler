package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jdbc.DBConnection;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller for the customers screen
 * @author Derek Brown
 */
public class Customers implements Initializable {
    /**
     * The table column for the customer name
     */
    public TableColumn custNameCol;
    /**
     * The table column for the customer ID
     */
    public TableColumn custIDCol;
    /**
     * The table column for the customer address
     */
    public TableColumn custAddressCol;
    /**
     * The table column for the customer postal code
     */
    public TableColumn custZipCol;
    /**
     * The table column for the customer phone
     */
    public TableColumn custPhoneCol;
    /**
     * The table column for the customer country
     */
    public TableColumn custCountryCol;
    /**
     * The table column for the customer first level division
     */
    public TableColumn custFirstLvlCol;
    /**
     * Table to display the customer information
     */
    public TableView custTable;
    /**
     * Label for the error/status message to display
     */
    public Label errorMessage;

    /** Initializes the Customers controller
     * @param url the location/url for the fxml file
     * @param resourceBundle not used in this controller, but required for method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");

        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custZipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custFirstLvlCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        custTable.setItems(Landing.fullCustomerList);

    }

    /**
     * Returns to the landing screen
     * @param actionEvent when the back button is activated
     * @throws IOException if the fxml file is not found
     */
    public void toLanding(ActionEvent actionEvent) throws IOException {
        errorMessage.setText("");
        Login login = new Login();
        login.toLanding(actionEvent);
    }

    /**
     * Navigates to the screen to add a new customer
     * @param actionEvent when the new customer button is activated
     * @throws IOException if the fxml file is not found
     */
    public void newCustomer(ActionEvent actionEvent) throws IOException {
        errorMessage.setText("");
        CustomerDetails.customer = null;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerDetails.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle("Create new Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Navigates to the screen to modify or update the selected appointment
     * @param actionEvent when the modify button is activated
     * @throws IOException if the fxml file is not found
     */
    public void modCustomer(ActionEvent actionEvent) throws IOException {
        if (custTable.getSelectionModel().getSelectedIndex() > -1){
            errorMessage.setText("");
            CustomerDetails.customer = (Customer) custTable.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomerDetails.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Customer");
            stage.setScene(new Scene(root));
            stage.show();
        }
        else {
            errorMessage.setText("No Customer selected");
        }
    }

    /**
     * Deletes the selected customer
     * @throws SQLException for deletion from database
     */
    public void deleteCustomer() throws SQLException {
        errorMessage.setText("");
        String delMsg = "Are you sure you want to delete this Customer?";
        String aptsToDel = "";
        Customer selectedCustomer = ((Customer) custTable.getSelectionModel().getSelectedItem());
        for(Appointment appointment : Landing.fullAppointmentList){
            if (appointment.getCustomerID() == selectedCustomer.getCustomerID()){
                aptsToDel += appointment.getTitle() + " - ID:" + appointment.getAppointmentID() + " | ";
            }
        }
        if (!aptsToDel.equals("")){
            delMsg += " This will also delete the following appointments:\n" + aptsToDel.substring(0, aptsToDel.length() - 3);
        }
        else{
            delMsg += " There are no appointments associated with this customer.";
        }

        if (custTable.getSelectionModel().getSelectedIndex() > -1){
            Alert exitAlert = new Alert(Alert.AlertType.NONE, delMsg, ButtonType.YES, ButtonType.NO);
            ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(response)) {


                //delete appointments
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("delete from appointments where Customer_ID = ?;");
                preparedStatement.setString(1, Integer.toString(selectedCustomer.getCustomerID()));
                preparedStatement.executeUpdate();
                //delete customer
                preparedStatement = DBConnection.connection.prepareStatement("delete from customers where Customer_ID = ?;");
                preparedStatement.setString(1, Integer.toString(selectedCustomer.getCustomerID()));
                preparedStatement.executeUpdate();

                Landing.updateCustomersList();
                Landing.updateAppointmentsList();

                custTable.setItems(Landing.fullCustomerList);

                errorMessage.setText("Customer and associated appointments have been deleted.");

            }
            else {
                errorMessage.setText("Deletion cancelled");
            }
        }
        else {
            errorMessage.setText("No customer selected");
        }


    }

    /**
     * exits the program
     */
    public void exitProgram() {
        errorMessage.setText("");
        Login login = new Login();
        login.exitProgram();
    }


}
