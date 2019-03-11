package com.java.model.cards;

import com.java.model.map.Country;

import java.util.ArrayList;
import java.util.Collections;

public class CardsDeck {

    ArrayList<Card> deck = new ArrayList<Card>();
    ArmyType[] armyTypes = ArmyType.values();

    public CardsDeck(ArrayList<Country> countryList) {
        final int EACH_ARMY_TYPE_COUNT = (countryList.size()/armyTypes.length);
        Collections.shuffle(countryList);
        for (int card = 0; card < countryList.size(); card++){
            deck.add(new Card(armyTypes[card/EACH_ARMY_TYPE_COUNT], countryList.get(card)));
        }
        Collections.shuffle(deck);
    }
}
