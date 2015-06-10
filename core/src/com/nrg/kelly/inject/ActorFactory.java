package com.nrg.kelly.inject;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrew on 10/06/2015.
 */

public interface ActorFactory {

    public Actor createEnemy(int level, int enemyCount);
    public boolean hasNextEnemy(int level, int enemy);
    public Actor createBackground(int level);
    public Actor createGround(int level);

}
