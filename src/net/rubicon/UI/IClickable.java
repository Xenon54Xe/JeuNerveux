package net.rubicon.UI;

public interface IClickable {

    public void setPayload(String payload);

    public void setActive(boolean active);

    public String getPayload();

    public void isClicked(); // Verify is clicked and then do things
}
