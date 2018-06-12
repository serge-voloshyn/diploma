package org.diploma.gui;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface HasTable {
    /**
     * Запись таблице в виде CSV
     *
     * @param writer запись в файл
     * @param table  таблица, которую нужно записать
     * @throws IOException
     */
    default void writeCsvTo(final FileWriter writer, final JTable table) throws IOException {
        writer.write("\"\", \"\"\n");

        final TableColumnModel columnModel = table.getColumnModel();
        List<String> rowData = new ArrayList<>(table.getColumnCount());
        for (int col = 0; col < table.getColumnCount(); col++) {
            rowData.add(String.format("\"%s\"", Objects.toString(columnModel.getColumn(col).getHeaderValue(), "")));
        }
        writer.write(rowData.stream().collect(Collectors.joining(",")));
        writer.write("\n");

        for (int row = 0; row < table.getRowCount(); row++) {
            rowData = new ArrayList<>(table.getColumnCount());
            for (int col = 0; col < table.getColumnCount(); col++) {
                rowData.add(String.format("\"%s\"", Objects.toString(table.getValueAt(row, col), "")));
            }
            writer.write(rowData.stream().collect(Collectors.joining(",")));
            writer.write("\n");
        }
    }
}
