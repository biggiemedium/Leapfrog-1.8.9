package dev.px.leapfrog.Client.GUI.AltManager;

import dev.px.leapfrog.API.Account.Account;
import dev.px.leapfrog.API.Account.MicrosoftAuth;
import dev.px.leapfrog.API.Util.System.IOUtils;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class AltMicrosoftAuth extends GuiScreen {

    private GuiScreen previousScreen;
    private String state;

    private GuiButton openButton = null;
    private boolean openButtonEnabled = true;
    private GuiButton cancelButton = null;
    private String status = null;
    private String cause = null;
    private ExecutorService executor = null;
    private CompletableFuture<Void> task = null;

    public AltMicrosoftAuth(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
        this.state = RandomStringUtils.randomAlphanumeric(8);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(openButton = new GuiButton(
                0,
                width / 2 - 75 - 2,
                height / 2 + fontRendererObj.FONT_HEIGHT / 2 + fontRendererObj.FONT_HEIGHT,
                75,
                20,
                "Open"
        ));
        buttonList.add(cancelButton = new GuiButton(
                1,
                width / 2 + 2,
                height / 2 + fontRendererObj.FONT_HEIGHT / 2 + fontRendererObj.FONT_HEIGHT,
                75,
                20,
                "Cancel"
        ));

        if (task == null) {
            URI url = MicrosoftAuth.getMSAuthLink(state);
            IOUtils.setClipboard(url != null ? url.toString() : "");
            status = "&fLogin link has been copied to the clipboard!&r";

            if (executor == null) {
                executor = Executors.newSingleThreadExecutor();
            }
            AtomicReference<String> refreshToken = new AtomicReference<>("");
            AtomicReference<String> accessToken = new AtomicReference<>("");
            task = MicrosoftAuth.acquireMSAuthCode(state, executor)
                    .thenComposeAsync(msAuthCode -> {
                        openButtonEnabled = false;
                        status = "&fAcquiring Microsoft access tokens&r";
                        return MicrosoftAuth.acquireMSAccessTokens(msAuthCode, executor);
                    })
                    .thenComposeAsync(msAccessTokens -> {
                        status = "&fAcquiring Xbox access token&r";
                        refreshToken.set(msAccessTokens.get("refresh_token"));
                        return MicrosoftAuth.acquireXboxAccessToken(msAccessTokens.get("access_token"), executor);
                    })
                    .thenComposeAsync(xboxAccessToken -> {
                        status = "&fAcquiring Xbox XSTS token&r";
                        return MicrosoftAuth.acquireXboxXstsToken(xboxAccessToken, executor);
                    })
                    .thenComposeAsync(xboxXstsData -> {
                        status = "&fAcquiring Minecraft access token&r";
                        return MicrosoftAuth.acquireMCAccessToken(
                                xboxXstsData.get("Token"), xboxXstsData.get("uhs"), executor
                        );
                    })
                    .thenComposeAsync(mcToken -> {
                        status = "&fFetching your Minecraft profile&r";
                        accessToken.set(mcToken);
                        return MicrosoftAuth.login(mcToken, executor);
                    })
                    .thenAccept(session -> {
                        status = null;
                        Account acc = new Account(
                                refreshToken.get(), accessToken.get(), session.getUsername()
                        );
                        for (Account account : LeapFrog.accountManager.accounts) {
                            if (acc.getUsername().equals(account.getUsername())) {
                                acc.setUnban(account.getUnban());
                                break;
                            }
                        }
                        LeapFrog.accountManager.accounts.add(acc);
                        LeapFrog.accountManager.save();
                        set(session);
                        mc.displayGuiScreen(new AltManagerGui(previousScreen));
                    })
                    .exceptionally(error -> {
                        openButtonEnabled = false;
                        status = String.format("&c%s&r", error.getMessage());
                        cause = String.format("&c%s&r", error.getCause().getMessage());
                        return null;
                    });
        }
    }

    @Override
    public void onGuiClosed() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
            executor.shutdownNow();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (openButton != null) {
            openButton.enabled = openButtonEnabled;
        }
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawCenteredString(
                fontRendererObj, "Microsoft Authentication",
                width / 2, height / 2 - fontRendererObj.FONT_HEIGHT / 2 - fontRendererObj.FONT_HEIGHT * 2, 11184810
        );

        if (status != null) {
            drawCenteredString(
                    fontRendererObj, IOUtils.TextFormatting.translate(status),
                    width / 2, height / 2 - fontRendererObj.FONT_HEIGHT / 2, -1
            );
        }

        if (cause != null) {
            String causeText = IOUtils.TextFormatting.translate(cause);
            Gui.drawRect(
                    0, height - 2 - fontRendererObj.FONT_HEIGHT - 3,
                    3 + mc.fontRendererObj.getStringWidth(causeText) + 3, height,
                    0x64000000
            );
            drawString(
                    fontRendererObj, IOUtils.TextFormatting.translate(cause),
                    3, height - 2 - fontRendererObj.FONT_HEIGHT, -1
            );
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            actionPerformed(cancelButton);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == null) {
            return;
        }

        if (button.enabled) {
            switch (button.id) {
                case 0: { // Open
                    IOUtils.openWebLink(MicrosoftAuth.getMSAuthLink(state));
                }
                break;
                case 1: { // Cancel
                    mc.displayGuiScreen(new AltManagerGui(previousScreen));
                }
                break;
            }
        }
    }
    private Field field = null;

    private Field getField() {
        if (field == null) {
            try {
                for (Field f : Minecraft.class.getDeclaredFields()) {
                    if (f.getType().isAssignableFrom(Session.class)) {
                        field = f;
                        field.setAccessible(true);
                        break;
                    }
                }
            } catch (Exception e) {
                field = null;
            }
        }

        return field;
    }

    public Session get() {
        return mc.getSession();
    }

    public void set(Session session) {
        try {
            getField().set(mc, session);
        } catch (Exception e) {
            //
        }
    }
}
