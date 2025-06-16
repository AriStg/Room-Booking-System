package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Custom cell renderer for the table, highlighting cell colors based on room availability.
 */
public class TableCellRenderer extends DefaultTableCellRenderer {
    /**
     * Overrides the default tables cells and applies custom colors.
     *
     * @param table      the JTable being rendered
     * @param value      the value to assign to the cell
     * @param isSelected whether the cell is selected
     * @param hasFocus   whether the cell has focus
     * @param row        the row index of the cell
     * @param column     the column index of the cell
     * @return the customized component used for the cells
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(column == 0){
            cell.setBackground(Color.LIGHT_GRAY);
        } else if ("".equals(value)) {
            cell.setBackground(Color.WHITE);
        } else {
            cell.setBackground(new Color(0xDEFF8C));
        }

        return cell;
    }
}