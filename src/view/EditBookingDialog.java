package view;

import java.awt.*;
import javax.swing.*;

import org.jdatepicker.impl.*;
import java.time.LocalDate;
import java.util.*;

/**
 * implements objects specific to this JDialog
 */
public class EditBookingDialog extends MethodsDialog{
    private JPanel panel;
    private GridBagConstraints gbc;

    private JComboBox<LocalDate> oldDateBox;

    private JLabel newTimeLabel;
    private JComboBox<Integer> newEndTimeBox;

    private JLabel newBookerLabel;
    private JTextField newName;
    private JLabel newReasonLabel;
    private JTextField newReason;

    private JLabel newDaTextField;
    private JDatePickerImpl newDatePicker;
    
    private JButton editBookingButton;

    public EditBookingDialog(){
        super("Edit Booking");

        initializeDatePicker();

        addComponents();
    }

    /**
     * initializes the date picker to show a calendar to choose a date from
     */
    private void initializeDatePicker(){
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        Calendar calendar = Calendar.getInstance();
        model.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        model.setSelected(true);

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        newDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void addComponents(){
        panel = getPanel();
        gbc = getGBC();

        this.oldDateBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(oldDateBox, gbc);
        
        this.newDaTextField = new JLabel("New date:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(newDaTextField, gbc);

        gbc.gridx++;
        panel.add(newDatePicker, gbc);

        newTimeLabel = new JLabel("New ending time:");
        Integer[] timeEnd = new Integer[10];
        for(int i = 0; i < 10; i++)
            timeEnd[i] = i+9;
        newEndTimeBox = new JComboBox<>(timeEnd);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(newTimeLabel, gbc);
        gbc.gridx++;
        panel.add(newEndTimeBox, gbc);

        //Booked by?
        newBookerLabel = new JLabel("New name:");
        newName = new JTextField();
        //Reason
        newReasonLabel = new JLabel("new Reason:");
        newReason = new JTextField();

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(newBookerLabel, gbc);
        gbc.gridx++;
        panel.add(newName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(newReasonLabel, gbc);
        gbc.gridx++;
        panel.add(newReason, gbc);


        editBookingButton = new JButton("Edit Booking");
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(editBookingButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    public JComboBox<Integer> getNewEndingTime(){
        return newEndTimeBox;
    }

    public JTextField getNewBookerName(){
        return newName;
    }

    public JTextField getNewBookingReason(){
        return newReason;
    }

    /**
     * updates the combo box with the dates
     * 
     * @param dateBox dates to be added in the date box in this dialog
     */
    public void updateDateComboBox(JComboBox<LocalDate> dateBox){
        if(this.oldDateBox != null)
            this.oldDateBox.removeAllItems();

        for(int i = 0; i < dateBox.getItemCount(); i++)
            this.oldDateBox.addItem(dateBox.getItemAt(i));
    }

    public JComboBox<LocalDate> getOldDateBox(){
        return this.oldDateBox;
    }

    public LocalDate getOldDate(){
        return (LocalDate) this.oldDateBox.getSelectedItem();
    }

    public JDatePickerImpl getNewDate(){
        return newDatePicker;
    }

    public JButton getEditBookingButton(){
        return editBookingButton;
    }

    public JComboBox<Integer> getStartingTime(){
        return super.getStartingTime();
    }

    public JComboBox<Integer> getEndingTime(){
        return super.getEndingTime();
    }

    public JTextField getBookerName(){
        return super.getBookerName();
    }

    public JTextField getBookingReason(){
        return super.getBookingReason();
    }

    public JComboBox<String> getRoomType(){
        return super.getRoomType();
    }

    public JComboBox<String> getRoomName(){
        return super.getRoomName();
    }
}