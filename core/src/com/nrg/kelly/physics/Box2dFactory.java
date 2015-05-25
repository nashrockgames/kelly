package com.nrg.kelly.physics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.config.actors.Ground;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.config.actors.WorldGravity;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Box2dFactory {

    private static World world;

    private static Box2dFactory instance;

    public static Vector2 runnerJumpingLinearImpulse;
    private static Vector2 slidePosition;
    private static float slideAngle;
    private static Vector2 runPosition;

    private Box2dFactory(){

    }

    public static Box2dFactory getInstance(){
        if(instance==null) {
            final Config config = ConfigFactory.getConfig();
            final Runner runner = config.getActors().getRunner();
            runnerJumpingLinearImpulse = new Vector2(runner.getJumpImpulseX(),
                    runner.getJumpImpulseY());
            slideAngle = (float)(90f * (Math.PI / 180f));
            slidePosition = new Vector2(runner.getSlideX(), runner.getSlideY());
            instance = new Box2dFactory();
        }
        return instance;
    }

    public ApplicationListener buildGame(){
        final Config config = ConfigFactory.getConfig();
        final WorldGravity worldGravity = config.getActors().getWorldGravity();
        world = new World(new Vector2(worldGravity.getX(), worldGravity.getY()), true);
        final DaggerGameComponent.Builder builder = DaggerGameComponent.builder();
        final GameComponent gameComponent = builder.build();
        return gameComponent.getKellyGame();

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
        final Config config = ConfigFactory.getConfig();
        final Runner runner = config.getActors().getRunner();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        runPosition = new Vector2(runner.getX(), runner.getY());
        bodyDef.position.set(runPosition);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(runner.getWidth() / 2, runner.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(runner.getGravityScale());
        body.createFixture(shape, runner.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }

    public Vector2 getRunnerLinerImpulse(){
        return runnerJumpingLinearImpulse;
    }



    public static World getWorld() {
        return world;
    }

    public Vector2 getRunPosition() {
        return runPosition;
    }


    public Vector2 getSlidePosition() {
        return slidePosition;
    }


    public float getSlideAngle() {
        return slideAngle;
    }
}