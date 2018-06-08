package org.diploma.gui.screens;

import org.diploma.gui.CanGoNext;
import org.diploma.gui.HasDependency;
import org.diploma.gui.Screens;
import org.diploma.gui.components.PairComparing;
import org.diploma.gui.components.SimpleTable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatiosScreen extends JPanel implements CanGoNext, HasDependency {

    public static final String CRITERIAS_RATIO = "Критерии";

    private final Map<String, Map<String, Double>> ratios = new HashMap<>();
    private final Map<String, PairComparing> ratiosComponenets = new HashMap<>();

    public RatiosScreen() {
        setLayout(new BorderLayout(10, 10));
        Screens.getScreen(AlternativesAndCriteriasScreen.class).ifPresent(screen ->
                draw(true, screen.getTarget(), screen.getCriterias(), screen.getAlternatives())
        );
    }

    @Override
    public boolean canGoNext() {
        return false;
    }

    private void draw(final boolean initial, final String target,
                      final java.util.List<String> criterias,
                      final java.util.List<String> alternatives) {
        if (initial) {
            addTargetTitle(target);
            final JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.add(CRITERIAS_RATIO, new JScrollPane(createPairComparing(CRITERIAS_RATIO, criterias)));
            for(final String criteria : criterias) {
                tabbedPane.add(criteria, new JScrollPane(createPairComparing(criteria, alternatives)));
            }
            add(tabbedPane, BorderLayout.CENTER);
        }


    }

    private Component createPairComparing(final String criteriasRatio, final List<String> criterias) {
        return ratiosComponenets.computeIfAbsent(criteriasRatio, k -> new PairComparing(criteriasRatio, criterias));
    }

    private void addTargetTitle(final String target) {
        final JLabel title = new JLabel(target);
        title.setBounds(0, 50, 200, 100);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        add(title, BorderLayout.PAGE_START);
    }

    @Override
    public void redraw() {
        Screens.getScreen(AlternativesAndCriteriasScreen.class).ifPresent(screen ->
                draw(false, screen.getTarget(), screen.getCriterias(), screen.getAlternatives())
        );
    }
}
