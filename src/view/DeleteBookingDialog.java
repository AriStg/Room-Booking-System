package view;

import java.awt.*;
import java.time.LocalDate;

import javax.swing.*;

/**
 * implements objects specific to this JDialog
 */
public class DeleteBookingDialog extends MethodsDialog{
    private JPanel panel;
    private GridBagConstraints gbc;

    private JComboBox<LocalDate> dateBox;
    private JButton deleteBookingButton;

    public DeleteBookingDialog(){
        super("Delete Booking");

        addComponents();
    }

    private void addComponents(){
        panel = getPanel();
        gbc = getGBC();


        this.dateBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(dateBox, gbc);

        deleteBookingButton = new JButton("Delete Booking");

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(deleteBookingButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    public JComboBox<LocalDate> getDateComboBox(){
        return this.dateBox;
    }

    /**
     * updates the combo box with the dates
     * 
     * @param dateBox dates to be added in the date box in this dialog
     */
    public void updateDateComboBox(JComboBox<LocalDate> dateBox){
        if(this.dateBox != null)
            this.dateBox.removeAllItems();

        for(int i = 0; i < dateBox.getItemCount(); i++)
            this.dateBox.addItem(dateBox.getItemAt(i));
    }

    public JComboBox<LocalDate> getDateBox(){
        return this.dateBox;
    }

    public LocalDate getDate(){
        LocalDate selectedDate = (LocalDate) this.dateBox.getSelectedItem();
        return selectedDate;
    }

    public JButton getDeleteBookingButton(){
        return deleteBookingButton;
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