package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;

@Module.ModuleInterface(name = "Client Spoofer", type = Type.Misc, description = "Spoofs if you are playing on a modified client")
public class ClientSpoofer extends Module {

    public ClientSpoofer() {

    }

    public Setting<ClientType> type = create(new Setting<>("Mode", ClientType.Vanilla));

    @Override
    public void onEnable() {
        ChatUtil.sendClientSideMessage("Leave server and rejoin to use");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public enum ClientType {
        Vanilla,
        Forge,
        Lunar
    }
}

