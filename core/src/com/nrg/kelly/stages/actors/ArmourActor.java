package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.ArmourConfig;
import com.nrg.kelly.events.game.ArmourPickedUpEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;

public class ArmourActor extends GameActor{

    private Vector2 linearVelocity;
    private boolean pickedUp = false;

    public ArmourActor(ArmourConfig config, CameraConfig cameraConfig) {
        super(config, cameraConfig);
        Events.get().register(this);
        final Body body = Box2dFactory.getInstance().createArmour(config);
        body.setUserData(this);
        setBody(body);
        setWidth(config.getWidth());
        setHeight(config.getHeight());
    }

    @Subscribe
    public void pickedUp(ArmourPickedUpEvent armourPickedUpEvent){
        this.pickedUp = true;
    }

    public void setLinearVelocity(Vector2 linearVelocity){
        this.linearVelocity = linearVelocity;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.getBody().setLinearVelocity(linearVelocity);
        final Body body = this.getBody();
        for(ActorConfig actorConfig : this.getConfig().asSet()) {
            if (pickedUp || !(body.getPosition().x + actorConfig.getWidth() / 2 > 0)) {
                    Box2dFactory.destroyBody(body);
                    this.remove();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        this.drawDefaultAnimation(batch);
    }

}
