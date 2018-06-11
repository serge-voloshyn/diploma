package org.diploma.gui.components;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PresentationTable extends JTable {

    public PresentationTable(final List<String> columnHeaders, final List<String[]> data) {
        super(new Model(columnHeaders, data));
        getColumnModel().getColumn(0).setMaxWidth(50);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setShowGrid(true);
        setGridColor(Color.lightGray);
    }

    private static Object[] prepareHeaders(final List<String> columnNames) {
        columnNames.add(0, "Альтернатива");
        return columnNames.toArray();
    }

    private static Object[][] prepareData(final List<String[]> data) {
        final Object[][] result = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }

    private static final class Model extends DefaultTableModel {

        public Model(final List<String> columnNames, final List<String[]> data) {
            super(prepareData(data), prepareHeaders(columnNames));
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
