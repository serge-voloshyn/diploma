package org.diploma.gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PairComparing extends JTable {
    public PairComparing(final String name, final List<String> pairs) {
        super(new Model(name, pairs));
        setFillsViewportHeight(true);
        setShowGrid(true);
        setGridColor(Color.lightGray);
    }

    private static Object[][] prepareData(final List<String> pairs) {
        final Object[][] data = new Object[pairs.size() + 1][];
        for (int i = 0; i < pairs.size(); i++) {
            final Object[] row = new Object[pairs.size() + 2];
            row[0] = pairs.get(i);
            for (int j = 0; j < pairs.size(); j++) {
                if (j + 1 == i + 1) {
                    row[j + 1] = "1";
                } else {
                    row[j + 1] = "";
                }
            }
            row[row.length - 1] = "";
            data[i] = row;
        }
        return data;
    }

    private static Object[] prepareHeaders(final String name, final List<String> pairs) {
        final Object[] headers = new Object[pairs.size() + 2];
        headers[0] = name;
        for (int i = 0; i < pairs.size(); i++) {
            headers[i + 1] = pairs.get(i);
        }
        headers[headers.length - 1] = "Лок.";

        return headers;
    }

    private static final class Model extends DefaultTableModel {

        private final Map<String, String> pairsRatio = new HashMap<>();
        private final List<String> pairs;

        public Model(final String name, final List<String> pairs) {
            super(prepareData(pairs), prepareHeaders(name, pairs));
            this.pairs = pairs;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column >= 1 && row != column && row < (getRowCount() - 1);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            pairsRatio.put(pairs.get(row) + "_" + pairs.get(column - 1), Objects.toString(aValue, ""));
            super.setValueAt(Objects.toString(aValue, ""), row, column);
            super.setValueAt(Objects.toString(aValue, ""), column, row);
        }
    }
}
