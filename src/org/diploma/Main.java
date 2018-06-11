package org.diploma;

import org.diploma.gui.MainFrame;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static void log(final String message, final Object... args) {
        System.out.println(String.format(message, args));
    }

    public static void main(final String[] args) throws Exception {
//        if (args == null || args.length < 1) {
//            throw new IllegalArgumentException("Должен быть указан файл альтернитив и критерий!");
//        }
//
//        if (args.length < 2) {
//            throw new IllegalArgumentException("Должен быть указан файл соотношения альтернатив!");
//        }
//
//        final Alternatives alternatives = Alternatives.build(Paths.get(args[0]));
//        final Ratio alternativeRatio = Ratio.build(Paths.get(args[1]));
//
//
//
//        final ConstructionCalculator calculator = ConstructionCalculator.INSTANCE;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(MainFrame::buildAndShow);
    }


    public static final class Alternatives {
        final String[] headers;
        final List<String[]> data;
        final List<Ratio> ratios;

        public Alternatives(final String[] headers,
                            final List<String[]> data,
                            final List<Ratio> ratios) {
            this.headers = headers;
            this.data = data;
            this.ratios = ratios;
        }

        public static Alternatives build(final Path filePath) throws Exception {
            final List<String> criterias = Files.readAllLines(filePath);

            if (criterias.size() < 2) {
                throw new IllegalArgumentException("Не указана ни одна альтернитива");
            }

            final String[] headers = criterias.get(0).split(",");
            final List<String[]> data = criterias.subList(1, criterias.size() - 1).stream()
                    .map(line -> line.split(",")).collect(Collectors.toList());

            final List<Ratio> ratios = Arrays.stream(criterias.get(criterias.size() - 1).split(",")).skip(1).map(
                    ratio -> {
                        try {
                            return Ratio.build(filePath.getParent().resolve(ratio));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            return new Alternatives(headers, data, ratios);
        }
    }

    public static final class Ratio {
        final Map<String, Double> ratios;

        private Ratio(final Map<String, Double> ratios) {
            this.ratios = ratios;
        }

        public static Ratio build(final Path filePath) throws Exception {
            final List<String[]> data = Files.readAllLines(filePath).stream()
                    .map(s -> s.split(",")).collect(Collectors.toList());

            final String[] headers = data.get(0);
            final List<String[]> ratios = data.subList(1, data.size());

            Map<String, Double> result = new HashMap<>();

            for (final String[] ratio : ratios) {
                Double ratioSum = 0d;
                for (int c = 1; c < headers.length; c++) {
                    ratioSum += Double.parseDouble(ratio[c]);
                }
                result.put(ratio[0], ratioSum);
            }

            final double ratioSum = result.values().stream().mapToDouble(Number::doubleValue).sum();
            if (ratioSum > 1) {
                result = result.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / ratioSum));
            }

            return new Ratio(result);
        }
    }
}
