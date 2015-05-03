package com.nrg.kelly.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.config.actors.Ground;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.config.actors.WorldGravity;

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
        final WorldGravity worldGravity = ConfigFactory.getConfig().getActors().getWorldGravity();
        world = new World(new Vector2(worldGravity.getX(), worldGravity.getY()), true);
    }

    public Body createGround() {
        BodyDef bodyDef = new BodyDef();
        final Ground ground = ConfigFactory.getConfig().getActors().getGround();
        bodyDef.position.set(new Vector2(ground.getX(), ground.getY()));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(ground.getWidth() / 2, ground.getHeight() / 2);
        body.createFixture(shape, ground.getDensity());
        shape.dispose();
        return body;
    }

    public Body createRunner() {

        final Runner runner = ConfigFactory.getConfig().getActors().getRunner();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(runner.getX(), runner.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(runner.getWidth() / 2, runner.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, runner.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }

    public static World getWorld() {
        return world;
    }
}