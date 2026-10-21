
package com.frankie;

public class Weapon {

    private String name;
    private int durability;
    private int enchantability;

    public Weapon(String name, int durability, int enchantability) {
        this.name = name;
        this.durability = durability;
        this.enchantability = enchantability;
    }

    public String getName() {
        return name;
    }

    public int getDurability() {
        return durability;
    }

    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public String toString() {
        return name
                + " | Durability: " + durability
                + " | Enchantability: " + enchantability;
    }
}
