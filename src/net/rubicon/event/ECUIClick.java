package net.rubicon.event;

import net.rubicon.UI.UIObject;

public record ECUIClick(UIObject uiObject, String buttonClicked) implements IEventComponent {

    public static final String LEFT_BUTTON = "left-button";
    public static final String RIGHT_BUTTON = "right-button";

    @Override
    public String getName() {
        return uiObject.getName() + "-" + buttonClicked;
    }
}
