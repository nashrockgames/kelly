package com.nrg.kelly.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrew on 7/05/2015.
 */
public class Box2dGameModel {

    private List<Actor> actors = new ArrayList<Actor>();

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Inject
    public Box2dGameModel(){

    }

    public List<Actor> getActors() {
        return actors;
    }




}
