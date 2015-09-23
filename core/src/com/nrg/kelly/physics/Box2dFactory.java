package com.nrg.kelly.physics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.actors.ArmourConfig;
import com.nrg.kelly.config.actors.BossDeathConfig;
import com.nrg.kelly.config.actors.GunConfig;
import com.nrg.kelly.config.actors.PositionConfig;
import com.nrg.kelly.config.actors.RunnerConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.inject.ConfigFactory;
import com.nrg.kelly.config.actors.GroundConfig;
import com.nrg.kelly.config.actors.WorldGravityConfig;
import com.nrg.kelly.config.actors.EnemyConfig;
import com.nrg.kelly.stages.actors.GameActor;

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
            final GameConfig gameConfig = ConfigFactory.getGameConfig();
            final RunnerConfig runnerConfig = gameConfig.getActors().getRunner();
            runnerJumpingLinearImpulse = new Vector2(runnerConfig.getJumpImpulseX(),
                    runnerConfig.getJumpImpulseY());
            slideAngle = (float)(90f * (Math.PI / 180f));
            final PositionConfig positionConfig = runnerConfig.getPosition();
            slidePosition = new Vector2(positionConfig.getX(), positionConfig.getY() - runnerConfig.getWidth() / 2);
            instance = new Box2dFactory();
        }
        return instance;
    }

    public ApplicationListener buildGame(){
        createWorld();
        final DaggerGameComponent.Builder builder = DaggerGameComponent.builder();
        final GameComponent gameComponent = builder.build();
        return gameComponent.getKellyGame();
    }

    private void createWorld() {
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final WorldGravityConfig worldGravityConfig = gameConfig.getActors().getWorldGravity();
        world = new World(new Vector2(worldGravityConfig.getX(), worldGravityConfig.getY()), true);
    }

    public Body createGround() {
        final BodyDef bodyDef = new BodyDef();
        final GroundConfig groundConfig = ConfigFactory.getGameConfig().getActors().getGround();
        final PositionConfig positionConfig = groundConfig.getPosition();
        bodyDef.position.set(new Vector2(positionConfig.getX(), positionConfig.getY()));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(groundConfig.getWidth() / 2, groundConfig.getHeight() / 2);
        body.createFixture(shape, groundConfig.getDensity());
        shape.dispose();
        return body;
    }

    public Body createBossDeath(BossDeathConfig  bossDeathConfig){

        final PositionConfig positionConfig = bossDeathConfig.getPosition();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Vector2 configuredPosition = new Vector2(positionConfig.getX(), positionConfig.getY());
        bodyDef.position.set(configuredPosition);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bossDeathConfig.getWidth() / 2, bossDeathConfig.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(bossDeathConfig.getGravityScale());
        body.createFixture(shape, bossDeathConfig.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;

    }

    public Body createRunner() {
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final RunnerConfig runnerConfig = gameConfig.getActors().getRunner();
        final PositionConfig positionConfig = runnerConfig.getPosition();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        runPosition = new Vector2(positionConfig.getX(), positionConfig.getY());
        bodyDef.position.set(runPosition);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(runnerConfig.getWidth() / 2, runnerConfig.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.setGravityScale(runnerConfig.getGravityScale());
        body.createFixture(shape, runnerConfig.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }

    public static Body createEnemy(final EnemyConfig enemyConfig){
        //Gdx.app.log("-", "Creating " + enemyConfig.getClass().getName());
        final PositionConfig positionConfig = enemyConfig.getPosition();
        final PolygonShape shape = new PolygonShape();
        final float hitBoxScale = enemyConfig.getHitBoxScale();
        final Vector2 positionVector = new Vector2(positionConfig.getX(), positionConfig.getY());
        final BodyDef bodyDef = createEnemyBodyDef(positionVector);
        //Gdx.app.log("-", "Created body def for " + enemyConfig.getClass().getName());
        shape.setAsBox((enemyConfig.getWidth() / 2) * hitBoxScale,
                (enemyConfig.getHeight() / 2) * hitBoxScale);
        final Body body = world.createBody(bodyDef);
        body.setGravityScale(enemyConfig.getGravityScale());
        body.createFixture(shape, enemyConfig.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }

    private static BodyDef createEnemyBodyDef(final Vector2 positionVector) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(positionVector);
        return bodyDef;
    }

    public Vector2 getRunnerJumpImpulse(){
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

    private static void destroyBody(final Body body) {
        final Array<JointEdge> jointList = body.getJointList();
        for(JointEdge jointEdge : jointList){
            getWorld().destroyJoint(jointEdge.joint);
        }
        getWorld().destroyBody(body);
    }

    public static void destroyAndRemove(final GameActor actor){
        //Gdx.app.log("-", "destroying " + actor.getConfig().get().toString());
        Events.get().unregister(actor);
        final Body body = actor.getBody();
        body.setUserData(null);
        destroyBody(body);
        actor.remove();
        //Gdx.app.log("-", "destroyed " + actor.getClass().getName());
    }

    public Body createArmour(ArmourConfig armour) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        final PositionConfig positionConfig = armour.getPosition();
        bodyDef.position.set(new Vector2(positionConfig.getX(), positionConfig.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(armour.getWidth() / 2, armour.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, armour.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }

    public Body createGun(GunConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        final PositionConfig positionConfig = config.getPosition();
        bodyDef.position.set(new Vector2(positionConfig.getX(), positionConfig.getY()));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(config.getWidth() / 2, config.getHeight() / 2);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, config.getDensity());
        body.resetMassData();
        shape.dispose();
        return body;
    }
}