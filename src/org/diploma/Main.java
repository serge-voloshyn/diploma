package org.diploma;

import org.diploma.gui.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(MainFrame::buildAndShow);
    }
}
