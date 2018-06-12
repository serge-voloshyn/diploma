package org.diploma.gui.screens;

import org.diploma.gui.CanGoNext;
import org.diploma.gui.HasDependency;
import org.diploma.gui.HasTable;
import org.diploma.gui.Screens;
import org.diploma.gui.components.PairComparing;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Окно попарного сравнения.
 */
public class RatiosScreen extends JPanel implements CanGoNext, HasDependency, HasTable {

    public static final String CRITERIAS_RATIO = "Критерии";

    private final Map<String, PairComparing> ratiosComponenets = new HashMap<>();
    private JTabbedPane tabbedPane;

    public RatiosScreen() {
        setLayout(new BorderLayout(10, 10));
        Screens.getScreen(AlternativesAndCriteriasScreen.class).ifPresent(screen ->
                draw(screen.getTarget(), screen.getCriterias(), screen.getAlternatives())
        );
    }

    @Override
    public boolean canGoNext() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (!((PairComparing)((JScrollPane) tabbedPane.getComponentAt(i)).getViewport().getComponent(0)).isFullyDefined()) {
                tabbedPane.setSelectedIndex(i);
                return false;
            }
        }
        return true;
    }

    public Map<String, PairComparing> getRatios() {
        return ratiosComponenets;
    }

    private void draw(final String target,
                      final java.util.List<String> criterias,
                      final java.util.List<String> alternatives) {
        if (tabbedPane == null) {
            addTargetTitle(target);
            tabbedPane = new JTabbedPane();
            tabbedPane.add(CRITERIAS_RATIO, new JScrollPane(createPairComparing(CRITERIAS_RATIO, criterias)));
            for (final String criteria : criterias) {
                tabbedPane.add(criteria, new JScrollPane(createPairComparing(criteria, alternatives)));
            }
            add(tabbedPane, BorderLayout.CENTER);
        } else {
            Optional.ofNullable(ratiosComponenets.get(CRITERIAS_RATIO)).ifPresent(comparing -> {
                comparing.update(criterias);
            });
            for (final String criteria : criterias) {
                final Optional<PairComparing> pairComparing = Optional.ofNullable(ratiosComponenets.get(criteria));
                pairComparing.ifPresent(comparing -> comparing.update(alternatives));
                if (!pairComparing.isPresent()) {
                    tabbedPane.add(criteria, new JScrollPane(createPairComparing(criteria, alternatives)));
                }
            }
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
                draw(screen.getTarget(), screen.getCriterias(), screen.getAlternatives())
        );
    }

    public void writeCsvTo(final FileWriter writer) throws IOException {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            final PairComparing component = (PairComparing) ((JScrollPane) tabbedPane.getComponentAt(i)).getViewport().getComponent(0);
            writeCsvTo(writer, component);
        }
    }
}
