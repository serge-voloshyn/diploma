package org.diploma.gui;

import org.diploma.gui.screens.AlternativesAndCriteriasScreen;
import org.diploma.gui.screens.RatiosScreen;
import org.diploma.gui.screens.ResultsScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private Component currentComponenet;
    private Screens.Screen currentScreen;

    private MainFrame() {
        setLayout(new BorderLayout(10, 10));//no layout manager
        build();
        setSize(940, 680);//frame size 300 width and 300 height
        setTitle("Метод анализа иерархий");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Формирование элементов окна.
     */
    private void build() {
        addTitle();
        addCentralPanel();
        addActions();
    }

    private void addCentralPanel() {
        goTo(Screens.Screen.AlternativesAndCriterias);
    }

    /**
     * Добавление кнопок переходов по окнам.
     */
    private void addActions() {
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        final JButton goBack = new JButton("Назад");
        goBack.addActionListener(this::goBack);
        final JButton goNext = new JButton("Далее");
        goNext.addActionListener(this::goNext);

        panel.add(goBack);
        panel.add(goNext);

        getContentPane().add(panel, BorderLayout.PAGE_END);
    }

    /**
     * Добавление основного заголовка.
     */
    private void addTitle() {
        final JLabel title = new JLabel("Метод анализа иерархий");
        title.setBounds(0, 50, 100, 100);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 24));
        getContentPane().add(title, BorderLayout.PAGE_START);
    }


    /**
     * Переход к следующему окну.
     *
     * @param screen тип следующего окна.
     */
    private void goTo(final Screens.Screen screen) {
        currentScreen = screen;
        switch (screen) {
            case AlternativesAndCriterias:
                switchCentral(Screens.getScreen(AlternativesAndCriteriasScreen.class, AlternativesAndCriteriasScreen::new));
                break;
            case Ratios:
                switchCentral(Screens.getScreen(RatiosScreen.class, RatiosScreen::new));
                break;
            case Results:
                switchCentral(Screens.getScreen(ResultsScreen.class, ResultsScreen::new));
                break;
        }
    }

    /**
     * Формирование центральной части окна.
     *
     * @param component компонент для отбражения в центральной части окна
     */
    private void switchCentral(final Component component) {
        if (currentComponenet != null) {
            getContentPane().remove(currentComponenet);
        }
        currentComponenet = component;
        getContentPane().add(currentComponenet, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Переход к предыдущему окну.
     *
     * @param actionEvent событие нажатия кнопки
     */
    private void goBack(ActionEvent actionEvent) {
        switch (currentScreen) {
            case AlternativesAndCriterias:
                break;
            case Ratios:
                goTo(Screens.Screen.AlternativesAndCriterias);
                break;
            case Results:
                goTo(Screens.Screen.Ratios);
                break;
        }
    }

    /**
     * Переход к следующему окну.
     *
     * @param actionEvent событие нажатия кнопки
     */
    private void goNext(ActionEvent actionEvent) {
        if (currentComponenet instanceof CanGoNext) {
            if (!((CanGoNext) currentComponenet).canGoNext()) {
                return;
            }
        }
        switch (currentScreen) {
            case AlternativesAndCriterias:
                goTo(Screens.Screen.Ratios);
                break;
            case Ratios:
                goTo(Screens.Screen.Results);
                break;
            default:
                break;
        }
    }

    /**
     * Создание окна приложения.
     */
    public static void buildAndShow() {
        new MainFrame();
    }
}
