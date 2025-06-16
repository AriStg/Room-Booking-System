package model;

import javax.print.attribute.*;
import javax.print.attribute.standard.Destination;
import javax.swing.*;

import java.awt.print.*;
import java.io.*;

/**
 * implements different JFileChoosers to be used based on the pressed button
 */
public class MyFileChooser extends JFileChooser {
    public MyFileChooser(String type) {
        setDialogTitle(type);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    /**
     * gets the chosen file, adds ".txt" if needed, asks to overwrite if the file already exists
     * 
     * @param selectedOption the option selected from the dialog, aka "Save" or "Cancel"
     * @return the file path, null if the operation isn't completed
     */
    public String getSaveFilePath(int selectedOption) {
        if (selectedOption != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File selectedFile = this.getSelectedFile();

        if (!selectedFile.getName().endsWith(".txt")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
        }

        if (selectedFile.exists()) {
            int overwriteConfirm = JOptionPane.showConfirmDialog(
                null,
                "The file already exists. Do you want to overwrite it?",
                "Confirm Overwrite",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (overwriteConfirm == JOptionPane.YES_OPTION) {
                return selectedFile.getAbsolutePath();
            }
        }

        return selectedFile.getAbsolutePath();
    }

    /**
     * loads the bookings from the chosen file
     * 
     * @param selectedOption the option selected from the dialog, aka "Save" or "Cancel"
     * @return the file path, null if the operation isn't completed
     */
    public String getLoadFilePath(int selectedOption){
        if(selectedOption != JFileChooser.APPROVE_OPTION){
            return null;
        }

        File selectedFile = this.getSelectedFile();
        if (!selectedFile.getName().endsWith(".txt")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
        }

        return selectedFile.getAbsolutePath();
    }

    /**
     * gets a new directory in which to save the tempFile from the automatic saving mechanism
     * 
     * @param selectedOption the option selected from the dialog, aka "Save" or "Cancel"
     * @return the file path, null if the operation isn't completed
     */
    public String changeTempFileDir(int selectedOption){
        if(selectedOption != JFileChooser.APPROVE_OPTION){
            return null;
        }

        File selectedFile = this.getSelectedFile();
        if (!selectedFile.getName().endsWith(".txt")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
        }

        return selectedFile.getAbsolutePath();
    }

    /**
     * prints the bookings
     * 
     * @param selectedOption the option selected from the dialog, aka "Save" or "Cancel"
     * @param table the table from the viewBookingsPanel
     * @return the filePath if the print is successful, null otherwise
     */
    public String printBookings(int selectedOption, JTable table){
        if (selectedOption != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File pdfFile = getSelectedFile();
        String pdfFilePath = pdfFile.getAbsolutePath();
    
        if (!pdfFilePath.toLowerCase().endsWith(".pdf")) {
            pdfFilePath += ".pdf";
        }

        String psFilePath = pdfFilePath.replace(".pdf", ".ps");
        File psFile = new File(psFilePath);

        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable(table.getPrintable(JTable.PrintMode.FIT_WIDTH, null, null));
    
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            attr.add(new Destination(psFile.toURI()));
    
            printerJob.print(attr);

            ProcessBuilder pb = new ProcessBuilder("ps2pdf", psFilePath, pdfFilePath);
            Process process = pb.start();
            process.waitFor();
            psFile.delete();

            return pdfFilePath;
        } catch (PrinterException | IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}