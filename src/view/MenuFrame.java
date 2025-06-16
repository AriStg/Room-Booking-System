package view;

import java.awt.*;
import javax.swing.*;

import java.util.List;


/**
 * Creates the frame that contains the buttons and the table that show the bookings
 */
public class MenuFrame extends JFrame{
    private ViewBookingsPanel viewP;

    private JMenuItem addB;
    private JMenuItem editB;
    private JMenuItem deleteB;

    private JMenuItem saveB;
    private JMenuItem loadB;
    private JMenuItem autoSaveDirB;

    private JMenuItem printB;

    private JMenu menu;
    
    private JMenuBar menuBar;

    /**
     * Constructor of the class, creates the menu bar with all the options and the panel that showcases the bookings
     * 
     * @param title
     */
    public MenuFrame(String title, List<String> roomNames) {
        super(title);

        this.viewP = new ViewBookingsPanel(roomNames);

        //create the menu items
        addB = new JMenuItem("Add");
        editB = new JMenuItem("Edit");
        deleteB = new JMenuItem("Delete");

        saveB = new JMenuItem("Save");
        loadB = new JMenuItem("Load");
        autoSaveDirB = new JMenuItem("Auto save dir");

        printB = new JMenuItem("Print");

        menu = new JMenu("Save/Load");
        menu.add(saveB);
        menu.add(loadB);
        menu.add(autoSaveDirB);

        //create the bar and add the two menus
        menuBar = new JMenuBar();
        menuBar.add(addB);
        menuBar.add(editB);
        menuBar.add(deleteB);
        menuBar.add(printB);
        menuBar.add(menu);

        //set menuBar as the JMenuBar in this frame 
        this.setJMenuBar(menuBar);

        this.add(this.viewP, BorderLayout.CENTER);

        //Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 800);
        setVisible(true);
    }

    public ViewBookingsPanel getViewPanel(){
        return this.viewP;
    }

    public JMenuItem getAddButton(){
        return this.addB;
    }

    public JMenuItem getEditButton(){
        return this.editB;
    }

    public JMenuItem getDeleteButton(){
        return this.deleteB;
    }

    public JMenuItem getPrintButton(){
        return this.printB;
    }

    public JMenuItem getSaveButton(){
        return this.saveB;
    }

    public JMenuItem getLoadButton(){
        return this.loadB;
    }

    public JMenuItem getAutoSaveDirButton(){
        return this.autoSaveDirB;
    }
}