package model;

import java.util.ArrayList;
import java.util.List;

/**
 * implements the rooms which implement a List of bookings and all the methods to work on said bookings.
 * also implements a generic
 */
public abstract class Room<E extends Booking>{
    private String ID;
    private int capacity;
    private String type;

    private List<E> bookings;

    public Room(String ID, int capacity, String type) {
        this.ID = ID;
        this.capacity = capacity;
        this.type = type;
        this.bookings = new ArrayList<>();
    }

    public String getRoomID(){
        return this.ID;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public String getType(){
        return this.type;
    }

    /**
     * getter for the bookings list
     * 
     * @return the List of the bookings with its information
     */
    public List<E> getBookings(){
        return this.bookings;
    }

    /**
     * abstract method that checks if a booking is valid
     * 
     * @param booking booking to be checks
     * @return true if valid, else false
     */
    public abstract boolean isValidBooking(E booking);

    /**
     * checks if the booking is overlapping with another one
     * 
     * @param booking the booking to check for
     * @return true if there's an overlap, else false
     */
    public boolean isOverlapping(E booking){
        for(E tempBooking : getBookings()){
            if(tempBooking.getDate().equals(booking.getDate()) &&
                booking.getStartingHour() < tempBooking.getEndingHour() &&
                booking.getEndingHour() > tempBooking.getStartingHour())
                return true;
        }
        
        return false;
    }

    /**
     * checks if the new booking overlaps with any booking based on if the new ending hour is greater or not, if the date is different check as if it was new
     * 
     * @param oldBooking booking to be edited
     * @param newBooking the new booking
     * @return true if there's any overlap with another booking, false if there's no overlap and it can be edited
     */
    public boolean verifyOverlappings(E oldBooking, E newBooking){
        if(!newBooking.getDate().equals(oldBooking.getDate()))
            return isOverlapping(newBooking);
        
        //if the ending time is less or equal, no checks needed
        if(newBooking.getEndingHour() <= oldBooking.getEndingHour() &&
            newBooking.getStartingHour() == oldBooking.getStartingHour())
            return false;
        
        if(newBooking.getEndingHour() > oldBooking.getEndingHour() &&
            newBooking.getStartingHour() == oldBooking.getStartingHour()){
                for(E booking : getBookings()){
                    if(!booking.equals(oldBooking) && 
                        booking.getDate().equals(newBooking.getDate())&&
                        booking.getStartingHour() < newBooking.getEndingHour()){
                        return true;
                        }
                }
            }
            
        return false;
    }

    /**
     * adds the booking to the list if it passes the validity checks
     * @param booking
     */
    public boolean addBooking(E booking){
        if(isValidBooking(booking) && !isOverlapping(booking)){
            getBookings().add(booking);
            return true;
        }

        return false;
    }

    /**
     * edits a booking with a new one
     * 
     * @param oldBooking the booking to be replaced
     * @param newBooking the new booking
     */
    public boolean editBooking(String roomType, E oldBooking, E newBooking){
        if(getBookings().contains(oldBooking) && isValidBooking(newBooking) && !verifyOverlappings(oldBooking, newBooking))
            return true;
        
        return false;
    }

    /**
     * if the booking exists, delete it
     * @param booking
     * @return
     */
    public boolean deleteBooking(E booking){
        if(getBookings().contains(booking) && booking != null){
            getBookings().remove(booking);
            return true;
        }

        return false;
    }

    @Override
    public String toString(){
        return getRoomID() + " " + getCapacity() + " " + getType();
    }
}