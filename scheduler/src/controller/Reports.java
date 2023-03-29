package controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jdbc.DBConnection;
import model.Appointment;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.ResourceBundle;

/**
 * Controller for the Reports screen
 * @author Derek Brown
 */
public class Reports implements Initializable {
    /**
     * the table column for the appointment ID
     */
    public TableColumn aptIDCol;
    /**
     * the table column for the appointment title
     */
    public TableColumn aptTitleCol;
    /**
     * the table column for the appointment description
     */
    public TableColumn aptDescCol;
    /**
     * the table column for the appointment type
     */
    public TableColumn aptTypeCol;
    /**
     * the table column for the appointment start date
     */
    public TableColumn aptStartCol;
    /**
     * the table column for the appointment end date
     */
    public TableColumn aptEndCol;
    /**
     * the table column for the appointment customer ID
     */
    public TableColumn aptCustIDCol;
    /**
     * the table to display appointments based on contact selected
     */
    public TableView reportTable;

    /**
     * textarea to display the count of appointments by Month - Type
     */
    public TextArea monthTypeField;
    /**
     * combobox populated with the contact names
     */
    public ComboBox contactCB;

    /**
     * label for the third report to display the average number of appointments per customer
     */
    public Label avgAptsLabel;

    /**
     * Initializes the Reports controller,
     * Lambda is used here in the change listener for the contact combobox to update the table efficiently based on the selected contact
     * @param url for the fxml file 
     * @param resourceBundle for the fxml file
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set combobox items
        try {
            contactCB.setItems(populateContactCB());
        } catch (SQLException e) {
            System.out.println("Unable to populate Contact combobox");
        }

        aptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        aptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        aptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        reportTable.setItems(firstReport());

        /**
         * Lambda method here to implement a change listener for effective and quick filtering of the combobox
         */
        contactCB.valueProperty().addListener((ChangeListener<String>) (observableValue, o, t1) -> reportTable.setItems(firstReport()));

        secondReport();
        thirdReport();

    }

    /**
     * This gets the list of contacts for the combobox
     * @return List of contacts
     */
    private ObservableList <String> populateContactCB() throws SQLException {
        ObservableList <String> contactNameList = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Contact_Name from contacts;");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            contactNameList.add(resultSet.getString("Contact_Name"));
        }
        contactCB.setValue(contactNameList.get(0));
        return contactNameList;

    }

    /**
     * returns to the landing page
     * @param actionEvent when the back button is activated
     * @throws IOException if the fxml file is not found
     */
    public void toLanding(ActionEvent actionEvent) throws IOException {
        Login login = new Login();
        login.toLanding(actionEvent);
    }

    /**
     * generates the list to populate the appointment table for first report based on the contact ID
     * @return a list with the contact ID of the selected name
     */
    public ObservableList<Appointment> firstReport() {
        //lists appointments based on contact
        ObservableList <Appointment> listByContact = FXCollections.observableArrayList();
        try{
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Contact_ID from contacts where Contact_Name = ?;");
            preparedStatement.setString(1, contactCB.getValue().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            int contactID = -1;
            while(resultSet.next()){
                contactID = resultSet.getInt("Contact_ID");
            }
            for (Appointment appointment : Landing.fullAppointmentList){
                if (contactID == appointment.getContactID()){
                    listByContact.add(appointment);
                }
            }
        }catch (SQLException e){

        }


        return listByContact;
    }

    /**
     * generates the report listing the count by Month and Type
     */
    public void secondReport() {
        //* these lists hold values for the report
        ObservableList <Month> monthsList = FXCollections.observableArrayList();
        ObservableList <String> typesList = FXCollections.observableArrayList();

        String monthTypeCount = "";
        for(int i = 1; i <= 12; i++){
            monthsList.add(Month.of(i));
        }
        for(Appointment appointment : Landing.fullAppointmentList){
            if (!typesList.contains(appointment.getType())){
                typesList.add(appointment.getType());
            }
        }
        int count = 0;
        for(Month month : monthsList){
            for (String type : typesList) {
                for (Appointment appointment : Landing.fullAppointmentList){
                    if (appointment.getStart().getMonth().equals(month) && appointment.getType().contains(type)){
                        count++;
                    }

                }

                if (count != 0){
                    monthTypeCount += month + " - " + type + " - " + count + "\n";
                    count = 0;
                }

            }
        }
        monthTypeField.setText(monthTypeCount);

    }

    /**
     * This method calculates the average number of appointments per customer
     */
    public void thirdReport() {
        //list user appointments

        if (Landing.fullCustomerList.size() > 0 && Landing.fullAppointmentList.size() > 0) {
            int aptSize = Landing.fullAppointmentList.size();
            int custSize = Landing.fullCustomerList.size();
            Double avg = (Double.valueOf(aptSize)/Double.valueOf(custSize));
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            decimalFormat.setRoundingMode(RoundingMode.UP);

            avgAptsLabel.setText(decimalFormat.format(avg));
        }
        else{
            String msg ="";
            if (Landing.fullCustomerList.size() < 1){
                msg += "No customers in list\n";
            }
            if (Landing.fullAppointmentList.size() < 1){
                msg += "No appointments in list\n";
            }
            avgAptsLabel.setText(msg);
        }

    }

}
