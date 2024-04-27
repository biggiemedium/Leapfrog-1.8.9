package dev.px.leapfrog.API.Util.Render.Font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

/**
 * @author eldoDebug
 */
public class FontUtil {

    public static volatile int completed;

    private static int scale;
    private static int prevScale;

    public static MinecraftFontRenderer
            regular12,
            regular16,
            regular20,
            regular22,
            regular24,
            regular40,
            regular_bold18, regular_bold20,
            regular_bold22, regular_bold26,
            regular_bold24,
            regular_bold30, regular_bold36,
            regular_bold40,
            icon18, icon20, icon24;

    public static Font
            regular12_, regular16_,
            regular20_, regular24_,
            regular22_,
            regular40_,
            regular_bold18_, regular_bold20_,
            regular_bold22_, regular_bold26_,
            regular_bold24_,
            regular_bold30_, regular_bold36_,
            regular_bold40_,
            icon18_, icon20_, icon24_;

    public static void init() {

        Map<String, Font> locationMap = new HashMap<>();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        scale = sr.getScaleFactor();

        if(scale != prevScale) {
            prevScale = scale;

            FontUtil.regular12_ = FontUtil.getFont(locationMap, "regular.ttf", 12);
            FontUtil.regular12 = new MinecraftFontRenderer(FontUtil.regular12_);
            FontUtil.regular16_ = FontUtil.getFont(locationMap, "regular.ttf", 16);
            FontUtil.regular16 = new MinecraftFontRenderer(FontUtil.regular16_);
            FontUtil.regular20_ = FontUtil.getFont(locationMap, "regular.ttf", 20);
            FontUtil.regular20 = new MinecraftFontRenderer(FontUtil.regular20_);
            FontUtil.regular22_ = FontUtil.getFont(locationMap, "regular.ttf", 22);
            FontUtil.regular22 = new MinecraftFontRenderer(FontUtil.regular22_);
            FontUtil.regular24_ = FontUtil.getFont(locationMap, "regular.ttf", 24);
            FontUtil.regular24 = new MinecraftFontRenderer(FontUtil.regular24_);
            FontUtil.regular40_ = FontUtil.getFont(locationMap, "regular.ttf", 40);
            FontUtil.regular40 = new MinecraftFontRenderer(FontUtil.regular40_);

            FontUtil.regular_bold18_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 18);
            FontUtil.regular_bold18 = new MinecraftFontRenderer(FontUtil.regular_bold18_);
            FontUtil.regular_bold20_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 20);
            FontUtil.regular_bold20 = new MinecraftFontRenderer(FontUtil.regular_bold20_);
            FontUtil.regular_bold22_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 22);
            FontUtil.regular_bold22 = new MinecraftFontRenderer(FontUtil.regular_bold22_);
            FontUtil.regular_bold24_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 24);
            FontUtil.regular_bold24 = new MinecraftFontRenderer(FontUtil.regular_bold24_);
            FontUtil.regular_bold26_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 26);
            FontUtil.regular_bold26 = new MinecraftFontRenderer(FontUtil.regular_bold26_);
            FontUtil.regular_bold30_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 30);
            FontUtil.regular_bold30 = new MinecraftFontRenderer(FontUtil.regular_bold30_);
            FontUtil.regular_bold36_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 36);
            FontUtil.regular_bold36 = new MinecraftFontRenderer(FontUtil.regular_bold36_);
            FontUtil.regular_bold40_ = FontUtil.getFont(locationMap, "regular_bold.ttf", 40);
            FontUtil.regular_bold40 = new MinecraftFontRenderer(FontUtil.regular_bold40_);

            FontUtil.icon18_ = FontUtil.getFont(locationMap, "icon.ttf", 18);
            FontUtil.icon18 = new MinecraftFontRenderer(FontUtil.icon18_);
            FontUtil.icon20_ = FontUtil.getFont(locationMap, "icon.ttf", 20);
            FontUtil.icon20 = new MinecraftFontRenderer(FontUtil.icon20_);
            FontUtil.icon24_ = FontUtil.getFont(locationMap, "icon.ttf", 24);
            FontUtil.icon24 = new MinecraftFontRenderer(FontUtil.icon24_);
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
