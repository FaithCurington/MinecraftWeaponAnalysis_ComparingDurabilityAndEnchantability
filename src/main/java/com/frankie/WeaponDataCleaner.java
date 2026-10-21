
package com.frankie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeaponDataCleaner {

    private static final String[] TARGET_SWORDS = {
            "Wooden Sword",
            "Stone Sword",
            "Iron Sword",
            "Golden Sword",
            "Diamond Sword",
            "Netherite Sword"
    };

    public List<Weapon> cleanExactNames(
            List<Weapon> weapons, String[] targetNames) {

        java.util.Map<String, Weapon> uniqueWeapons =
                new java.util.LinkedHashMap<>();

        for (String targetName : targetNames) {
            for (Weapon weapon : weapons) {
                if (weapon.getName().equalsIgnoreCase(targetName)) {
                    uniqueWeapons.putIfAbsent(targetName, weapon);
                }
            }
        }

        return new java.util.ArrayList<>(uniqueWeapons.values());
    }


    public List<Weapon> cleanWeapons(List<Weapon> weapons) {

        Map<String, Weapon> uniqueWeapons = new LinkedHashMap<>();

        for (String target : TARGET_SWORDS) {
            for (Weapon weapon : weapons) {
                if (weapon.getName().equalsIgnoreCase(target)) {
                    uniqueWeapons.putIfAbsent(target, weapon);
                }
            }
        }

        return new ArrayList<>(uniqueWeapons.values());
    }
}

