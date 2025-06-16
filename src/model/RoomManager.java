package model;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * the manager of the model package, creates the List whith the rooms and the bookings and every method called from the model is called in here or through here
 */
public class RoomManager extends Thread{
    private List<Room<Booking>> rooms;
    private String tempFile;

    public RoomManager(String classroomsFile){
        this.rooms = new ArrayList<>();
        this.tempFile = "tempFile";
        
        roomLoader(classroomsFile);
    }

    /**
     * @return returns the List with the rooms and bookings
     */
    public List<Room<Booking>> getRooms(){
        return this.rooms;
    }

    /**
     * loads the rooms and bookings from a file
     * 
     * @param classroomsFile the file from which to load
     */
    public void roomLoader(String classroomsFile){
        try {
            getRooms().clear();
            BufferedReader reader = new BufferedReader(new FileReader(classroomsFile));
            String line;
            Room<Booking> room = null;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String id = parts[0];

                if(id.length() == 2){
                    int capacity = Integer.parseInt(parts[1]);
                    String type = parts[2];

                    if(type.equals("Classroom"))
                        room = new ClassRoom(id, capacity, Boolean.parseBoolean(parts[3]), Boolean.parseBoolean(parts[4]));
                    else if(type.equals("Lab"))
                        room = new LabRoom(id, capacity, Boolean.parseBoolean(parts[3]), Boolean.parseBoolean(parts[4]));

                    getRooms().add(room);
                } else if(room != null){
                    LocalDate date = LocalDate.parse(parts[0]);
                    Booking booking = new Booking(date, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3], parts[4]);
                    room.getBookings().add(booking);
                }
            }

            reader.close();
        } catch (FileNotFoundException e) { 
            System.err.println("File not found: " + e.getMessage());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * saves the bookings in a file together with their respective rooms
     * 
     * @param filename the file to save to
     */
    public void saveBookings(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return;
        }
    
        File file = new File(filePath);
    
        // Ensure it has a ".txt" extension
        if (!filePath.endsWith(".txt")) {
            file = new File(filePath + ".txt");
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Room<Booking> room : getRooms()) {
                writer.write(room.toString());
                writer.newLine();
    
                for (Booking booking : room.getBookings()) {
                    writer.write(booking.toString());
                    writer.newLine();
                }
            }
            System.out.println("Bookings saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads only the names (the ids) of the rooms to show them on the first row of the table and on the combo boxes to choose the room
     * 
     * @param fileName file with the rooms to use
     * @return the List with the rooms' names
     */
    public List<String> loadRoomNames(String fileName){
        List<String> roomNames = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts[0].length() == 2)
                    roomNames.add(parts[0].trim());
            }

            reader.close();
        } catch(FileNotFoundException e){
            System.err.println("File not found");
        } catch(IOException e){
            e.printStackTrace();
        }

        return roomNames;
    }
    
    @Override
    public void run(){
        try{
            while(true){
                saveToTempFile();
                Thread.sleep(60000);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    /**
     * saves bookings to a temporary file automatically if the bookings (and rooms) aren't empty
     */
    private void saveToTempFile(){
        if(!getRooms().isEmpty() && !areBookingsEmpty())
            saveBookings(this.tempFile);
    }

    public void setTempFile(String newTempFile){
        this.tempFile = newTempFile;
    }

    /**
     * finds a room based on an id
     * 
     * @param id of the room
     * @return the room if it exists, else null
     */
    public Room<Booking> findRoom(String id){
        for(Room<Booking> room : getRooms())
            if(room.getRoomID().equals(id))
                return room;

        return null;
    }

    /**
     * checks that the new ending hour is withing the limits: +-1 if Classroom, +-2 if Lab
     * 
     * @param roomType type of the room to decide which check to use
     * @param oldEndingHour the previous ending time
     * @param newEndingHour the new ending time to check
     * @return true if the new ending time is correct, false otherwise
     */
    public boolean verifyCorrectTimeChange(String roomType, int oldEndingHour, int newEndingHour){
        if(roomType.equals("Classroom") && 
            ((newEndingHour < oldEndingHour-1) || (newEndingHour > oldEndingHour+1)))
            return false;
        if(roomType.equals("Lab") && 
            ((newEndingHour < oldEndingHour-2) || (newEndingHour > oldEndingHour+2)))
            return false;
        
        return true;
    }

    /**
     * calls the add method to add the new booking
     * 
     * @param id of the room to add the booking in
     * @param newBooking booking to be added
     * @return true if it's been added, false otherwise
     */
    public boolean addBooking(String id, Booking newBooking){
        Room<Booking> room = findRoom(id);

        if(room != null)
            return room.addBooking(newBooking);
        
        return false;
    }

    /**
     * calls the edit method in the Room class which will check if the booking can be edited, if yes: calls the delete and add methods
     * 
     * @param id id to find the room of the booking to edit
     * @param oldBooking booking to be replaced
     * @param newBooking the edited booking
     * @return true if it has been edited, false otherwise
     */
    public boolean editBooking(String roomType, String id, Booking oldBooking, Booking newBooking){
        Room<Booking> room = findRoom(id);

        if(room.editBooking(roomType, oldBooking, newBooking)){
            deleteBooking(id, oldBooking);
            addBooking(id, newBooking);
            
            return true;
        }

        return false;
    }

    /**
     * calls the delete method to delete the booking
     * 
     * @param id of the room to delete from
     * @param booking to be deleted
     * @return true if it's been deleted, false otherwise
     */
    public boolean deleteBooking(String id, Booking booking){
        Room<Booking> room = findRoom(id);

        if(room != null)
            return room.deleteBooking(booking);
        
        return false;
    }

    /**
     * gets all the dates that have bookings (only once, no duplicates)
     * 
     * @return the set with the dates
     */
    public Set<LocalDate> getBookingDates(){
        Set<LocalDate> set = new TreeSet<>();

        for(int i = 0; i < getRooms().size(); i++){
            List<Booking> bookings = getRooms().get(i).getBookings();

            for(Booking booking : bookings)
                set.add(booking.getDate());
        }

        return set;
    }

    /**
     * @return true if the List is empty, false otherwise
     */
    public boolean areBookingsEmpty(){
        for(Room<Booking> room : getRooms())
            if(!room.getBookings().isEmpty())
                return false;
        
        return true;
    }
}