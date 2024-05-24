package dev.px.leapfrog.Client.Manager.Other;

import net.minecraft.client.Minecraft;

public class ServerManager {

    private Minecraft mc = Minecraft.getMinecraft();

    public boolean isOnServer() {
        if(mc.getCurrentServerData() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHypixel() {
        if(isOnServer() && mc.getCurrentServerData().serverIP.contains("hypixel")) {
            return true;
        } else {
            return false;
        }
    }

}
