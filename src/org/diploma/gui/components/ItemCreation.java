package org.diploma.gui.components;

import org.diploma.gui.CanGoNext;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.stream.IntStream;

public class ItemCreation extends JPanel implements CanGoNext {

    private final SimpleTable table;

    public ItemCreation(final String title) {
        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(new Label(title), c);
        final Choice choice = new Choice();
        IntStream.rangeClosed(2, 10).forEach(i -> {
            choice.addItem(String.valueOf(i));
        });
        choice.select("2");
        c.gridx = 1;
        c.gridy = 0;
        add(choice, c);

        table = new SimpleTable("Название", 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(new JScrollPane(table), c);

        choice.addItemListener(e ->
            table.changeRowCount(Integer.parseInt(Objects.toString(e.getItem(), "1")))
        );
    }

    public java.util.List<String> getData() {
        return table.getData();
    }

    public boolean canGoNext() {
        return getData().stream().noneMatch(String::isEmpty);
    }
}
