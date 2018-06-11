package org.diploma.gui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PairComparing extends JTable {
    public PairComparing(final String name, final List<String> pairs) {
        super(new Model(name, pairs));
        setFillsViewportHeight(true);
        setShowGrid(true);
        setGridColor(Color.lightGray);
    }

    private static Object[][] prepareData(final List<String> pairs, final Map<String, String> ratio) {
        final Object[][] data = new Object[pairs.size()][];
        for (int i = 0; i < pairs.size(); i++) {
            final Object[] row = new Object[pairs.size() + 3];
            row[0] = pairs.get(i);
            for (int j = 0; j < pairs.size(); j++) {
                if (j + 1 == i + 1) {
                    row[j + 1] = "1";
                } else {
                    row[j + 1] =
                            ratio.getOrDefault(row[0] + "_" + pairs.get(j), ratio.get(pairs.get(j) + "_" + row[0]));
                }
            }
            row[row.length - 2] = "";
            row[row.length - 1] = "";
            data[i] = row;
        }
        return data;
    }

    private static Object[] prepareHeaders(final String name, final List<String> pairs) {
        final Object[] headers = new Object[pairs.size() + 3];
        headers[0] = name;
        for (int i = 0; i < pairs.size(); i++) {
            headers[i + 1] = pairs.get(i);
        }
        headers[headers.length - 2] = "Лок.";
        headers[headers.length - 1] = "Норм.";

        return headers;
    }

    public void update(final List<String> pairs) {
        final Map<String, String> existingPairsRatio = ((Model) getModel()).pairsRatio;
        final Map<String, String> ratio = existingPairsRatio.entrySet().stream()
                .filter(e -> Arrays.stream(e.getKey().split("_")).allMatch(pairs::contains))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        setModel(new Model(((Model) getModel()).name, pairs, ratio));
    }

    public boolean isFullyDefined() {
        return ((Model) getModel()).isFullyDefined();
    }

    public Map<String, Double> getResultingRatios() {
        return ((Model) getModel()).getResultingRatios();
    }

    private static final class Model extends DefaultTableModel {

        private final Map<String, String> pairsRatio = new HashMap<>();
        private final List<String> pairs;
        private final String name;

        public Model(final String name, final List<String> pairs) {
            super(prepareData(pairs, Collections.emptyMap()), prepareHeaders(name, pairs));
            this.pairs = pairs;
            this.name = name;
        }

        public Model(final String name, final List<String> pairs, final Map<String, String> ratio) {
            super(prepareData(pairs, ratio), prepareHeaders(name, pairs));
            this.pairs = pairs;
            this.name = name;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column >= 1 && (column < getColumnCount() - 2) && row != column - 1;
        }

        private String reverse(final String value) {
            if (value.contains("/"))
                return new StringBuilder(value).reverse().toString();
            return "1/" + value;
        }

        private double evaluate(final String value) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                final String[] sub = value.trim().split("/");
                if (sub.length > 1) {
                    return Double.parseDouble(sub[0]) / Double.parseDouble(sub[1]);
                } else {
                    return Double.parseDouble(sub[0]);
                }
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            pairsRatio.put(pairs.get(row) + "_" + pairs.get(column - 1), Objects.toString(aValue, "1"));
            super.setValueAt(aValue, row, column);
            super.setValueAt(reverse(Objects.toString(aValue, "1")), column - 1, row + 1);


            double fullSum = 0.0;

            for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
                double rowSum = 0.0;
                for (int i = 1; i < getColumnCount() - 2; i++) {
                    final Object columnValue = getValueAt(rowNum, i);
                    if (columnValue instanceof String) {
                        rowSum += evaluate((String) columnValue);
                    }
                }
                super.setValueAt(Objects.toString(Math.round(rowSum * 100) / 100.0), rowNum, getColumnCount() - 2);
                fullSum += rowSum;
            }

            for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
                final Object columnValue = getValueAt(rowNum, getColumnCount() - 2);
                try {
                    final Double rowLocal = Double.parseDouble(Objects.toString(columnValue, "0"));
                    super.setValueAt(Objects.toString(Math.round((rowLocal / fullSum) * 100) / 100.0), rowNum, getColumnCount() - 1);
                } catch (NumberFormatException e) {
                    //skipped
                }
            }
        }

        public boolean isFullyDefined() {
            for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
                for (int i = 1; i < getColumnCount() - 2; i++) {
                    if (Objects.toString(getValueAt(rowNum, i), "").trim().isEmpty()) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Map<String, Double> getResultingRatios() {
            final Map<String, Double> resulting = new HashMap<>();
            for (int rowNum = 0; rowNum < getRowCount(); rowNum++) {
                resulting.put(Objects.toString(getValueAt(rowNum, 0)),
                        Double.parseDouble(Objects.toString(getValueAt(rowNum, getColumnCount() - 1))));
            }
            return resulting;
        }
    }
}
