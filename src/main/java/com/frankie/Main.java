package com.frankie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class Main {

    public static void main(String[] args) {

        WeaponApiClient apiClient = new WeaponApiClient();
        WeaponCsvExporter exporter = new WeaponCsvExporter();
        WeaponDataCleaner cleaner = new WeaponDataCleaner();
        WeaponChartCreator chartCreator = new WeaponChartCreator();

        String[] searchTerms = {
                // Swords
                "wooden sword", "stone sword", "iron sword",
                "golden sword", "diamond sword", "netherite sword",

                // Axes
                "wooden axe", "stone axe", "iron axe",
                "golden axe", "diamond axe", "netherite axe",

                // Pickaxes
                "wooden pickaxe", "stone pickaxe", "iron pickaxe",
                "golden pickaxe", "diamond pickaxe", "netherite pickaxe",

                // Other equipment
                "bow", "shield"
        };

        List<Weapon> allWeapons = new ArrayList<>();

        // Collect data from the API.
        for (String term : searchTerms) {
            try {
                List<Weapon> weapons = apiClient.searchWeapons(term);

                System.out.println("\nSearch: " + term);

                for (Weapon weapon : weapons) {
                    System.out.println(weapon);
                    allWeapons.add(weapon);
                }

            } catch (Exception e) {
                System.err.println(
                        "Could not retrieve " + term + ": "
                                + e.getMessage());
            }
        }

        System.out.println(
                "\nRaw records collected: " + allWeapons.size());

        // Clean and process swords.
        List<Weapon> cleanedWeapons =
                cleaner.cleanWeapons(allWeapons);

        System.out.println(
                "Cleaned sword records: " + cleanedWeapons.size());

        for (Weapon weapon : cleanedWeapons) {
            System.out.println(weapon);
        }

        if (cleanedWeapons.isEmpty()) {
            System.out.println(
                    "No matching swords found. Check API names.");
        } else {
            try {
                exporter.exportWeapons(
                        cleanedWeapons, "cleaned_weapon_data.csv");

                chartCreator.createDurabilityChart(cleanedWeapons);
                chartCreator.createEnchantabilityChart(cleanedWeapons);

            } catch (Exception e) {
                System.err.println(
                        "Could not process sword data: "
                                + e.getMessage());
            }
        }

        // Define the additional categories to analyze.
        Map<String, String[]> categories = new LinkedHashMap<>();

        categories.put("Axes", new String[]{
                "Wooden Axe", "Stone Axe", "Iron Axe",
                "Golden Axe", "Diamond Axe", "Netherite Axe"
        });

        categories.put("Pickaxes", new String[]{
                "Wooden Pickaxe", "Stone Pickaxe", "Iron Pickaxe",
                "Golden Pickaxe", "Diamond Pickaxe", "Netherite Pickaxe"
        });

        categories.put("Bows", new String[]{
                "Bow", "Bow of Bone", "Bow of Lost Souls",
                "Bow of Weapon Projectiles", "Bonebow"
        });


        categories.put("Shields", new String[]{
                "Shield",
                "Stone Shield",
                "Iron Shield",
                "Gold Shield",
                "Golden Shield",
                "Bronze Shield"
        });


        // Clean, export, and chart each additional category.
        for (Map.Entry<String, String[]> entry
                : categories.entrySet()) {

            String category = entry.getKey();

            List<Weapon> categoryItems =
                    cleaner.cleanExactNames(
                            allWeapons, entry.getValue());

            System.out.println(
                    "\n" + category + " records: "
                            + categoryItems.size());

            for (Weapon item : categoryItems) {
                System.out.println(item);
            }

            try {
                String csvFile =
                        category.toLowerCase() + "_data.csv";

                exporter.exportWeapons(categoryItems, csvFile);

                chartCreator.createCategoryCharts(
                        categoryItems, category);

            } catch (Exception e) {
                System.err.println(
                        "Could not process " + category + ": "
                                + e.getMessage());
            }
        }
    }
}


