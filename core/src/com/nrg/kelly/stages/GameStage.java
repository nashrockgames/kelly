package com.nrg.kelly.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.events.game.PostConstructGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.SceneFactory;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;
import com.nrg.kelly.stages.text.DebugText;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by Andrew on 26/04/2015.
 */
public class GameStage extends Stage{

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    @Inject
    RunnerActor runner;

    @Inject
    GroundActor ground;

    @Inject
    Config gameConfig;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    @Inject
    public GameStage() {
        renderer = new Box2DDebugRenderer();
        setupCamera();
        Events.get().register(this);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    @Subscribe
    public void setupActors(PostConstructGameEvent postConstructGameEvent){
        this.addActor(this.ground);
        this.addActor(this.runner);
        if(gameConfig.getSettings().isDebug()) {
            this.addActor(new DebugText());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            SceneFactory.getWorld().step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(SceneFactory.getWorld(), camera.combined);
    }

}