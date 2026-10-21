
package com.frankie;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WeaponCsvExporter {

    public void exportWeapons(List<Weapon> weapons, String fileName)
            throws IOException {

        StringBuilder csv = new StringBuilder();

        // CSV column headers
        csv.append("Weapon,Durability,Enchantability\n");

        for (Weapon weapon : weapons) {
            csv.append(escapeCsv(weapon.getName()))
                    .append(",")
                    .append(weapon.getDurability())
                    .append(",")
                    .append(weapon.getEnchantability())
                    .append("\n");
        }

        Path filePath = Path.of(fileName).toAbsolutePath();

        Files.writeString(
                filePath,
                csv.toString(),
                StandardCharsets.UTF_8
        );

        System.out.println("\nCSV file saved to: " + filePath);
    }

    private String escapeCsv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}

