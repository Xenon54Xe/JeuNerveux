package net.rubicon.event;

public class UIClickEvent extends Event<String, String>{

    @Override
    public void trigger(String payload) {
        for (IListener<String> listener : listeners){
            listener.onTrigger(payload);
        }
    }
}
