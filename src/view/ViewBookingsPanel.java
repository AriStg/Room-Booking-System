package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

/**
 * creates the panel which will show the booking tables
 */
public class ViewBookingsPanel extends JPanel {
    private JTable table;
    private MyTable myTable;

    private JComboBox<LocalDate> dateBox;

    /**
     * This constructor creates the layout and adds to it the tables that will show the booking
     */
    public ViewBookingsPanel(List<String> roomNames) {
        setLayout(new BorderLayout());

        //Creates the table component with the custom table as the model
        myTable = new MyTable(roomNames);
        table = new JTable(myTable);

        //Set custom cell renderer
        table.setDefaultRenderer(Object.class, new TableCellRenderer());

        //increase cell height to show more informations
        table.setRowHeight(30);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setMinWidth(80);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        dateBox = new JComboBox<>();
        dateBox.addActionListener(e -> updateTableForSelectedDate(e));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Date"));
        topPanel.add(dateBox);

        //Add components to the panel
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    public MyTable getMyTable(){
        return this.myTable;
    }

    public JTable getTable(){
        return this.table;
    }

    public void refreshTable(){
        myTable.fireTableDataChanged();
    }

    /**
     * updates the shown bookings based on the selected date
     * 
     * @param e
     */
    private void updateTableForSelectedDate(ActionEvent e) {
        LocalDate selectedDate = (LocalDate) dateBox.getSelectedItem();
        if (selectedDate != null) {
            myTable.setDate(selectedDate);
            refreshTable();
        }
    }

    /**
     * updates the dates in the combobox
     * @param date
     * @param action
     */
    public void updateAvailableDates(LocalDate date, String action) {
        if(action.equals("add")){
            this.dateBox.addItem(date);
        }
        else if(action.equals("del")){
            this.dateBox.removeItem(date);
        }
        
    }

    public JComboBox<LocalDate> getDateComboBox(){
        return this.dateBox;
    }
}