package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jdbc.DBConnection;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * This is the controller for the landing screen. This is the first screen after logging in
 */
public class Landing implements Initializable {

    /**
     * stores all appointments for easy access throughout the program - static for use in other controllers
     */
    public static ObservableList <Appointment> fullAppointmentList = FXCollections.observableArrayList();
    /**
     * stores all customers for easy access throughout the program - static for use in other controllers
     */
    public static ObservableList <Customer> fullCustomerList = FXCollections.observableArrayList();
    /**
     * checks if it's the first initialization - sets the lists if it is
     */
    private static boolean firstInitialize = true;
    /**
     * label for the reminder for whether there's an upcoming appointment or not
     */
    public Label reminder;

    /**
     * Initializes the landing screen controller,
     * A lambda method is used here to check if there's an upcoming appointment efficiently
     * @param url the location/url for the fxml file
     * @param resourceBundle not used in this controller, but required for method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (firstInitialize) {
            firstInitialize = false;
            //set appointment list
            try {
                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select * from appointments;");
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {

                    Appointment apt = new Appointment(-1,null,null,null,null,null,null,-1,-1, -1);
                    apt.setAppointmentID(resultSet.getInt("Appointment_ID"));
                    apt.setTitle(resultSet.getString("Title"));
                    apt.setLocation(resultSet.getString("Location"));
                    apt.setType(resultSet.getString("Type"));
                    apt.setContactID(resultSet.getInt("Contact_ID"));
                    apt.setCustomerID(resultSet.getInt("Customer_ID"));
                    apt.setDescription(resultSet.getString("Description"));
                    apt.setStart(resultSet.getTimestamp("Start").toLocalDateTime());
                    apt.setEnd(resultSet.getTimestamp("End").toLocalDateTime());
                    apt.setUserID(resultSet.getInt("User_ID"));
                    fullAppointmentList.add(apt);

                }

                if (fullAppointmentList != null){

                    ObservableList<Appointment> filteredList = FXCollections.observableArrayList();
                    for (Appointment appointment : fullAppointmentList){
                        filteredList.add(appointment);
                    }

                    /**
                     * Lambda method here to quickly and efficiently check for upcoming appointments
                     */
                    filteredList.removeIf(appointment -> appointment.getStart().isBefore(LocalDateTime.now())
                            || appointment.getStart().isAfter(LocalDateTime.now().plusMinutes(15)));

                    if (!filteredList.isEmpty()){
                        String reminderMsg = "You have an upcoming appointment:\n";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        for (Appointment appointment : filteredList){
                            reminderMsg += "ID - " + appointment.getAppointmentID() + " - " + appointment.getTitle()
                                    + "\nDate/time: - " + appointment.getStart().format(formatter) + "\n" + "(" + ZoneId.systemDefault() + " time)";
                        }
                        reminder.setText(reminderMsg);
                        reminder.setTextFill(Color.RED);
                        Alert appointmentAlert = new Alert(Alert.AlertType.WARNING, "There's an appointment in less than 15 minutes", ButtonType.OK);
                        ButtonType response = appointmentAlert.showAndWait().orElse(ButtonType.OK);
                        if (ButtonType.OK.equals(response)) {

                            appointmentAlert.close();
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println("Failed to populate Appointments list");
            }
            //set customer list
            try {

                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select * from customers;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()) {

                    Customer customer = new Customer(-1, "", "", "", "", -1);

                    customer.setCustomerID(resultSet.getInt("Customer_ID"));
                    customer.setCustomerName(resultSet.getString("Customer_Name"));
                    customer.setAddress(resultSet.getString("Address"));
                    customer.setPostalCode(resultSet.getString("Postal_Code"));
                    customer.setPhone(resultSet.getString("Phone"));
                    customer.setDivisionID(resultSet.getInt("Division_ID"));

                    fullCustomerList.add(customer);

                }

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to populate Customer list");
            }

        }

    }
    /**
     * clears the customer list and reacquires it from the database to update it
     */
    public static void updateCustomersList(){
        fullCustomerList.clear();
        try {

            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select * from customers;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {

                Customer customer = new Customer(-1, "", "", "", "", -1);

                customer.setCustomerID(resultSet.getInt("Customer_ID"));
                customer.setCustomerName(resultSet.getString("Customer_Name"));
                customer.setAddress(resultSet.getString("Address"));
                customer.setPostalCode(resultSet.getString("Postal_Code"));
                customer.setPhone(resultSet.getString("Phone"));
                customer.setDivisionID(resultSet.getInt("Division_ID"));

                fullCustomerList.add(customer);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to populate Customer list");
        }
    }

    /**
     * clears the appointment list and reacquires it from the database to update it
     */
    public static void updateAppointmentsList(){
        fullAppointmentList.clear();
        try {
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select * from appointments;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Appointment apt = new Appointment(-1,null,null,null,null,null,null,-1,-1, -1);
                apt.setAppointmentID(resultSet.getInt("Appointment_ID"));
                apt.setTitle(resultSet.getString("Title"));
                apt.setLocation(resultSet.getString("Location"));
                apt.setType(resultSet.getString("Type"));
                apt.setContactID(resultSet.getInt("Contact_ID"));
                apt.setCustomerID(resultSet.getInt("Customer_ID"));
                apt.setDescription(resultSet.getString("Description"));
                apt.setStart(resultSet.getTimestamp("Start").toLocalDateTime());
                apt.setEnd(resultSet.getTimestamp("End").toLocalDateTime());
                apt.setUserID(resultSet.getInt("User_ID"));

                fullAppointmentList.add(apt);

            }

        } catch (SQLException e) {
            System.out.println("Failed to populate Appointments list");
        }
    }

    /**
     * goes to customer page
     * @param actionEvent when the "Customers" button is activated
     * @throws IOException if the .fxml file is missing
     */
    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customers.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Customers");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * goes to appointments page
     * @param actionEvent when the "Appointments" button is activated
     * @throws IOException if the .fxml file is missing
     */
    public void toAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Appointments.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Appointments");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * goes to reports page
     * @param actionEvent when the "Reports" button is activated
     * @throws IOException if the .fxml file is missing
     */
    public void toReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Reports.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * gives alert to confirm logout, then calls method to log out if yes
     * @param actionEvent when the "Logout" button is activated
     * @throws IOException if the .fxml file is missing
     */
    public void logOut(ActionEvent actionEvent) throws IOException {
        Alert logoutAlert = new Alert(Alert.AlertType.NONE, "Would you like to log out?", ButtonType.YES, ButtonType.NO);
        ButtonType response = logoutAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            toLogin(actionEvent);
        }

    }

    /**
     * returns to the login screen
     * @param actionEvent passed from the logOut method upon confirmation
     * @throws IOException if the .fxml file is missing
     */
    public void toLogin(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Exits/closes the program
     */
    public void exitProgram() {

        Login login = new Login();
        login.exitProgram();
    }

}
