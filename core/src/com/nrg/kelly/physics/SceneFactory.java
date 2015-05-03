package com.nrg.kelly.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.PostConstructGameEvent;
import com.nrg.kelly.events.PreConstructGameEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by Andrew on 26/04/2015.
 */
public class SceneFactory {

    private static World world;

    private static SceneFactory instance;

    private SceneFactory(){

    }

    public static SceneFactory getInstance(){
        if(instance==null) {
            instance = new SceneFactory();
        }
        return instance;
    }

    public void init(){
        world = new World(Constants.WORLD_GRAVITY, true);
    }

    public Body createGround(Config config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.GROUND_X, Constants.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.GROUND_WIDTH / 2, Constants.GROUND_HEIGHT / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);
        shape.dispose();
        return body;
    }

    public Body createRunner(Config config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.resetMassData();
        shape.dispose();
        return body;
    }

    public static World getWorld() {
        return world;
    }
}