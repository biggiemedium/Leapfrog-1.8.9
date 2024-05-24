package dev.px.leapfrog.API.Event.Client;

import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.Client.Module.Setting;

public class SettingUpdateEvent extends Event {

    private Setting<?> setting;

    public SettingUpdateEvent(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }

    public void setSetting(Setting<?> setting) {
        this.setting = setting;
    }
}
