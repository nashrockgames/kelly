package com.nrg.kelly.config.levels;

import com.nrg.kelly.config.actors.Enemy;

import java.util.List;

/**
 * Created by Andrew on 27/05/2015.
 */
public class LevelConfig {

    private int enemyCount;
    private int enemySeed;
    private List<Enemy> enemies;
    private String background;
    private String ground;

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
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
}
