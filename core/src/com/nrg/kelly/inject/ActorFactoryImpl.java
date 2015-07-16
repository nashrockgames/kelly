package com.nrg.kelly.inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ArmourConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.EnemyConfig;
import com.nrg.kelly.config.actors.RunnerConfig;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.config.levels.LevelsConfig;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.ActorState;
import com.nrg.kelly.stages.actors.ArmourActor;
import com.nrg.kelly.stages.actors.BackgroundActor;
import com.nrg.kelly.stages.actors.BossActor;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.EnemyBulletActor;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActorFactoryImpl implements ActorFactory{

    private LevelsConfig levelsConfig;

    private CameraConfig cameraConfig;

    private int enemySeed = 0;

    private Random random;

    @Inject
    public ActorFactoryImpl(LevelsConfig levelsConfig, CameraConfig cameraConfig){
        this.cameraConfig = cameraConfig;
        this.levelsConfig = levelsConfig;
    }

    @Override
    public void reset() {
        initLevel(1);
    }

    public void initLevel(int level){

        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        this.enemySeed = levelConfig.getEnemySeed();
        random = new Random(enemySeed);

    }

    public Actor createEnemy(int level) {

        if(random==null){
            initLevel(level);
        }

        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        final List<EnemyConfig> enemies = levelConfig.getEnemies();
        final int size = enemies.size();
        final int max = size - 1;
        final int enemyIndex = random.nextInt((max - 1) + 1) + 1;
        final EnemyConfig enemyConfig = enemies.get(enemyIndex);
        final EnemyActor enemyActor = createEnemyActor(enemyConfig);
        return enemyActor;
    }

    private void setupDefaultAnimation(EnemyConfig enemyConfig, EnemyActor enemyActor, List<AtlasConfig> animations) {
        final AtlasConfig defaultAtlasConfig = enemyActor
                .getAtlasConfigByName(animations, "default");
        enemyActor.setDefaultAtlasConfig(defaultAtlasConfig);
        final TextureAtlas defaultAtlas =
                new TextureAtlas(Gdx.files.internal(defaultAtlasConfig.getAtlas()));
        enemyActor.setDefaultAnimation(new Animation(enemyConfig.getFrameRate(),
                defaultAtlas.getRegions()));
    }

    public Actor createBackground(int level){
        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        return new BackgroundActor(levelConfig.getBackground(), cameraConfig);
    }

    public Actor createGround(int level){
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        final String ground = levelConfig.getGround();
        return new GroundActor(gameConfig.getActors().getGround(), ground, cameraConfig);
    }

    public RunnerActor createRunner(){
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final RunnerConfig runnerConfig = gameConfig.getActors().getRunner();
        final Body body = Box2dFactory.getInstance().createRunner();
        RunnerActor runnerActor = new RunnerActor(runnerConfig, cameraConfig);
        body.setUserData(runnerActor);
        runnerActor.setBody(body);
        runnerActor.setActorState(ActorState.RUNNING);
        runnerActor.resetPosition();
        return runnerActor;
    }

    @Override
    public ArmourActor createArmour() {
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final ArmourConfig armourConfig = gameConfig.getActors().getArmour();
        final ArmourActor armourActor = new ArmourActor(armourConfig, cameraConfig);
        final List<AtlasConfig> atlasConfigList = armourConfig.getAnimations();
        armourActor.setDefaultAtlasConfig(armourActor.getAtlasConfigByName(atlasConfigList, "default"));
        final String armour = armourActor.getDefaultAtlasConfig().getAtlas();
        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(armour));
        armourActor.setDefaultAnimation(new Animation(armourConfig.getFrameRate(), defaultAtlas.getRegions()));
        armourActor.setLinearVelocity(new Vector2(armourConfig.getVelocityX(), 0f));
        return armourActor;
    }

    @Override
    public Actor createBoss(int level) {
        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        final EnemyConfig boss = levelConfig.getBoss();
        final BossActor enemyActor = new BossActor(boss, cameraConfig);
        final List<AtlasConfig> animations = boss.getAnimations();
        if(animations != null) {
            setupDefaultAnimation(boss, enemyActor, animations);
        }
        enemyActor.setConfiguredLinearVelocity(new Vector2(boss.getVelocityX(), 0f));
        return enemyActor;
    }

    @Override
    public EnemyBulletActor createBossBullet(int level) {
        final LevelConfig levelConfig = levelsConfig.getLevels().get(level - 1);
        final EnemyConfig bossBullet = levelConfig.getBossBullet();
        return createEnemyBullet(bossBullet);
    }

    private EnemyActor createEnemyActor(final EnemyConfig enemyConfig) {
        final EnemyActor enemyActor = new EnemyActor(enemyConfig, cameraConfig);
        final List<AtlasConfig> animations = enemyConfig.getAnimations();
        if(animations != null) {
            setupDefaultAnimation(enemyConfig, enemyActor, animations);
        }
        enemyActor.setConfiguredLinearVelocity(new Vector2(enemyConfig.getVelocityX(), 0f));
        return enemyActor;
    }

    private EnemyBulletActor createEnemyBullet(final EnemyConfig enemyConfig) {
        final EnemyBulletActor enemyBulletActor = new EnemyBulletActor(enemyConfig, cameraConfig);
        final List<AtlasConfig> animations = enemyConfig.getAnimations();
        if(animations != null) {
            setupDefaultAnimation(enemyConfig, enemyBulletActor, animations);
        }
        enemyBulletActor.setConfiguredLinearVelocity(new Vector2(enemyConfig.getVelocityX(), 0f));
        return enemyBulletActor;
    }


}
