package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Appointment class to store values from database
 * @author Derek Brown
 */
public class Appointment{
    /**
     * Stores Appointment ID
     */
    private int appointmentID;

    /**
     * Stores title for appointment
     */
    private String title;
    /**
     * Stores description for appointment
     */
    private String description;
    /**
     * Stores location for appointment
     */
    private String location;
    /**
     * Stores type for appointment
     */
    private String type;
    /**
     * Stores start date and time for appointment
     */
    private LocalDateTime start;
    /**
     * Stores end date and time for appointment
     */
    private LocalDateTime end;
    /**
     * Stores customer ID for appointment
     */
    private int customerID;
    /**
     * Stores user ID for appointment
     */
    private int userID;
    /**
     * Stores contact ID for appointment
     */
    private int contactID;

    /**
     * Constructor for Appointment class
     * @param appointmentID the appointmentID to set
     * @param title the title to set
     * @param description the description to set
     * @param location the location to set
     * @param type the type to set
     * @param start the start time to set
     * @param end the end time to set
     * @param customerID the customerID to set
     * @param contactID the contactID to set
     * @param userID the userID to set
     */
    public Appointment(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int contactID, int userID){
        setType(type);
        setLocation(location);
        setTitle(title);
        setStart(start);
        setAppointmentID(appointmentID);
        setDescription(description);
        setEnd(end);
        setUserID(userID);
        setCustomerID(customerID);
        setContactID(contactID);
    }

    /**
     * @return appointmentID for appointment
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID the appointmentID to set
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return title for appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description for appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return location for appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type for appointment
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start time for appointment
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * This method converts from UTC to local time then sets the value
     * @param start the start time to set
     */
    public void setStart(LocalDateTime start) {
        if (start != null){
            ZonedDateTime zonedDateTime;
            zonedDateTime = start.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            this.start = zonedDateTime.toLocalDateTime();
        }
        else{
            this.start = null;
        }
    }

    /**
     * @return end time for appointment
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * This method converts from UTC to local time then sets the value
     * @param end the end time to set
     */
    public void setEnd(LocalDateTime end) {
        if (end != null){
            ZonedDateTime zonedDateTime;
            zonedDateTime = end.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
            this.end = zonedDateTime.toLocalDateTime();
        }
        else{
            this.end = null;
        }
    }

    /**
     * @return customerID for appointment
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return userID for appointment
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contactID for appointment
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID the contactID to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
