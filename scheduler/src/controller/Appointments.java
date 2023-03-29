package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Controller for the appointments screen
 * @author Derek Brown
 */
public class Appointments implements Initializable{
    /**
     * label for error message
     */
    public Label errorMessage;
    /**
     * table column for appointment ID
     */
    public TableColumn aptIDCol;
    /**
     * table column for appointment title
     */
    public TableColumn aptTitleCol;
    /**
     * table column for appointment description
     */
    public TableColumn aptDescCol;
    /**
     * table column for appointment location
     */
    public TableColumn aptLocCol;
    /**
     * table column for appointment contact
     */
    public TableColumn aptContactCol;
    /**
     * table column for appointment type
     */
    public TableColumn aptTypeCol;
    /**
     * table column for start date and time
     */
    public TableColumn aptStartCol;
    /**
     * table column for end date and time
     */
    public TableColumn aptEndCol;
    /**
     * table column for customer ID associated with the appointment
     */
    public TableColumn aptCustIDCol;
    /**
     * table column for user ID
     */
    public TableColumn aptUserIDCol;
    /**
     * the table displaying the appointments
     */
    public TableView aptsTable;
    /**
     * this button calls the method to change the table view to the week view
     */
    public RadioButton weekViewBtn;
    /**
     * this button calls the method to change the table view to the month view
     */
    public RadioButton monthViewBtn;
    /**
     * this button calls the method to change the table view to the default view
     */
    public RadioButton defaultViewBtn;
    /**
     * this list is used to filter the table results
     */
    public ObservableList <Appointment> aptsList;
    /**
     * this label displays the time zone for start and end times
     */
    public Label timeMsg;

    /**
     * Initializes the appointments controller
     * @param url the location/url for the fxml file
     * @param resourceBundle not used in this controller, but required for method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultViewBtn.setSelected(true);
        weekViewBtn.setSelected(false);
        monthViewBtn.setSelected(false);
        timeMsg.setText("*Times in " + ZoneId.systemDefault() + " timezone");

        aptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        aptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        aptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        aptUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptsTable.setItems(Landing.fullAppointmentList);

        errorMessage.setText("");

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
     * Navigates to the form to add a new appointment
     * @param actionEvent when the New Appointment button is activated
     * @throws IOException if the fxml file is not found
     */
    public void newAppointment(ActionEvent actionEvent) throws IOException {
        errorMessage.setText("");
        AptDetails.appointment = null;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AptDetails.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle("Create new Appointment");
        stage.setScene(new Scene(root, 475, 400));
        stage.show();
    }

    /**
     * Navigates to the form for modifying an appointment
     * @param actionEvent when the Modify Selected button is activated
     * @throws IOException if the fxml file is not found
     */
    public void modAppointment(ActionEvent actionEvent) throws IOException {
        if (aptsTable.getSelectionModel().getSelectedIndex() > -1){
            errorMessage.setText("");
            AptDetails.appointment = (Appointment) aptsTable.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AptDetails.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stage.centerOnScreen();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root));
            stage.show();
        }
        else {
            errorMessage.setText("No appointment selected");
        }

    }

    /**
     * deletes the selected appointment
     * @throws SQLException if unable to delete the appointment due to SQL errors
     */
    public void deleteAppointment() throws SQLException {
        errorMessage.setText("");

        Alert exitAlert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete this appointment?", ButtonType.YES, ButtonType.NO);
        ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            if (aptsTable.getSelectionModel().getSelectedItem() != null){
                Appointment selectedAppointment = ((Appointment) aptsTable.getSelectionModel().getSelectedItem());

                PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("delete from appointments where Appointment_ID = ?;");

                preparedStatement.setString(1, Integer.toString(selectedAppointment.getAppointmentID()));
                preparedStatement.executeUpdate();

                errorMessage.setText("Appointment cancelled:\nAppointment ID: " + selectedAppointment.getAppointmentID() + " - " + selectedAppointment.getType());
                Landing.fullAppointmentList.remove(selectedAppointment);
                aptsTable.setItems(Landing.fullAppointmentList);

                aptsTable.getSelectionModel().selectFirst();


            }
            else{
                errorMessage.setText("No appointment selected.");
            }

        }

    }

    /**
     * Exits the program
     */
    public void exitProgram() {
        errorMessage.setText("");
        Login login = new Login();
        login.exitProgram();
    }
    /**
     * sets the table to month view - showing all appointments within 1 week of current date
     */
    public void weekView() {
        errorMessage.setText("");
        monthViewBtn.setSelected(false);
        defaultViewBtn.setSelected(false);
        aptsList = FXCollections.observableArrayList();

        for (Appointment appointment : Landing.fullAppointmentList){
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusWeeks(1))){
                aptsList.add(appointment);
            }
        }

        aptsTable.setItems(aptsList);
    }
    /**
     * sets the table to month view - showing all appointments within 1 month of current date
     */
    public void monthView() {
        errorMessage.setText("");
        weekViewBtn.setSelected(false);
        defaultViewBtn.setSelected(false);
        aptsList = FXCollections.observableArrayList();

        for (Appointment appointment : Landing.fullAppointmentList){
            if (appointment.getStart().isAfter(LocalDateTime.now()) && appointment.getStart().isBefore(LocalDateTime.now().plusMonths(1))){
                aptsList.add(appointment);
            }
        }

        aptsTable.setItems(aptsList);
    }

    /**
     * sets the table to default view - showing all appointments
     */
    public void toDefaultView() {
        errorMessage.setText("");
        monthViewBtn.setSelected(false);
        weekViewBtn.setSelected(false);
        aptsTable.setItems(Landing.fullAppointmentList);
    }

}
