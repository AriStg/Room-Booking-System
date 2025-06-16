package view;

import model.Room;
import model.Booking;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.*;

/**
 * Sets the List with the updated one from the model and updates the table to show the bookings based on the chose date
 */
public class MyTable extends AbstractTableModel {
    private final String[] hours = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"};
    private List<String> roomNames;
    private List<Room<Booking>> rooms;
    private LocalDate currentDate;

    public MyTable(List<String> roomNames) {
        if (roomNames == null || roomNames.isEmpty()) {
            throw new IllegalArgumentException("Room names cannot be null or empty.");
        }

        this.roomNames = roomNames;
        this.rooms = new ArrayList<>();
        this.currentDate = null;
    }

    /**
     * @return returns the List from this class
     */
    public List<Room<Booking>> getRooms(){
        return this.rooms;
    }

    /**
     * updates the List with the updated bookings
     * 
     * @param rooms the new List with the rooms and bookings
     */
    public void setRooms(List<Room<Booking>> rooms){
        this.rooms.clear();
        this.rooms.addAll(rooms);
    }

    /**
     * @return returns the chosen date from the view panel's combo box
     */
    public LocalDate getCurrentDate(){
        return this.currentDate;
    }

    /**
     * returns number of rows
     */
    @Override
    public int getRowCount() {
        return hours.length;
    }

    /**
     * returns number of columns + 1 (column for the hours)
     */
    @Override
    public int getColumnCount() {
        return roomNames.size() + 1;
    }

    /**
     * returns the column of the room
     */
    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Time";
        }
        
        return roomNames.get(col - 1);
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return hours[row];
        }
    
        for (Room<Booking> room : getRooms()){
            if(roomNames.get(col - 1).equals(room.getRoomID()))
                for(Booking booking : room.getBookings())
                    if(booking.getDate().equals(getCurrentDate()) && checkHour(booking, hours[row]))
                        return booking.getName() + " - " + booking.getReason();
        }

        return "";
    }

    /**
     * checks whether a given hour falls within the time range of a booking.
     * 
     * @param booking the booking to check
     * @param hour the hour to be checked
     * @return true if the hour falls within the booking's starting and ending hour, false otherwise
     */
    public boolean checkHour(Booking booking, String hour){
        int currentHour = Integer.parseInt(hour.split(":")[0]);
        return currentHour >= booking.getStartingHour() && currentHour < booking.getEndingHour();
    }

    /**
     * changes the bookings to show on the table based on the date passed from the JComboBox in ViewBookingsPanel.java
     * 
     * @param date the date to show bookings for
     */
    public void setDate(LocalDate date) {
        this.currentDate = date;
    }
}