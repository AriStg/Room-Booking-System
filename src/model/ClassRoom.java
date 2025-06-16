package model;

/**
 * extension of the Room class to specialize Classrooms
 */
public class ClassRoom extends Room<Booking> {
    private boolean hasBlackboard;
    private boolean hasProjector;

    public ClassRoom(String roomNumber, int capacity, boolean hasBlackboard, boolean hasProjector) {
        super(roomNumber, capacity, "Classroom");
        this.hasBlackboard = hasBlackboard;
        this.hasProjector = hasProjector;
    }

    public boolean hasBlackboard() {
        return hasBlackboard;
    }

    public boolean hasProjector() {
        return hasProjector;
    }

    @Override
    public boolean isValidBooking(Booking booking){
        int duration = booking.getEndingHour() - booking.getStartingHour();

        if(0 < duration && duration <= 8)
            return true;

        return false;
    }

    @Override
    public String toString(){
        return super.toString() + " " + hasBlackboard() + " " + hasProjector();
    }
}