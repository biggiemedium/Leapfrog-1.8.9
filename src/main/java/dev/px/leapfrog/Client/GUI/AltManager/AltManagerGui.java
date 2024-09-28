package dev.px.leapfrog.Client.GUI.AltManager;

import dev.px.leapfrog.API.Account.Account;
import dev.px.leapfrog.API.Account.MicrosoftAuth;
import dev.px.leapfrog.API.Util.System.IOUtils;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.Session;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import java.io.IOException;

public class AltManagerGui extends GuiScreen {

    private GuiScreen previousScreen;

    private GuiButton loginButton = null;
    private GuiButton deleteButton = null;
    private GuiButton cancelButton = null;
    private GuiAccountList guiAccountList = null;
    private int selectedAccount = -1;
    private ExecutorService executor = null;
    private CompletableFuture<Void> task = null;

    public AltManagerGui(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        LeapFrog.accountManager.load();
        Keyboard.enableRepeatEvents(true);

        buttonList.clear();
        buttonList.add(loginButton = new GuiButton(
                0, width / 2 - 150 - 4, height - 52, 150, 20, "Login"
        ));
        buttonList.add(new GuiButton(
                1, width / 2 + 4, height - 52, 150, 20, "Add"
        ));
        buttonList.add(deleteButton = new GuiButton(
                2, width / 2 - 150 - 4, height - 28, 150, 20, "Delete"
        ));
        buttonList.add(cancelButton = new GuiButton(
                3, width / 2 + 4, height - 28, 150, 20, "Cancel"
        ));

        guiAccountList = new GuiAccountList(mc);
        guiAccountList.registerScrollButtons(11, 12);

        updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        if (task != null && !task.isDone()) {
            task.cancel(true);
            executor.shutdownNow();
        }
    }

    @Override
    public void updateScreen() {
        if (loginButton != null && deleteButton != null) {
            loginButton.enabled = deleteButton.enabled = selectedAccount >= 0;
            if (task != null && !task.isDone()) {
                loginButton.enabled = false;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
        if (guiAccountList != null) {
            guiAccountList.drawScreen(mouseX, mouseY, renderPartialTicks);
        }
        super.drawScreen(mouseX, mouseY, renderPartialTicks);

        drawCenteredString(
                fontRendererObj,
                IOUtils.TextFormatting.translate(String.format(
                        "&rAccount Manager &8(&7%s&8)&r", LeapFrog.accountManager.accounts.size()
                )),
                width / 2, 20, -1
        );

        String text = IOUtils.TextFormatting.translate(String.format(
                "&7Username: &3%s&r", get().getUsername()
        ));
        mc.currentScreen.drawString(mc.fontRendererObj, text, 3, 3, -1);
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (guiAccountList != null) {
            guiAccountList.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        switch (keyCode) {
            case Keyboard.KEY_UP: {
                if (selectedAccount > 0) {
                    --selectedAccount;
                    if (isCtrlKeyDown()) {
                        Collections.swap(LeapFrog.accountManager.accounts, selectedAccount, selectedAccount + 1);
                        LeapFrog.accountManager.save();
                    }
                }
            }
            break;
            case Keyboard.KEY_DOWN: {
                if (selectedAccount < LeapFrog.accountManager.accounts.size() - 1) {
                    ++selectedAccount;
                    if (isCtrlKeyDown()) {
                        Collections.swap(LeapFrog.accountManager.accounts, selectedAccount, selectedAccount - 1);
                        LeapFrog.accountManager.save();
                    }
                }
            }
            break;
            case Keyboard.KEY_RETURN: {
                actionPerformed(loginButton);
            }
            break;
            case Keyboard.KEY_DELETE: {
                actionPerformed(deleteButton);
            }
            break;
            case Keyboard.KEY_ESCAPE: {
                actionPerformed(cancelButton);
            }
            break;
        }

        if (isKeyComboCtrlC(keyCode) && selectedAccount >= 0) {
            setClipboardString(LeapFrog.accountManager.accounts.get(selectedAccount).getUsername());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == null) {
            return;
        }

        if (button.enabled) {
            switch (button.id) {
                case 0: { // Login
                    if (task == null || task.isDone()) {
                        if (executor == null) {
                            executor = Executors.newSingleThreadExecutor();
                        }
                        Account account = LeapFrog.accountManager.accounts.get(selectedAccount);
                        String username = StringUtils.isBlank(account.getUsername()) ? "???" : account.getUsername();
                        AtomicReference<String> refreshToken = new AtomicReference<>("");
                        AtomicReference<String> accessToken = new AtomicReference<>("");
                        task = MicrosoftAuth.login(account.getAccessToken(), executor)
                                .handle((session, error) -> {
                                    if (session != null) {
                                        account.setUsername(session.getUsername());
                                        LeapFrog.accountManager.save();
                                        set(session);
                                        return true;
                                    }
                                    return false;
                                })
                                .thenComposeAsync(completed -> {
                                    if (completed) {
                                        throw new NoSuchElementException();
                                    }
                                    return MicrosoftAuth.refreshMSAccessTokens(account.getRefreshToken(), executor);
                                })
                                .thenComposeAsync(msAccessTokens -> {
                                    refreshToken.set(msAccessTokens.get("refresh_token"));
                                    return MicrosoftAuth.acquireXboxAccessToken(msAccessTokens.get("access_token"), executor);
                                })
                                .thenComposeAsync(xboxAccessToken -> {
                                    return MicrosoftAuth.acquireXboxXstsToken(xboxAccessToken, executor);
                                })
                                .thenComposeAsync(xboxXstsData -> {
                                    return MicrosoftAuth.acquireMCAccessToken(
                                            xboxXstsData.get("Token"), xboxXstsData.get("uhs"), executor
                                    );
                                })
                                .thenComposeAsync(mcToken -> {
                                    accessToken.set(mcToken);
                                    return MicrosoftAuth.login(mcToken, executor);
                                })
                                .thenAccept(session -> {
                                    account.setRefreshToken(refreshToken.get());
                                    account.setAccessToken(accessToken.get());
                                    account.setUsername(session.getUsername());
                                    LeapFrog.accountManager.save();
                                    set(session);
                                })
                                .exceptionally(error -> {
                                    if (!(error.getCause() instanceof NoSuchElementException)) {
                                        
                                    }
                                    return null;
                                });
                    }
                }
                break;
                case 1: { // Add
                    mc.displayGuiScreen(new AltMicrosoftAuth(previousScreen));
                }
                break;
                case 2: { // Delete
                    if (selectedAccount > -1 && selectedAccount < LeapFrog.accountManager.accounts.size()) {
                        LeapFrog.accountManager.accounts.remove(selectedAccount);
                        LeapFrog.accountManager.save();
                        selectedAccount = -1;
                        updateScreen();
                    }
                }
                break;
                case 3: { // Cancel
                    mc.displayGuiScreen(previousScreen);
                }
                break;
                default: {
                    guiAccountList.actionPerformed(button);
                }
            }
        }
    }

    class GuiAccountList extends GuiSlot {
        public GuiAccountList(Minecraft mc) {
            super(
                    mc, AltManagerGui.this.width, AltManagerGui.this.height,
                    32, AltManagerGui.this.height - 64, 16
            );
        }

        @Override
        protected int getSize() {
            return LeapFrog.accountManager.accounts.size();
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return slotIndex == AltManagerGui.this.selectedAccount;
        }

        @Override
        protected int getScrollBarX() {
            return (this.width + getListWidth()) / 2 + 2;
        }

        @Override
        public int getListWidth() {
            return (150 + 4) * 2;
        }

        @Override
        protected int getContentHeight() {
            return LeapFrog.accountManager.accounts.size() * 16;
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            AltManagerGui.this.selectedAccount = slotIndex;
            AltManagerGui.this.updateScreen();
            if (isDoubleClick) {
                AltManagerGui.this.actionPerformed(loginButton);
            }
        }

        @Override
        protected void drawBackground() {
            AltManagerGui.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int entryID, int x, int y, int k, int mouseXIn, int mouseYIn) {
            FontRenderer fr = AltManagerGui.this.fontRendererObj;
            Account account = LeapFrog.accountManager.accounts.get(entryID);

            String username = account.getUsername();
            if (StringUtils.isBlank(username)) {
                username = "&7&l?";
            } else if (username.equals(get().getUsername())) {
                username = String.format("&a&l%s", username);
            }
            username = IOUtils.TextFormatting.translate(
                    String.format("&r%s&r", username)
            );
            AltManagerGui.this.drawString(
                    fr, username, x + 2, y + 2, -1
            );

            long currentTime = System.currentTimeMillis();
            long unbanTime = account.getUnban();
            String unban;
            if (unbanTime < 0L) {
                unban = "&4&l⚠";
            } else if (unbanTime <= currentTime) {
                unban = "&2&l✔";
            } else {
                long diff = unbanTime - currentTime;
                long s = (diff / 1000L) % 60L;
                long m = (diff / 60000L) % 60L;
                long h = (diff / 3600000L) % 24L;
                long d = (diff / 86400000L);
                unban = String.format(
                        "%s%s%s%s",
                        d > 0L ? String.format("%dd", d) : "",
                        h > 0L ? String.format(" %dh", h) : "",
                        m > 0L ? String.format(" %dm", m) : "",
                        s > 0L ? String.format(" %ds", s) : ""
                );
                unban = unban.trim();
                unban = String.format("%s &c&l⚠", unban);
            }
            unban = IOUtils.TextFormatting.translate(
                    String.format("&r%s&r", unban)
            );
            AltManagerGui.this.drawString(
                    fr, unban, x + getListWidth() - 5 - fr.getStringWidth(unban), y + 2, -1
            );
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
