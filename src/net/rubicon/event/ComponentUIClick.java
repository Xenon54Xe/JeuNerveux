package net.rubicon.event;

import net.rubicon.ui.UIObject;

public record ComponentUIClick(UIObject uiObject, String mouseButtonClicked) implements IEventComponent {

    public static final String LEFT_BUTTON = "left-button";
    public static final String RIGHT_BUTTON = "right-button";

    @Override
    public String getName() {
        return uiObject.getName() + "-" + mouseButtonClicked;
    }
}
