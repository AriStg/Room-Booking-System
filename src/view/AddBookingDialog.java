package view;

import java.awt.*;
import javax.swing.*;

import org.jdatepicker.impl.*;
import java.util.*;

/**
 * implements objects specific to this JDialog
 */
public class AddBookingDialog extends MethodsDialog{
    private JPanel panel;
    private GridBagConstraints gbc;

    private JDatePickerImpl datePicker;
    private JButton addBookingButton;

    public AddBookingDialog(){
        super("Add Booking");

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
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void addComponents(){
        panel = getPanel();
        gbc = getGBC();

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(datePicker, gbc);

        //Add Booking Button
        addBookingButton = new JButton("Add Booking");

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(addBookingButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    public JButton getAddBookingButton(){
        return addBookingButton;
    }
    
    public JDatePickerImpl getDate(){
        return this.datePicker;
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