package net.rubicon.UI;

public interface IClickable {

    void setPayload(String payload);

    void setActive(boolean active);

    String getPayload();

    void isClicked(); // Verify is clicked and then do things
}
