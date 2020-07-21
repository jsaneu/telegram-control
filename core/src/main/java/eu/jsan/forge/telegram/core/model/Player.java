package eu.jsan.forge.telegram.core.model;

public class Player {

    private final String name;

    private final float currentHealth;

    private final float maxHealth;

    private final int experienceLevel;

    public Player(String name, float currentHealth, float maxHealth, int experienceLevel) {
        this.name = name;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.experienceLevel = experienceLevel;
    }

    public String getName() {
        return name;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }
}
