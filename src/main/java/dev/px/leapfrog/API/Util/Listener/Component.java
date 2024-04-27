package dev.px.leapfrog.API.Util.Listener;

import net.minecraft.client.Minecraft;

import java.io.IOException;

public interface Component {

     Minecraft mc = Minecraft.getMinecraft();

    void render(int mouseX, int mouseY);

    void onClick(int mouseX, int mouseY, int button) throws IOException;

    void onRelease(int mouseX, int mouseY, int button);

    void onType(char typedChar, int keyCode) throws IOException;
}
