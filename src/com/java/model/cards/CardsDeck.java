package com.java.model.cards;

import com.java.model.map.Country;

import java.util.ArrayList;
import java.util.Collections;

public class CardsDeck {

    ArrayList<Card> deck = new ArrayList<Card>();
    ArmyType[] armyTypes = ArmyType.values();
    private Card card;
    private int top = 0;

    public CardsDeck(ArrayList<Country> countryList) {
        Collections.shuffle(countryList);
        for (int card = 0; card < countryList.size(); card++){
            deck.add(new Card(armyTypes[card%armyTypes.length], countryList.get(card)));
        }
        Collections.shuffle(deck);
    }

    public Card getCard(){
        card = deck.get(top);
        deck.remove(top);
        return card;
    }
}
