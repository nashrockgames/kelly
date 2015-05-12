package com.nrg.kelly.screens;

import com.google.common.base.Predicate;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.menus.Menu;

/**
 * Created by Andrew on 12/05/2015.
 */
public class MenuIdFilter implements Predicate<Menu> {

    private String id;

    public MenuIdFilter(String id){
        this.id = id;
    }

    public MenuIdFilter(Constants.MENU_IDS main) {
        this(main.toString());
    }

    @Override
    public boolean apply(Menu input) {
        return input.getId().equalsIgnoreCase(id);
    }
}
