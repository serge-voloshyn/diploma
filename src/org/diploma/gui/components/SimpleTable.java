package org.diploma.gui.components;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleTable extends JTable {

    private final String columnHeader;

    public SimpleTable(final String columnHeader, final List<String> data) {
        super(new Model(columnHeader, data));
        getColumnModel().getColumn(0).setMaxWidth(50);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setShowGrid(true);
        setGridColor(Color.lightGray);
        this.columnHeader = columnHeader;
    }

    public SimpleTable(final String columnHeader, int rowCount) {
        this(columnHeader, IntStream.range(0, rowCount).mapToObj(i -> "").collect(Collectors.toList()));
    }

    public List<String> getData() {
        return ((Model) getModel()).getData();
    }

    private static Object[][] prepareData(final List<String> data) {
        final Object[][] result = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            result[i] = new Object[]{"" + (i + 1), data.get(i)};
        }
        return result;
    }

    public void changeRowCount(int rowCount) {
        List<String> data = getData();
        if (data.size() < rowCount) {
            final int toAdd = rowCount - data.size();
            for (int i = 0; i < toAdd; i++) {
                data.add("");
            }
        } else {
            data = data.subList(0, rowCount);
        }
        setModel(new Model(columnHeader, data));
        getColumnModel().getColumn(0).setMaxWidth(50);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setShowGrid(true);
        setGridColor(Color.lightGray);
    }

    private static final class Model extends DefaultTableModel {

        private final List<String> data;

        public Model(final String columnName, final List<String> data) {
            super(prepareData(data), new Object[]{"%№", columnName});
            this.data = data;
        }

        public List<String> getData() {
            return data;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column >= 1;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            data.set(row, Objects.toString(aValue, ""));
            super.setValueAt(Objects.toString(aValue, ""), row, column);
        }
    }
}
