package dev.px.leapfrog.API.Util.Render.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontRenderer {

    public static volatile int completed;

    private static int scale;
    private static int prevScale;

    public static MinecraftFontRenderer
            sans12, sans16, sans18, sans20, sans22, sans24, sans40,
            sans12_bold, sans16_bold, sans18_bold, sans20_bold, sans24_bold, sans30_bold, sans36_bold, sans40_bold;

    public static Font
            sans12_, sans16_, sans18_, sans20_, sans24_, sans22_, sans40_,

            sans_bold12_, sans_bold16_, sans_bold18_, sans_bold20_, sans_bold24_, sans_bold30_, sans_bold36_, sans_bold40_;

    public static void init() {

        Map<String, Font> locationMap = new HashMap<>();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        scale = sr.getScaleFactor();

        if(scale != prevScale) {
            prevScale = scale;

            FontRenderer.sans12_ = FontRenderer.getFont(locationMap, "sans.ttf", 12);
            FontRenderer.sans12 = new MinecraftFontRenderer(FontUtil.regular12_);
            FontRenderer.sans16_ = FontRenderer.getFont(locationMap, "sans.ttf", 16);
            FontRenderer.sans16 = new MinecraftFontRenderer(FontUtil.regular16_);
            FontRenderer.sans18_ = FontRenderer.getFont(locationMap, "sans.ttf", 18);
            FontRenderer.sans18 = new MinecraftFontRenderer(FontUtil.regular16_);
            FontRenderer.sans20_ = FontRenderer.getFont(locationMap, "sans.ttf", 20);
            FontRenderer.sans20 = new MinecraftFontRenderer(FontUtil.regular20_);
            FontRenderer.sans22_ = FontRenderer.getFont(locationMap, "sans.ttf", 22);
            FontRenderer.sans22 = new MinecraftFontRenderer(FontUtil.regular22_);
            FontRenderer.sans24_ = FontRenderer.getFont(locationMap, "sans.ttf", 24);
            FontRenderer.sans24 = new MinecraftFontRenderer(FontUtil.regular24_);
            FontRenderer.sans40_ = FontRenderer.getFont(locationMap, "sans.ttf", 40);
            FontRenderer.sans40 = new MinecraftFontRenderer(FontUtil.regular40_);

            FontRenderer.sans_bold12_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 12);
            FontRenderer.sans12_bold = new MinecraftFontRenderer(FontUtil.regular12_);

            FontRenderer.sans_bold16_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 16);
            FontRenderer.sans16_bold = new MinecraftFontRenderer(FontUtil.regular16_);

            FontRenderer.sans_bold18_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 18);
            FontRenderer.sans18_bold = new MinecraftFontRenderer(FontUtil.regular16_);

            FontRenderer.sans_bold20_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 20);
            FontRenderer.sans20_bold = new MinecraftFontRenderer(FontUtil.regular20_);

            FontRenderer.sans_bold24_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 24);
            FontRenderer.sans24_bold = new MinecraftFontRenderer(FontUtil.regular24_);

            FontRenderer.sans_bold30_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 30);
            FontRenderer.sans30_bold = new MinecraftFontRenderer(FontUtil.regular24_);

            FontRenderer.sans_bold36_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 36);
            FontRenderer.sans36_bold = new MinecraftFontRenderer(FontUtil.regular24_);

            FontRenderer.sans_bold30_ = FontRenderer.getFont(locationMap, "sans_bold.ttf", 40);
            FontRenderer.sans30_bold = new MinecraftFontRenderer(FontUtil.regular40_);

        }
    }

    public static Font getFont(Map<String, Font> locationMap, String location, float size) {
        Font font;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        size = size * ((float) sr.getScaleFactor() / 2);

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("Leapfrog/Fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

}
