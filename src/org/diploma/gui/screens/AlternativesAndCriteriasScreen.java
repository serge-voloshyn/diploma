package org.diploma.gui.screens;

import org.diploma.gui.CanGoNext;
import org.diploma.gui.components.ItemCreation;

import javax.swing.*;
import java.awt.*;

public class AlternativesAndCriteriasScreen extends JPanel implements CanGoNext {

    private final JTextField target = new JTextField(60);
    private final ItemCreation alternatives = new ItemCreation("Количество альтернитив:");
    private final ItemCreation criterias = new ItemCreation("Количество критериев:");

    public AlternativesAndCriteriasScreen() {
        setLayout(new BorderLayout(10, 10));
        addTargetInput();

        final JPanel centerPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        centerPanel.add(alternatives);
        centerPanel.add(criterias);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addTargetInput() {
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(new JLabel("Цель:"));
        panel.add(target);
        add(panel, BorderLayout.PAGE_START);
    }

    public String getTarget() {
        return target.getText();
    }

    public java.util.List<String> getAlternatives() {
        return alternatives.getData();
    }

    public java.util.List<String> getCriterias() {
        return criterias.getData();
    }

    public boolean canGoNext() {
        return alternatives.canGoNext() && criterias.canGoNext() && !target.getText().isEmpty();
    }

}
