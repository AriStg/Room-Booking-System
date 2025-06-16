package model;

/**
 * extension of the Room class to specialize the laboratories
 */
public class LabRoom extends Room<Booking> {
    private boolean hasComputers;
    private boolean hasPowerOutlets;

    public LabRoom(String roomNumber, int capacity, boolean hasComputers, boolean hasPowerOutlets) {
        super(roomNumber, capacity, "Lab");
        this.hasComputers = hasComputers;
        this.hasPowerOutlets = hasPowerOutlets;
    }

    public boolean hasComputers() {
        return hasComputers;
    }

    public boolean hasPowerOutlets() {
        return hasPowerOutlets;
    }

    @Override
    public boolean isValidBooking(Booking booking) {
        int duration = booking.getEndingHour() - booking.getStartingHour();
        
        if(2 <= duration && duration <= 4 && duration % 2 == 0)
            return true;
            
        return false;
    }

    @Override
    public String toString(){
        return super.toString() + " " + hasComputers() + " " + hasPowerOutlets();
    }
}