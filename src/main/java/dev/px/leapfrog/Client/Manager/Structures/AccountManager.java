package dev.px.leapfrog.Client.Manager.Structures;

import com.google.gson.*;
import dev.px.leapfrog.API.Account.Account;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class AccountManager {

    private Minecraft mc = Minecraft.getMinecraft();
    private File file = new File(LeapFrog.fileManager.getDirectory(), "accounts.json");
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public ArrayList<Account> accounts = new ArrayList<>();

    public AccountManager() {

        if (!file.exists()) {
            try {
                if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                    if (file.createNewFile()) {
                        System.out.print("Successfully created accounts.json!");
                    }
                }
            } catch (IOException e) {
                System.err.print("Couldn't create accounts.json!");
            }
        }
    }


    public void load() {
        accounts.clear();
        try {
            JsonElement json = new JsonParser().parse(
                    new BufferedReader(new FileReader(file))
            );
            if (json instanceof JsonArray) {
                JsonArray jsonArray = json.getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    accounts.add(new Account(
                            Optional.ofNullable(jsonObject.get("refreshToken")).map(JsonElement::getAsString).orElse(""),
                            Optional.ofNullable(jsonObject.get("accessToken")).map(JsonElement::getAsString).orElse(""),
                            Optional.ofNullable(jsonObject.get("username")).map(JsonElement::getAsString).orElse(""),
                            Optional.ofNullable(jsonObject.get("unban")).map(JsonElement::getAsLong).orElse(0L)
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print("Couldn't find accounts.json!");
        }
    }

    public void save() {
        try {
            JsonArray jsonArray = new JsonArray();
            for (Account account : accounts) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("refreshToken", account.getRefreshToken());
                jsonObject.addProperty("accessToken", account.getAccessToken());
                jsonObject.addProperty("username", account.getUsername());
                jsonObject.addProperty("unban", account.getUnban());
                jsonArray.add(jsonObject);
            }
            PrintWriter printWriter = new PrintWriter(new FileWriter(file));
            printWriter.println(gson.toJson(jsonArray));
            printWriter.close();
        } catch (IOException e) {
            System.err.print("Couldn't save accounts.json!");
        }
    }
}
