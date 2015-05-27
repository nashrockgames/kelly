package com.nrg.kelly.config.levels;

import java.util.LinkedList;

/**
 * Created by Andrew on 27/05/2015.
 */
public class LevelsConfig {

    private LinkedList<LevelConfig> levels;

    public LinkedList<LevelConfig> getLevels() {
        return levels;
    }

    public void setLevels(LinkedList<LevelConfig> levels) {
        this.levels = levels;
    }
}
