package controller;

import model.*;
import view.*;


import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;

import javax.swing.*;

/**
 * implements a controller that calls the methods from the model and view packages
 */
public class BookingController implements ActionListener {
    private MenuFrame frame;
    private RoomManager model;

    private ViewBookingsPanel viewP;
    

    public BookingController(MenuFrame frame, RoomManager model) {
        //initialize objects to call the other packages
        this.frame = frame;
        this.model = model;

        //initialize the panel
        this.viewP = frame.getViewPanel();
        
        //make the JMenuItems initialize the dialogs
        this.frame.getAddButton().addActionListener(e -> showAddDialog(e));
        this.frame.getEditButton().addActionListener(e -> showEditDialog(e));
        this.frame.getDeleteButton().addActionListener(e -> showDeleteDialog(e));

        //add an action listener to the JMenuItems
        this.frame.getPrintButton().addActionListener(e -> printBookings(e));
        this.frame.getSaveButton().addActionListener(e -> saveHandler(e));
        this.frame.getLoadButton().addActionListener(e -> loadHandler(e));
        this.frame.getAutoSaveDirButton().addActionListener(e -> changeAutoSaveFile(e));
    }


    /**
     * calls the model to print the bookings as a pdf
     * 
     * @param e
     */
    public void printBookings(ActionEvent e) {
        MyFileChooser fileChooser = new MyFileChooser("Choose PDF file name");
        String pdfFilePath = fileChooser.printBookings(fileChooser.showSaveDialog(this.frame), viewP.getTable());

        if(pdfFilePath != null)
            JOptionPane.showMessageDialog(frame, "PDF printed at: " + pdfFilePath, "Success", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(frame, "Failed to print bookings.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * calls the model to save the bookings to a file
     * 
     * @param e
     */
    public void saveHandler(ActionEvent e) {
        MyFileChooser saveFileChooser = new MyFileChooser("Save bookings");

        String filePath = saveFileChooser.getSaveFilePath(saveFileChooser.showSaveDialog(this.frame));
        
        if (filePath != null){
            model.saveBookings(filePath);
            JOptionPane.showMessageDialog(frame, "The file has been saved!.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Save operation canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * calls the model to load the bookings from a file
     * @param e
     */
    public void loadHandler(ActionEvent e){
        MyFileChooser loadFileChooser = new MyFileChooser("Load bookings");
        String filePath = loadFileChooser.getLoadFilePath(loadFileChooser.showOpenDialog(this.frame));

        if(filePath != null){
            model.roomLoader(filePath);
            emptyAllDates();
            this.viewP.getMyTable().setRooms(model.getRooms());

            for(LocalDate date : model.getBookingDates()){
                addDateToComboBox(date);
            }

            this.viewP.refreshTable();
            JOptionPane.showMessageDialog(frame, "The file has been loaded!.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else{
            JOptionPane.showMessageDialog(frame, "Load operation canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * calls the model to change the directory in which the tempFile is going to be saved in through the automatic saving system
     * 
     * @param e
     */
    public void changeAutoSaveFile(ActionEvent e){
        MyFileChooser fileChooser = new MyFileChooser("Change tempFile dir");
        String filePath = fileChooser.changeTempFileDir(fileChooser.showOpenDialog(this.frame));

        if(filePath != null){
            model.setTempFile(filePath);
            JOptionPane.showMessageDialog(frame, "The temporary file's name/directory has been changed!.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else{
            JOptionPane.showMessageDialog(frame, "Operation canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * creates a temporary instance of AddBookingDialog and calls the actual addBooking method
     * 
     * @param e
     */
    private void showAddDialog(ActionEvent e){
        AddBookingDialog addDialog = new AddBookingDialog();
        
        addDialog.setBounds(500, 500, 480, 300);
        addDialog.getAddBookingButton().addActionListener(d -> addBooking(d, addDialog));
        addDialog.setVisible(true);
    }

    /**
     * calls the model to add a new booking and calls the view to update the table to show it
     * 
     * @param e
     * @param addDialog the instance of AddBookingDialog to be used each item the add JMenuItem is pressed
     */
    public void addBooking(ActionEvent e, AddBookingDialog addDialog) {
        Date tmpDate = (Date) addDialog.getDate().getModel().getValue();
        String roomName = (String) addDialog.getRoomName().getSelectedItem();
        int startTime = (int) addDialog.getStartingTime().getSelectedItem();
        int endTime = (int) addDialog.getEndingTime().getSelectedItem();
        String bookerName = addDialog.getBookerName().getText();
        String bookingReason = addDialog.getBookingReason().getText();
    
        // Check if all boxes have been filled
        if (bookerName.isEmpty() || bookingReason.isEmpty() || tmpDate == null) {
            JOptionPane.showMessageDialog(addDialog, "Please fill in all the fields.", "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate date = convertToLocalDate(tmpDate);
        
        Booking booking = new Booking(date, startTime, endTime, bookerName, bookingReason);

        //roomName is the ID of the room
        boolean isBookingAdded = model.addBooking(roomName, booking);

        if(!isBookingAdded){
            JOptionPane.showMessageDialog(addDialog, "Failed to add booking.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //update all ComboBoxes in the View
        addDateToComboBox(date);

        //Update the table
        this.viewP.getMyTable().setRooms(model.getRooms());
        this.viewP.refreshTable();
    
        JOptionPane.showMessageDialog(addDialog, "Booking added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * creates a temporary instance of EditBookingDialog and calls the actual editBooking method
     * 
     * @param e
     */
    private void showEditDialog(ActionEvent e){
        EditBookingDialog editDialog = new EditBookingDialog();

        editDialog.setBounds(500, 500, 480, 425);
        editDialog.updateDateComboBox(viewP.getDateComboBox());
        editDialog.getEditBookingButton().addActionListener(d -> editBooking(d, editDialog));
        editDialog.setVisible(true);
    }
    
    /**
     * calls the model to edit the booking and calls the view to update the table
     * 
     * @param e
     * @param editDialog the instance of EditBookingDialog to be used each item the add JMenuItem is pressed
     */
    public void editBooking(ActionEvent e, EditBookingDialog editDialog) {
        LocalDate oldDate = editDialog.getOldDate();
        String roomType = (String) editDialog.getRoomType().getSelectedItem();
        String roomName = (String) editDialog.getRoomName().getSelectedItem();
        int startTime = (int) editDialog.getStartingTime().getSelectedItem();
        int oldEndTime = (int) editDialog.getEndingTime().getSelectedItem();
        String oldBookerName = editDialog.getBookerName().getText();
        String oldBookingReason = editDialog.getBookingReason().getText();

        Date tmpNewDate = (Date) editDialog.getNewDate().getModel().getValue();
        int newEndTime = (int) editDialog.getNewEndingTime().getSelectedItem();
        String newBookerName = editDialog.getNewBookerName().getText();
        String newBookingReason = editDialog.getNewBookingReason().getText();

        //check if all boxes have been filled
        if (oldBookerName.isEmpty() || oldBookingReason.isEmpty()) {
            JOptionPane.showMessageDialog(editDialog, "Please fill in all the necessary fields.", "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate newDate = convertToLocalDate(tmpNewDate);
        if(newBookerName.isEmpty()) newBookerName = oldBookerName;
        if(newBookingReason.isEmpty()) newBookingReason = oldBookingReason;

        Booking oldBooking = new Booking(oldDate, startTime, oldEndTime, oldBookerName, oldBookingReason);
        Booking newBooking = new Booking(newDate, startTime, newEndTime, newBookerName, newBookingReason);
        
        //calls the method from the model to check if the new ending hour is correct, if not it gives a warning and returns
        if(roomType.equals("Classroom") && !model.verifyCorrectTimeChange(roomType, oldEndTime, newEndTime)){
            JOptionPane.showMessageDialog(editDialog, 
            "The ending time for a Classroom can only be increased by 1 and you can't book it for more than 8 hours.", "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        } else if(roomType.equals("Lab") && !model.verifyCorrectTimeChange(roomType, oldEndTime, newEndTime)){
            JOptionPane.showMessageDialog(editDialog, 
            "The ending time for a Laboratory can only be increased by 2 and you can't book it for more than 4 hours.", "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //roomName == id
        boolean isBookingEdited = model.editBooking(roomType, roomName, oldBooking, newBooking);

        if(!isBookingEdited){
            JOptionPane.showMessageDialog(editDialog, "Failed to edit Booking.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //update all ComboBoxes in the View
        addDateToComboBox(newDate);
        deleteDateFromComboBox(oldDate);

        //update the table
        this.viewP.getMyTable().setRooms(model.getRooms());
        this.viewP.refreshTable();

        editDialog.updateDateComboBox(viewP.getDateComboBox());

        JOptionPane.showMessageDialog(editDialog, "Booking edited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * creates a temporary instance of DeleteBookingDialog and calls the actual editBooking method
     * 
     * @param e
     */
    private void showDeleteDialog(ActionEvent e){
        DeleteBookingDialog deleteDialog = new DeleteBookingDialog();

        deleteDialog.setBounds(500, 500, 480, 300);
        deleteDialog.updateDateComboBox(viewP.getDateComboBox());
        deleteDialog.getDeleteBookingButton().addActionListener(d -> deleteBooking(d, deleteDialog));
        deleteDialog.setVisible(true);
    }

    /**
     * calls the model to delete the booking and calls the methods to update the view
     * 
     * @param e
     * @param deleteDialog the instance of DeleteBookingDialog to be used each item the add JMenuItem is pressed
     */
    public void deleteBooking(ActionEvent e, DeleteBookingDialog deleteDialog) {
        LocalDate date = deleteDialog.getDate();
        String roomName = (String) deleteDialog.getRoomName().getSelectedItem();
        int startTime = (int) deleteDialog.getStartingTime().getSelectedItem();
        int endTime = (int) deleteDialog.getEndingTime().getSelectedItem();
        String bookerName = deleteDialog.getBookerName().getText();
        String bookingReason = deleteDialog.getBookingReason().getText();

        //check if all boxes have been filled
        if (bookerName.isEmpty() || bookingReason.isEmpty()) {
            JOptionPane.showMessageDialog(deleteDialog, "Please fill in all the fields.", "Error.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Booking booking = new Booking(date, startTime, endTime, bookerName, bookingReason);

        //roomName == id
        boolean isBookingDeleted = model.deleteBooking(roomName, booking);

        if(!isBookingDeleted){
            JOptionPane.showMessageDialog(deleteDialog, "Failed to delete booking.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //update all ComboBoxes in the View
        deleteDateFromComboBox(date);

        //update the table
        this.viewP.getMyTable().setRooms(model.getRooms());
        this.viewP.refreshTable();
        deleteDialog.updateDateComboBox(viewP.getDateComboBox());

        JOptionPane.showMessageDialog(deleteDialog, "Booking deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Checks if a date is present in the Map, if not it adds it
     * 
     * @param date the date to add if it's not already added
     */
    public void addDateToComboBox(LocalDate date){
        boolean existsDate = false;
        JComboBox<LocalDate> dateBox = this.viewP.getDateComboBox();

        for (int i = 0; i < dateBox.getItemCount(); i++) {
            LocalDate existingDate = dateBox.getItemAt(i);
            if(existingDate.isEqual(date))
                existsDate = true;
        }

        if (!existsDate)
            this.viewP.updateAvailableDates(date, "add");
    }

    /**
     * deletes the date if there are no remaining bookings for it
     * 
     * @param date the date to delete
     */
    public void deleteDateFromComboBox(LocalDate date){
        boolean isDatePresent = false;

        for(Room<Booking> room : model.getRooms())
            for(Booking booking : room.getBookings())
                if(booking.getDate().equals(date))
                    isDatePresent = true;

        if(!isDatePresent)
            this.viewP.updateAvailableDates(date, "del");
    }

    /**
     * empties all the combo boxes with the dates
     */
    public void emptyAllDates(){
        this.viewP.getDateComboBox().removeAllItems();
    }

    /**
     * Converts the date from Date type to LocalDate
     * 
     * @param date date to be converted
     * @return the date converted to LocalDate
     */
    public LocalDate convertToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    

    @Override
    public void actionPerformed(ActionEvent e){}
}