/*
package dev.px.leapfrog.Client.Manager.Other;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;
import dev.px.leapfrog.API.Gui.CustomMainMenu;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

import java.util.Objects;
import java.util.Random;

public class DiscordManager {

    private DiscordEventHandlers handlers = new DiscordEventHandlers();
    private DiscordRichPresence presence = new DiscordRichPresence();
    private DiscordUser user;
    private DiscordRPC rpc;
    public Thread thread;

    private String applicationId = "830252222372773908";
    private String steamId = "";

    private Minecraft mc = Minecraft.getMinecraft();

    public DiscordManager() {
        this.rpc = DiscordRPC.INSTANCE;
        this.user = new DiscordUser();
    }

    public void start() {
        System.out.println("Starting RPC");
        handlers.ready = (user) -> System.out.println("Ready to start RPC!");
        rpc.Discord_Initialize(applicationId, handlers, true, steamId);

        rpc.Discord_UpdatePresence(presence);
        presence.largeImageKey = "leapfrog512";
        this.thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                presence.details = setDetails();
                presence.state = setState();

                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "LeapFrog-RPC-Handler");
        thread.start();
    }

    public void shutDown() {
        System.out.println("Shutting Down RPC");
        if(!this.thread.isInterrupted() || thread.isAlive()) {
            this.thread.interrupt();
            this.rpc.Discord_Shutdown();
            this.rpc.Discord_ClearPresence();
            return;
        }
    }

    private String setState() {
        String s = "";

        if(mc.currentScreen instanceof GuiMainMenu || mc.currentScreen instanceof CustomMainMenu) {
            s = "Idle...";
        } else if(LeapFrog.spotifyManager.getAPI().isInitialized() && LeapFrog.spotifyManager.getAPI().isPlaying()) {
            s = "Listening to " + LeapFrog.spotifyManager.getAPI().getTrack().getName() + " | " + LeapFrog.spotifyManager.getAPI().getTrack().getArtist();
        } else {
            //s = "LeapFrog on top!";
            Random r = new Random();
            int index = r.nextInt(states().length);
            s = states()[index];
        }

        return s;
    }

    private String[] states() {
        return new String[] {
                "LeapFrog on top!",
                "New bypasses!",
                "Sponsored by Kermit!"
        };
    }

    private String setDetails() {
        String detail = presence.details;

        if(mc.thePlayer == null)
            return "Main Menu";

        if(mc.isSingleplayer()) {
            return mc.thePlayer.getName() + " | " + "Singleplayer";
        }

        if(mc.thePlayer != null && !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
            return mc.thePlayer.getName() + " | " + "Multiplayer";
        }

        return detail;
    }
}

 */
