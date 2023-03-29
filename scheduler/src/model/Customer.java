package model;

import jdbc.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Customer class to store values from database
 * @author Derek Brown
 */
public class Customer{
    /**
     * the customer ID for the customer
     */
    private int customerID;
    /**
     * the name of the customer
     */
    private String customerName;
    /**
     * the address for the customer
     */
    private String address;
    /**
     * the postal code for the customer
     */
    private String postalCode;
    /**
     * the phone number for the customer
     */
    private String phone;
    /**
     * the division ID associated with the customer
     */
    private String divisionID;
    /**
     * the country for the customer
     */
    private String country;

    /**
     * constructor
     * @param customerID the customerID to set
     * @param customerName the customerName to set
     * @param address the address to set
     * @param postalCode the postalCode to set
     * @param phone the phone to set
     * @param divisionID the divisionID to set
     */
    public Customer (int customerID, String customerName, String address, String postalCode, String phone, int divisionID) {
        setDivisionID(divisionID);
        setPhone(phone);
        setCustomerName(customerName);
        setCustomerID(customerID);
        setAddress(address);
        setPostalCode(postalCode);

    }

    /**
     * get the customerID
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * set the customerID
     * @param customerID the customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * get the customerName
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * set the customerName
     * @param customerName the name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * get the address
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * set the address
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * get the postalCode
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * set the postalCode
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * get the phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set the phone number
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * get the divisionID
     * @return divisionID
     */
    public String getDivisionID() {
        return divisionID;
    }

    /**
     * set the divisionID
     * @param divisionID the divisionID to set
     */
    public void setDivisionID(int divisionID) {
        setCountry(divisionID);

        try{
            PreparedStatement preparedStatement = DBConnection.connection.prepareStatement("select Division from first_level_divisions where Division_ID = ?;");
            preparedStatement.setString(1, Integer.toString(divisionID));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                this.divisionID= resultSet.getString("Division");
            }
        }catch (SQLException e){
            System.out.println("Couldn't get division names");
            this.divisionID = "";
        }


//        this.divisionID = divisionID;
    }

    /**
     * get the country
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * set the country
     * @param divisionID the division ID used to look up the Country to set
     */
    public void setCountry(int divisionID) {
        String country = "UK";
        if (divisionID < 55){
            country = "U.S";
        }
        else if (divisionID < 100){
            country = "Canada";
        }

        this.country = country;

    }
}
