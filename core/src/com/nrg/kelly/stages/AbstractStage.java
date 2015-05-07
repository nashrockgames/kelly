package com.nrg.kelly.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


import java.util.List;

/**
 * Created by Andrew on 7/05/2015.
 */
public class AbstractStage extends Stage {

    protected void addActors(List<Actor> actorsList){
        for(Actor actor: actorsList){
            this.addActor(actor);
        }
    }
}
