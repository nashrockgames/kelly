package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.google.common.base.Optional;

public class CollisionParams {
    private final Vector2 linearVelocity;
    private final Vector2 linearImpulse;
    private final Filter filter;
    private final Body body;
    private final Optional<Float> rotation;

    public CollisionParams(Vector2 linearVelocity,
                           Vector2 linearImpulse,
                           Filter filter,
                           Body body,
                           Optional<Float> rotation) {
        this.linearVelocity = linearVelocity;
        this.linearImpulse = linearImpulse;
        this.filter = filter;
        this.body = body;
        this.rotation = rotation;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public Vector2 getLinearImpulse() {
        return linearImpulse;
    }

    public Filter getFilter() {
        return filter;
    }

    public Body getBody() {
        return body;
    }

    public Optional<Float> getRotation() {
        return rotation;
    }
}
