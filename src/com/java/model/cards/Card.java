package com.java.model.cards;

import com.java.model.map.Country;

public class Card {
    private ArmyType armyType;
    private Country country;

    public Card(ArmyType armyType, Country country) {
        this.armyType = armyType;
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }
    public ArmyType getArmyType() {
        return armyType;
    }
}
