package view;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements shared fields and labels between other JDialogs
 */
abstract class MethodsDialog extends JDialog{
    private JPanel panel;
    private GridBagConstraints gbc;

    private JLabel dateLabel;
    
    private JLabel roomTypeLabel;
    private JComboBox<String> roomTypeBox;
    private JComboBox<String> roomBox;
    
    private JLabel timeLabel;
    private JComboBox<Integer> startTimeBox;
    private JComboBox<Integer> endTimeBox;
    
    private JLabel bookerLabel;
    private JTextField name;
    private JLabel reasonLabel;
    private JTextField reason;

    public MethodsDialog(String title){
        super((Frame) null, title, true);
        setLayout(new BorderLayout());

        add(createControlPanel(), BorderLayout.NORTH);
    }

    private JPanel createControlPanel() {
        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        //Room Type Selection (Classroom or Lab)
        roomTypeLabel = new JLabel("Room:");
        roomTypeBox = new JComboBox<>(new String[]{"Classroom", "Lab"});
        roomTypeBox.addActionListener(e -> updateRoomSelection(e));
        roomBox = new JComboBox<>();

        //Date selection
        dateLabel = new JLabel("Date:");

        //Booking Time
        timeLabel = new JLabel("Time (start-end):");

        Integer[] timeStart = new Integer[10];
        Integer[] timeEnd = new Integer[10];
        for(int i = 0; i < 10; i++){
            timeStart[i] = i+8;
            timeEnd[i] = i+9;
        }

        startTimeBox = new JComboBox<>(timeStart);
        endTimeBox = new JComboBox<>(timeEnd);

        //Booked by?
        bookerLabel = new JLabel("Booker's name:");
        name = new JTextField();

        //Reason
        reasonLabel = new JLabel("Reason:");
        reason = new JTextField();

        //row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(dateLabel, gbc);

        //row 2
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(roomTypeLabel, gbc);
        gbc.gridx++;
        panel.add(roomTypeBox, gbc);
        gbc.gridx++;
        panel.add(roomBox, gbc);

        //row 3
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(timeLabel, gbc);
        gbc.gridx++;
        panel.add(startTimeBox, gbc);
        gbc.gridx++;
        panel.add(endTimeBox, gbc);

        //row 4
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(bookerLabel, gbc);
        gbc.gridx++;
        panel.add(name, gbc);

        //row 5
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(reasonLabel, gbc);
        gbc.gridx++;
        panel.add(reason, gbc);

        updateRoomSelection(null);
        return panel;
    }

    public GridBagConstraints getGBC(){
        return gbc;
    }

    public JPanel getPanel(){
        return panel;
    }

    public JComboBox<Integer> getStartingTime(){
        return startTimeBox;
    }

    public JComboBox<Integer> getEndingTime(){
        return endTimeBox;
    }

    public JTextField getBookerName(){
        return name;
    }

    public JTextField getBookingReason(){
        return reason;
    }

    public JComboBox<String> getRoomType(){
        return roomTypeBox;
    }

    public JComboBox<String> getRoomName(){
        return roomBox;
    }

    /**
     * Differentiates the rooms from the combo box and gives those of the
     * specific chosen type
     * 
     * @param type type of the room chosen
     * @return
     */
    private List<String> getRoomsByType(String type) {
        List<String> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[2].trim().equalsIgnoreCase(type)) {
                    rooms.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Updates the rooms to choose from based on the desired type of room
     */
    private void updateRoomSelection(ActionEvent e){
        roomBox.removeAllItems();
        String selectedType = (String) roomTypeBox.getSelectedItem();
        List<String> availableRooms = getRoomsByType(selectedType);
        for (String room : availableRooms) {
            roomBox.addItem(room);
        }
    }
}
