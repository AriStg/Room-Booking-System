package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * implementation of the bookings containing important informations like date, time, name
 */
public class Booking implements Serializable{
    private LocalDate date;
    private int startingHour;
    private int endingHour;
    private String name;
    private String reason;

    /**
     * constructor to initialize the bookings informations
     * 
     * @param date the date of the booking
     * @param startingHour the starting hour
     * @param endingHour the ending time
     * @param name the name of the booker
     * @param reason the reason for the booking
     */
    public Booking(LocalDate date, int startingHour, int endingHour, String name, String reason){
        this.date = date;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.name = name;
        this.reason = reason;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public int getStartingHour() {
        return this.startingHour;
    }

    public int getEndingHour() {
        return this.endingHour;
    }

    public String getName() {
        return this.name;
    }

    public String getReason() {
        return this.reason;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Booking)) return false;
        Booking other = (Booking) obj;
        return this.startingHour == other.startingHour &&
               this.endingHour == other.endingHour &&
               this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return getDate() + " " + getStartingHour() + " " + getEndingHour() + " " + getName() + " " + getReason();
    }
}