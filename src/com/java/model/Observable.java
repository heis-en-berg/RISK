package com.java.model;

import com.java.view.GameView;

import java.util.ArrayList;

public abstract class Observable {
    private ArrayList<GameView> listView;

    public Observable(){
        listView = new ArrayList<GameView>();
    }

    public void addObserver(GameView observer){
       if(observer != null) {
           listView.add(observer);
       }
    }

    public void deleteObserver(GameView observer){
       if(observer != null) {
           listView.remove(observer);
       }
    }

    public void notifyView(){
        for (GameView gm : listView){
            gm.update(this);
        }
    }


}
