package controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jdbc.DBConnection;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * The controller for AptDetails screen
 * @author Derek Brown
 */
public class AptDetails implements Initializable {
    /**
     * This is the appointment being modified. If creating a new appointment, this will be null (set in Appointments)
     */
    public static Appointment appointment;
    /**
     * String format for use with DateTimeFormatter class to format the start and end times
     */
    public final String TIME_FORMAT = "HH:mm:ss";
    /**
     * String format for use with DateTimeFormatter class to format the start and end date and times
     */
    public final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * This is the utc time zone ID for use in time zone conversions
     */
    public final ZoneId utcZoneID = ZoneId.of("UTC");
    /**
     * This is the eastern US time zone ID for use in time zone conversions
     */
    public final ZoneId etZoneID = ZoneId.of("US/Eastern");
    /**
     * this holds the ET values for the start times
     */
    public ObservableList <String> hoursList = FXCollections.observableArrayList();

    /**
     * this holds the ET values for the end times
     */
    public ObservableList <String> endHoursList = FXCollections.observableArrayList();
    /**
     * Stores localTimes after converting start times from hoursList to the user's time zone
     */
    public ObservableList <String> hoursUpdatedList = FXCollections.observableArrayList();
    /**
     * Stores localTimes after converting end times from endHoursList to the user's time zone
     */
    public ObservableList <String> endHoursUpdatedList = FXCollections.observableArrayList();

    /**
     * Set to true if updating an existing appointment
     */
    public static boolean update;
    /**
     * Stores the start date and time for the appointment (obtained from comboboxes)
     */
    public LocalDateTime startLDT;
    /**
     * Stores the end date and time for the appointment (obtained from comboboxes)
     */
    public LocalDateTime endLDT;

    /**
     * Label for the appointment end time
     */
    public Label endLabel;
    /**
     * Combobox for the appointment customer ID
     */
    public ComboBox custIDCB;
    /**
     * Combobox for the appointment user ID
     */
    public ComboBox userIDCB;
    /**
     * Combobox for the appointment contact
     */
    public ComboBox contactCB;
    /**
     * Combobox for the appointment start time
     */
    public ComboBox startTimeCB;
    /**
     * Combobox for the appointment end time
     */
    public ComboBox endTimeCB;
    /**
     * Textfield for the appointment ID
     */
    public TextField appointmentIDField;
    /**
     * Textfield for the appointment title
     */
    public TextField titleField;
    /**
     * Textfield for the appointment description
     */
    public TextField descriptionField;
    /**
     * Textfield for the appointment location
     */
    public TextField locationField;
    /**
     * Textfield for the appointment type
     */
    public TextField typeField;
    /**
     * Datepicker to pick the date for the appointment
     */
    public DatePicker datePickerField;

    /**
     * Label for error message to set if fields are invalid
     */
    public Label errorMessage;

    public Label startTimeLabel;
    /**
     * Initializes the controller for the appointment details form,
     * Lambda method is used in the changeListener for the startTimeCB to filter the endTimeCB times to values after selected time.
     * @param url the location/url for the fxml file
     * @param resourceBundle not used in this controller, but required for method
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update = false;
        endLabel.setText("");
        errorMessage.setText("");
        startTimeLabel.setText("Start Time (" + ZoneId.systemDefault() + " time)");

        populateTimeCB();
        populateContactCB();
        populateUserCB();
        populateCustomerCB();

        //set the appointment if applicable
        if (appointment != null){
            update = true;

            String startTime = (appointment.getStart().toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
            String endTime = (appointment.getEnd().toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));

            appointmentIDField.setText(Integer.toString(appointment.getAppointmentID()));
            userIDCB.setValue(appointment.getUserID());
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());
            locationField.setText(appointment.getLocation());
            contactCB.setValue(appointment.getContactID());
            typeField.setText(appointment.getType());
            custIDCB.setValue(appointment.getCustomerID());
            datePickerField.setValue(appointment.getStart().toLocalDate());
            startTimeCB.setValue(startTime);
            endTimeCB.setValue(endTime);
            endTimeCB.setItems(filteredEndTimes());
        }
        //lambda function used to filter the end time combobox quickly
        /**
         * Lambda method here to implement a change listener for effective and quick filtering of the endTimeCB combobox
         */
        startTimeCB.valueProperty().addListener((ChangeListener<String>) (observableValue, o, t1) -> endTimeCB.setItems(filteredEndTimes()));

    }

    private ObservableList filteredEndTimes() {
        ObservableList <String> filteredEndTimes = FXCollections.observableArrayList();
        int startIndex = startTimeCB.getSelectionModel().getSelectedIndex();
        for (int i = 0; i < endHoursUpdatedList.size(); i++){
            if (i >= startIndex){
                filteredEndTimes.add(endHoursUpdatedList.get(i));
            }
        }
        return filteredEndTimes;
    }

    /**
     * Populates the customer ID combobox
     */
    private void populateCustomerCB() {
        try{
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Customer_ID from customers order by Customer_ID asc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList <Integer> custIDList = FXCollections.observableArrayList();
            while(resultSet.next()){
                custIDList.add(resultSet.getInt("Customer_ID"));
            }
            custIDCB.setItems(custIDList);
        } catch (SQLException e){
            System.out.println("Could not import Customer ID list");
        }
    }
    /**
     * Populates the user ID combobox
     */
    private void populateUserCB() {
        try{
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select User_ID from users order by User_ID asc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList <Integer> userIDList = FXCollections.observableArrayList();
            while(resultSet.next()){
                userIDList.add(resultSet.getInt("User_ID"));
            }
            userIDCB.setItems(userIDList);
        } catch (SQLException e){
            System.out.println("Could not import user ID list");
        }
    }
    /**
     * Populates the contact ID combobox
     */
    private void populateContactCB() {
        try {
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Contact_ID from contacts order by Contact_ID asc;");
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList <Integer> contactIDList = FXCollections.observableArrayList();
            while(resultSet.next()){
                contactIDList.add(resultSet.getInt("Contact_ID"));
            }
            contactCB.setItems(contactIDList);

        } catch (SQLException e) {
            System.out.println("Could not import contact ID list");
        }
    }

    /**
     * Populates the start and end time comboboxes - in local time
     */
    private void populateTimeCB() {
        //this will convert the hours and endHours lists to local time
        LocalTime localTime = LocalTime.of(8, 0, 0);
        while(!localTime.equals(LocalTime.of(22, 0, 0))){
            hoursList.add(localTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
            localTime = localTime.plusMinutes(30);
        }
        endHoursList = FXCollections.observableArrayList(hoursList);
        endHoursList.remove(0);
        endHoursList.add("22:00:00");

        ZonedDateTime zonedDateTime;

        for (String time : hoursList){
            LocalDateTime localDateTime = LocalDateTime.parse("2000-01-01 " + time, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            zonedDateTime = localDateTime.atZone(etZoneID).withZoneSameInstant(ZoneId.systemDefault());
            hoursUpdatedList.add(zonedDateTime.toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        }
        for (String time : endHoursList){
            LocalDateTime localDateTime = LocalDateTime.parse("2000-01-01 " + time, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            zonedDateTime = localDateTime.atZone(etZoneID).withZoneSameInstant(ZoneId.systemDefault());
            endHoursUpdatedList.add(zonedDateTime.toLocalTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        }

        startTimeCB.setItems(hoursUpdatedList);
        endTimeCB.setItems(endHoursUpdatedList);

    }

    /**
     * Navigates back to the previous screen (Appointments)
     * @param actionEvent when the cancel/back button is activated
     * @throws IOException if the fxml file is not found
     */
    public void toAppointments(ActionEvent actionEvent) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.NONE, "Return to Appointments? (Any unsaved information will be lost)", ButtonType.YES, ButtonType.NO);
        ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            Landing landing = new Landing();
            landing.toAppointments(actionEvent);
        }
    }

    /**
     * Validates the fields in the Appointment form
     * @return true if all fields are valid
     */
    public boolean checkFields(){

        String errorList = "";
        if (userIDCB.getValue() == null){
            errorList += "Invalid User ID. ";
        }
        if (titleField.getText().length() < 1 || titleField.getText() == null){
            errorList += "Invalid Title. ";
        }
        if (descriptionField.getText().length() < 1 || descriptionField.getText() == null){
            errorList += "Invalid Description. ";
        }
        if (locationField.getText().length() < 1 || locationField.getText() == null){
            errorList += "Invalid Location. ";
        }
        if (contactCB.getValue() == null){
            errorList += "Invalid Contact ID. ";
        }
        if (custIDCB.getValue() == null){
            errorList += "Invalid Customer ID. ";
        }
        if (typeField.getText().length() < 1 || typeField.getText() == null){
            errorList += "Invalid Type. ";
        }
        if (datePickerField.getValue() == null ){
            errorList += "Invalid Date. ";
        }
        if (startTimeCB.getValue() == null){
            errorList += "Invalid Start Time. ";

        }
        else {

            try {
                //converts end time from system default timezone to utc
                startLDT = LocalDateTime.parse(datePickerField.getValue().toString() + " " + startTimeCB.getValue().toString(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            }
            catch (DateTimeException e){
                System.out.println("Failed to parse start time to local time zone");
                errorList += "Failed to parse start time. ";
            }
        }

        if (endTimeCB.getValue() == null || endTimeCB.getValue().equals(startTimeCB.getValue())){
            errorList += "Invalid End Time. ";
        }
        else{
            try {
                //converts end time from system default timezone to utc
                endLDT = LocalDateTime.parse(datePickerField.getValue().toString() + " " + endTimeCB.getValue().toString(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            }
            catch (DateTimeException e){
                System.out.println("Failed to parse end time to local time zone");
                errorList += "Failed to parse end time. ";
            }
            if (endLDT.isBefore(startLDT)){
                //increments the end date by 1 day if the appointment time crosses midnight (ie. earlier time, but a higher index)
                if (endTimeCB.getSelectionModel().getSelectedIndex() < startTimeCB.getSelectionModel().getSelectedIndex()){
                    endLDT = endLDT.plusDays(1);
                }
                else{
                    errorList += "End time is before start time. ";
                }
            }

            if (checkOverlap(startLDT, endLDT)){
                errorList += "Times overlap with existing appointments. ";
            }

            try {
                //converts start time from system default timezone to utc
                ZonedDateTime zonedDateTime = startLDT.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZoneID);
                startLDT = zonedDateTime.toLocalDateTime();
            }
            catch (DateTimeException e){
                System.out.println("Failed to convert start time to local time zone");
                errorList += "Failed to convert start time. ";
            }
            try {
                //converts end time from system default timezone to utc
                ZonedDateTime zonedDateTime = endLDT.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZoneID);
                endLDT = zonedDateTime.toLocalDateTime();
            }
            catch (DateTimeException e){
                System.out.println("Failed to convert end time to local time zone");
                errorList += "Failed to convert end time. ";
            }
        }


        if (errorList != ""){
            errorList += "Unable to save appointment.";
            errorMessage.setText(errorList);
            return false;
        }
        return true;

    }

    /**
     * This method checks for overlap with existing appointments
     * @param start the start time of new appointment
     * @param end the end time of new appointment
     */
    private boolean checkOverlap(LocalDateTime start, LocalDateTime end){
        String cid = custIDCB.getValue().toString();
        String aptCid = "";
        int nAptID = -1;
        if (update){
            nAptID = Integer.parseInt(appointmentIDField.getText());
        }
        int aptID;

        for(Appointment apt : Landing.fullAppointmentList){
            aptCid = Integer.toString(apt.getCustomerID());
            aptID = apt.getAppointmentID();

            LocalDateTime aptSt = apt.getStart();
            LocalDateTime aptEnd = apt.getEnd();

            if(cid.equals(aptCid) && !(nAptID == aptID)){
                if (start.isEqual(aptEnd) || end.isEqual(aptSt)){
                    return false;
                }
                if(start.isAfter(aptSt) && start.isBefore(aptEnd) || start.isEqual(aptSt)){
                    return true;
                }
                if(end.isAfter(aptSt) && end.isBefore(aptEnd) || end.isEqual(aptEnd)){
                    return true;
                }
                if(start.isBefore(aptSt) && end.isAfter(aptEnd)){
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Saves the appointment to database
     * @param actionEvent When the Save button is activated
     */
    public void saveApt(ActionEvent actionEvent) throws IOException {

        PreparedStatement preparedStatement;

        if(checkFields()){

            try{
                if (update){
                    preparedStatement = DBConnection.connection.prepareStatement("update appointments set Title = ?, Description = ?, "
                            + "Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, "
                            + "Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ? where Appointment_ID = ?");

                    preparedStatement.setString(1, titleField.getText());
                    preparedStatement.setString(2, descriptionField.getText());
                    preparedStatement.setString(3, locationField.getText());
                    preparedStatement.setString(4, typeField.getText());
                    preparedStatement.setString(5, startLDT.toString());
                    preparedStatement.setString(6, endLDT.toString());
                    preparedStatement.setString(7, custIDCB.getValue().toString());
                    preparedStatement.setString(8, userIDCB.getValue().toString());
                    preparedStatement.setString(9, contactCB.getValue().toString());
                    preparedStatement.setString(10, Login.username);
                    preparedStatement.setString(11, appointmentIDField.getText());

                    preparedStatement.executeUpdate();
                    errorMessage.setText("Successfully inserted appointment");
                    System.out.println("Successfully updated appointment");

                }
                else{
                    preparedStatement = DBConnection.connection.prepareStatement("insert into appointments (Title, Description, Location, Type, Start, End, "
                            + "Customer_ID, User_ID, Contact_ID, Last_Update, Last_Updated_By, Created_By, Create_Date)"
                            + " Values (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, CURRENT_TIMESTAMP);");
                    preparedStatement.setString(1, titleField.getText());
                    preparedStatement.setString(2, descriptionField.getText());
                    preparedStatement.setString(3, locationField.getText());
                    preparedStatement.setString(4, typeField.getText());
                    preparedStatement.setString(5, startLDT.toString());
                    preparedStatement.setString(6, endLDT.toString());
                    preparedStatement.setString(7, custIDCB.getValue().toString());
                    preparedStatement.setString(8, userIDCB.getValue().toString());
                    preparedStatement.setString(9, contactCB.getValue().toString());
                    preparedStatement.setString(10, Login.username);
                    preparedStatement.setString(11, Login.username);

                    preparedStatement.executeUpdate();
                    errorMessage.setText("Successfully inserted appointment");
                    System.out.println("Successfully inserted appointment");

                }

                Landing.updateAppointmentsList();
                Landing landing = new Landing();

                landing.toAppointments(actionEvent);
            }
            catch (SQLException e){
                System.out.println("SQL error - ");
            }
        }
    }
}
