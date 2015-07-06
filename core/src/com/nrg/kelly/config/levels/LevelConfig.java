package com.nrg.kelly.config.levels;

import com.nrg.kelly.config.actors.Enemy;

import java.util.List;

/**
 * Created by Andrew on 27/05/2015.
 */
public class LevelConfig {

    private int enemySeed;
    private List<Enemy> enemies;
    private String background;
    private String ground;
    private Enemy boss;
    private Enemy bossBullet;

    public Enemy getBossBullet() {
        return bossBullet;
    }

    public void setBossBullet(Enemy bossBullet) {
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

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Enemy getBoss() {
        return boss;
    }

    public void setBoss(Enemy boss) {
        this.boss = boss;
    }
}
