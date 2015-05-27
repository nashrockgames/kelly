package com.nrg.kelly.events.menus;

import com.nrg.kelly.Constants;


public class ButtonClickedEvent {

    private final Constants.BUTTON_ID buttonId;

    public ButtonClickedEvent(Constants.BUTTON_ID menuId){
        this.buttonId = menuId;
    }

    public Constants.BUTTON_ID getButtonId() {
        return buttonId;
    }


}
