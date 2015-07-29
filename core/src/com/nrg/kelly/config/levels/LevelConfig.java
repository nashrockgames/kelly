package com.nrg.kelly.config.levels;

import com.nrg.kelly.config.actors.BossBombConfig;
import com.nrg.kelly.config.actors.BossBulletConfig;
import com.nrg.kelly.config.actors.EnemyBossConfig;
import com.nrg.kelly.config.actors.EnemyConfig;

import java.util.List;

/**
 * Created by Andrew on 27/05/2015.
 */
public class LevelConfig {

    private int enemySeed;
    private List<EnemyConfig> enemies;
    private String background;
    private String ground;
    private EnemyBossConfig boss;
    private BossBulletConfig bossBullet;
    private BossBombConfig bossBomb;

    public BossBombConfig getBossBomb() {
        return bossBomb;
    }

    public void setBossBomb(BossBombConfig bossBomb) {
        this.bossBomb = bossBomb;
    }

    public BossBulletConfig getBossBullet() {
        return bossBullet;
    }

    public void setBossBullet(BossBulletConfig bossBullet) {
        this.bossBullet = bossBullet;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public int getEnemySeed() {
        return enemySeed;
    }

    public void setEnemySeed(int enemySeed) {
        this.enemySeed = enemySeed;
    }

    public List<EnemyConfig> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemyConfig> enemies) {
        this.enemies = enemies;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public EnemyBossConfig getBoss() {
        return boss;
    }

    public void setBoss(EnemyBossConfig boss) {
        this.boss = boss;
    }
}
