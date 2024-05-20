package dev.px.leapfrog.Client.Manager.Other;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapeManager {

    private List<UUID> devUUID = new ArrayList<>();

    public CapeManager() {
        getUsers("https://pastebin.com/sAuT09rF", devUUID);
    }

    private void getUsers(String link, List<UUID> list) {
        try {
            URL url = new URL(link);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line;
            while((line = reader.readLine()) != null) {
                list.add(UUID.fromString(line));
            }
        } catch (Exception ignored) {}
    }

    public boolean hasDevCape(UUID id) {
        return devUUID.contains(id);
    }

    public List<UUID> getDevUUID() {
        return devUUID;
    }
}
