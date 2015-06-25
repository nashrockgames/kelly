package com.nrg.kelly.events.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.google.common.base.Optional;
import com.nrg.kelly.stages.actors.ArmourActor;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;


public class BeginContactEvent {
    private final Contact contact;
    private Optional<EnemyActor> enemyActor = Optional.absent();
    private Optional<RunnerActor> runnerActor = Optional.absent();
    private Optional<GroundActor> groundActor = Optional.absent();
    private Optional<ArmourActor> armourActor = Optional.absent();


    public BeginContactEvent(Contact contact) {
        this.contact = contact;
        this.parseContact(contact);
    }

    private void parseContact(Contact contact) {
        parseUserData(contact.getFixtureA().getBody().getUserData());
        parseUserData(contact.getFixtureB().getBody().getUserData());
    }

    private void parseUserData(Object userData) {
        if(userData instanceof EnemyActor){
            this.enemyActor = Optional.of((EnemyActor)userData);
        } else if(userData instanceof RunnerActor){
            this.runnerActor = Optional.of((RunnerActor)userData);
        } else if(userData instanceof GroundActor){
            this.groundActor = Optional.of((GroundActor)userData);
        } else if(userData instanceof ArmourActor ){
            this.armourActor = Optional.of((ArmourActor)userData);
        }
    }

    public Optional<ArmourActor> getArmourActor() {
        return armourActor;
    }

    public Optional<GroundActor> getGroundActor() {
        return groundActor;
    }

    public Optional<EnemyActor> getEnemyActor() {
        return enemyActor;
    }

    public Optional<RunnerActor> getRunnerActor() {
        return runnerActor;
    }

    public Contact getContact() {
        return contact;
    }
}
