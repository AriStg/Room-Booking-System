package mainFolder;

import model.*;
import view.*;
import controller.*;

import java.util.List;

/**
 * @author 187565
 * 
 * The main class from where the program starts
 */
public class MainClass {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //initialize the model
        RoomManager model = new RoomManager("Rooms.txt");

        //get the names of the room to initialize the frame
        List<String> roomNames = model.loadRoomNames("Rooms.txt");
        MenuFrame frame = new MenuFrame("Gestione Aule", roomNames);

        //start the automatic saving funcionality from the model
        model.start();

        //initialize the controller to control everything
        new BookingController(frame, model);
    }
}