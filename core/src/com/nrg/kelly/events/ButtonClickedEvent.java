package com.nrg.kelly.events;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nrg.kelly.Constants;

/**
 * Created by Andrew on 21/05/2015.
 */
public class ButtonClickedEvent {

    private final Constants.BUTTON_ID buttonId;

    public ButtonClickedEvent(Constants.BUTTON_ID menuId){
        this.buttonId = menuId;
    }

    public Constants.BUTTON_ID getButtonId() {
        return buttonId;
    }


}
