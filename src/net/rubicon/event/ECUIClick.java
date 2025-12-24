package net.rubicon.event;

import net.rubicon.UI.UIObject;

public class ECUIClick implements IEventComponent {
    
    public static final String LEFT_BUTTON = "left-button";
    public static final String RIGHT_BUTTON = "right-button";
    
    public final UIObject uiObject;
    public final String buttonClicked;
    
    public ECUIClick(UIObject uiObject, String buttonClicked){
        this.uiObject = uiObject;
        this.buttonClicked = buttonClicked;
    }

    @Override
    public String getName() {
        return uiObject.getName() + "-" + buttonClicked;
    }
}
