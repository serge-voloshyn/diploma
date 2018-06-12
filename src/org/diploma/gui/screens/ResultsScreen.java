package org.diploma.gui.screens;

import org.diploma.gui.HasDependency;
import org.diploma.gui.HasTable;
import org.diploma.gui.Screens;
import org.diploma.gui.components.PresentationTable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResultsScreen extends JPanel implements HasDependency, HasTable {

    private PresentationTable presentationTable;

    public ResultsScreen() {
        setLayout(new BorderLayout(10, 10));
        redraw();
    }

    @Override
    public void redraw() {
        removeAll();
        Screens.getScreen(RatiosScreen.class).ifPresent(ratios ->
                Screens.getScreen(AlternativesAndCriteriasScreen.class).ifPresent(screen -> {
                    addTargetTitle(screen.getTarget());
                    final Map<String, Map<String, Double>> ratiosSettings =
                            ratios.getRatios().entrySet().stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getResultingRatios()));
                    final List<String> criterias = screen.getCriterias();
                    final List<String> alternatives = screen.getAlternatives();
                    final java.util.List<String[]> data = new ArrayList<>(alternatives.size());
                    final Map<String, Double> criteriaRatios
                            = ratiosSettings.get(RatiosScreen.CRITERIAS_RATIO);
                    for (final String alternative : alternatives) {
                        final String[] altData = new String[criterias.size() + 2];
                        altData[0] = alternative;
                        double sum = 0.0;
                        for (int i = 0; i < criterias.size(); i++) {
                            final Map<String, Double> alternativeRation = ratiosSettings.get(criterias.get(i));
                            double alternativeValue = alternativeRation.get(alternative) * criteriaRatios.get(criterias.get(i));
                            alternativeValue = Math.round(alternativeValue * 100) / 100.0;
                            altData[i + 1] = Objects.toString(alternativeValue);
                            sum += alternativeValue;
                        }
                        altData[altData.length - 1] = Objects.toString(sum);
                        data.add(altData);
                    }
                    final List<String> headers = new ArrayList<>(criterias);
                    headers.add("Сумма");
                    presentationTable = new PresentationTable(headers, data);
                    add(new JScrollPane(presentationTable), BorderLayout.CENTER);
                    addSaveButton();
                })
        );
    }

    private void addTargetTitle(final String target) {
        final JLabel title = new JLabel(target);
        title.setBounds(0, 50, 200, 100);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        add(title, BorderLayout.PAGE_START);
    }

    private void addSaveButton() {
        final JButton button = new JButton("Сохранить");
        button.addActionListener(e -> {
            JFileChooser savePlaylistDialog = new JFileChooser();
            savePlaylistDialog.setSelectedFile(new File(".csv"));
            int status = savePlaylistDialog.showSaveDialog(savePlaylistDialog);
            try {
                if (status == JFileChooser.APPROVE_OPTION) {
                    File savePlaylist = savePlaylistDialog.getSelectedFile();

                    FileWriter csvWriter = null;
                    try {
                        final FileWriter writer = new FileWriter(savePlaylist);
                        csvWriter = writer;

                        Screens.getScreen(RatiosScreen.class).ifPresent(ratios ->
                                Screens.getScreen(AlternativesAndCriteriasScreen.class).ifPresent(screen -> {
                                    try {
                                        screen.writeCsvTo(writer);
                                        ratios.writeCsvTo(writer);
                                        writeCsvTo(writer);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                })
                        );

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            System.out.println("closing file");
                            if (csvWriter != null) {
                                csvWriter.flush();
                                csvWriter.close();
                            }
                        } catch (IOException e1) {
                        }
                    }

                } else if (status == JFileChooser.CANCEL_OPTION) {
                    // User has pressed cancel button
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Невозможно записать в файл. Попробуйте еще раз.");
            }
        });
        add(button, BorderLayout.PAGE_END);
    }

    public void writeCsvTo(final FileWriter writer) throws IOException {
        writeCsvTo(writer, presentationTable);
    }
}
