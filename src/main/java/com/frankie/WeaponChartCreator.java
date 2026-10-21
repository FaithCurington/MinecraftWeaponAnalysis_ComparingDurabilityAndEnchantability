
package com.frankie;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.event.AnnotationChangeListener;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import org.jfree.chart.axis.ValueAxis;

public class WeaponChartCreator {

    public void createDurabilityChart(List<Weapon> weapons)
            throws IOException {

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        for (Weapon weapon : weapons) {
            dataset.addValue(
                    weapon.getDurability(),
                    "Durability",
                    weapon.getName()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Minecraft Sword Durability Comparison",
                "Sword Type",
                "Durability (Maximum Damage)",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        styleChart(chart, "Durability");
        addWeaponImages(chart, weapons);

        saveChartAsPng(chart, "durability_chart.png");
        displayChart(chart, "Sword Durability");
    }

    public void createEnchantabilityChart(List<Weapon> weapons)
            throws IOException {

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        for (Weapon weapon : weapons) {
            dataset.addValue(
                    weapon.getEnchantability(),
                    "Enchantability",
                    weapon.getName()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Minecraft Sword Enchantability Comparison",
                "Sword Type",
                "Enchantability Value",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        styleChart(chart, "Enchantability");
        addWeaponImages(chart, weapons);

        saveChartAsPng(chart, "enchantability_chart.png");
        displayChart(chart, "Sword Enchantability");
    }

    private void saveChartAsPng(
            JFreeChart chart, String fileName)
            throws IOException {

        File file = new File(fileName);

        ChartUtils.saveChartAsPNG(
                file,
                chart,
                1200,
                700
        );

        System.out.println(
                "Chart saved to: " + file.getAbsolutePath()
        );
    }

    private void displayChart(
            JFreeChart chart, String title) {

        ChartFrame frame = new ChartFrame(title, chart);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private BufferedImage loadWeaponImage(String weaponName) {
        String name = weaponName.toLowerCase();

        String imageName;


        if (name.contains("sword")) {
            imageName = "sword.png";
        } else if (name.contains("pickaxe")) {
            imageName = "pickaxe.png";
        } else if (name.contains("axe")) {
            imageName = "axe.png";
        } else if (name.contains("bow")) {
            imageName = "bow.png";
        } else if (name.contains("shield")) {
            imageName = "shield.png";
        } else {
            System.out.println(
                    "No category image assigned for: " + weaponName
            );
            return null;
        }


        String imagePath = "/images/" + imageName;

        try (InputStream input =
                     getClass().getResourceAsStream(imagePath)) {

            if (input == null) {
                System.out.println("Image not found: " + imagePath);
                return null;
            }

            BufferedImage image = ImageIO.read(input);

            if (image == null) {
                System.out.println(
                        "Invalid or unsupported image: " + imagePath
                );
            }

            return image;

        } catch (IOException e) {
            System.out.println("Could not load image: " + imagePath);
            return null;
        }
    }



    private void addWeaponImages(
            JFreeChart chart, List<Weapon> weapons) {

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryDataset dataset = plot.getDataset();

        if (dataset == null || weapons.isEmpty()) {
            return;
        }

        double maxValue = 0;

        for (int column = 0;
             column < dataset.getColumnCount();
             column++) {

            Number value = dataset.getValue(0, column);

            if (value != null) {
                maxValue = Math.max(
                        maxValue, value.doubleValue()
                );
            }
        }

        if (maxValue <= 0) {
            return;
        }

        NumberAxis rangeAxis =
                (NumberAxis) plot.getRangeAxis();

        rangeAxis.setRange(
                0,
                maxValue * 1.25
        );

        rangeAxis.setNumberFormatOverride(
                new DecimalFormat("0;")
        );

        for (Weapon weapon : weapons) {
            BufferedImage image =
                    loadWeaponImage(weapon.getName());

            if (image != null) {
                plot.addAnnotation(
                        new WeaponImageAnnotation(
                                weapon.getName(),
                                image
                        )
                );
            }
        }
    }

    private void styleChart(
            JFreeChart chart, String chartType) {


        Color stormyTeal = new Color(22, 97, 91);
        Color teal = new Color(22, 126, 134);
        Color verdigris = new Color(17, 155, 133);
        Color fernGreen = new Color(86, 131, 83);
        Color white = new Color(245, 248, 245);
        Color softWhite = new Color(220, 231, 226);
        Color black = new Color(0, 0, 0);

        chart.setBackgroundPaint(stormyTeal);

        chart.getTitle().setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );
        chart.getTitle().setPaint(white);

        CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(verdigris);
        plot.setOutlinePaint(teal);
        plot.setOutlineVisible(true);
        plot.setRangeGridlinePaint(stormyTeal);

        // Horizontal category axis.
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setLabelFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        domainAxis.setLabelPaint(white);

        domainAxis.setTickLabelFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        domainAxis.setTickLabelPaint(softWhite);

        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.UP_45
        );

        // Vertical numeric axis.
        NumberAxis rangeAxis =
                (NumberAxis) plot.getRangeAxis();

        rangeAxis.setLabelFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        rangeAxis.setLabelPaint(white);

        rangeAxis.setTickLabelFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        rangeAxis.setTickLabelPaint(softWhite);

        rangeAxis.setUpperMargin(0.15);

        // Bar appearance and value labels.
        BarRenderer renderer =
                (BarRenderer) plot.getRenderer();

        renderer.setDefaultItemLabelsVisible(true);

        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator()
        );

        renderer.setDefaultItemLabelFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        renderer.setDefaultItemLabelPaint(white);

        renderer.setDefaultPositiveItemLabelPosition(
                new org.jfree.chart.labels.ItemLabelPosition(
                        org.jfree.chart.labels.ItemLabelAnchor.OUTSIDE12,
                        org.jfree.chart.ui.TextAnchor.BOTTOM_CENTER
                )
        );

        if ("Durability".equals(chartType)) {
            renderer.setSeriesPaint(0, fernGreen);
        } else {
            renderer.setSeriesPaint(0, teal);
        }

        renderer.setSeriesOutlinePaint(0, black);
        renderer.setDrawBarOutline(true);
    }

    public void createCategoryCharts(
            List<Weapon> items, String category)
            throws IOException {

        if (items.isEmpty()) {
            System.out.println(
                    "No matching records for " + category
            );
            return;
        }

        DefaultCategoryDataset durabilityDataset =
                new DefaultCategoryDataset();

        DefaultCategoryDataset enchantabilityDataset =
                new DefaultCategoryDataset();

        for (Weapon item : items) {
            durabilityDataset.addValue(
                    item.getDurability(),
                    "Durability",
                    item.getName()
            );

            enchantabilityDataset.addValue(
                    item.getEnchantability(),
                    "Enchantability",
                    item.getName()
            );
        }

        String filePrefix = category.toLowerCase();

        JFreeChart durabilityChart =
                ChartFactory.createBarChart(
                        "Minecraft " + category
                                + " Durability Comparison",
                        "Item Type",
                        "Durability (Maximum Damage)",
                        durabilityDataset,
                        PlotOrientation.VERTICAL,
                        false,
                        true,
                        false
                );

        styleChart(durabilityChart, "Durability");
        addWeaponImages(durabilityChart, items);

        saveChartAsPng(
                durabilityChart,
                filePrefix + "_durability_chart.png"
        );

        displayChart(
                durabilityChart,
                category + " Durability"
        );

        JFreeChart enchantabilityChart =
                ChartFactory.createBarChart(
                        "Minecraft " + category
                                + " Enchantability Comparison",
                        "Item Type",
                        "Enchantability Value",
                        enchantabilityDataset,
                        PlotOrientation.VERTICAL,
                        false,
                        true,
                        false
                );

        styleChart(enchantabilityChart, "Enchantability");
        addWeaponImages(enchantabilityChart, items);

        saveChartAsPng(
                enchantabilityChart,
                filePrefix + "_enchantability_chart.png"
        );

        displayChart(
                enchantabilityChart,
                category + " Enchantability"
        );
    }


    private static class WeaponImageAnnotation implements CategoryAnnotation {

        private final String category;
        private final BufferedImage image;

        public WeaponImageAnnotation(
                String category,
                BufferedImage image) {
            this.category = category;
            this.image = image;
        }

        @Override
        public void draw(
                Graphics2D g2,
                CategoryPlot plot,
                Rectangle2D dataArea,
                CategoryAxis domainAxis,
                ValueAxis rangeAxis) {

            if (plot.getDataset() == null || image == null) {
                return;
            }

            int categoryIndex =
                    plot.getDataset().getColumnIndex(category);

            if (categoryIndex < 0) {
                return;
            }

            int iconSize = 150;

            // Position the icon inside the graph's top-left corner.
            int imageX = (int) dataArea.getMinX() + 10;
            int imageY = (int) dataArea.getMinY() + 10;

            g2.drawImage(
                    image,
                    imageX,
                    imageY,
                    iconSize,
                    iconSize,
                    null);
        }

        @Override
        public void addChangeListener(
                AnnotationChangeListener listener) {

        }

        @Override
        public void removeChangeListener(
                AnnotationChangeListener listener) {

        }
    }
}
