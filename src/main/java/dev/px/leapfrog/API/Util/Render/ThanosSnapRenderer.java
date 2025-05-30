package dev.px.leapfrog.API.Util.Render;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import dev.px.leapfrog.API.Event.Render.Render3DEvent;
import dev.px.leapfrog.API.Util.Math.DustBox;
import dev.px.leapfrog.ASM.Listeners.IMixinSkinManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Sk1er
 */
public class ThanosSnapRenderer {
    
    public HashMap<UUID, Integer> renderBlacklist = new HashMap<>();
    private List<UUID> realEntity = new ArrayList<>();
    private HashMap<UUID, Integer> timeCheck = new HashMap<>();
    public int DISTANCE = 16;
    public int MODE = 2;
    public boolean enabled = true;
    public double speed = 1.0D;
    public int RENDER_DISTANCE = 32;
    public boolean openGui;
    public boolean blending = true;
    private List<BodyPart> partList = new ArrayList<>();
    private List<DustBox> dustBoxes = new ArrayList<>();
    private HashMap<Integer, Pos> locationPos = new HashMap<>();
    private HashMap<UUID, Long> cancel = new HashMap<>();
    private double snapTime = .01;
    private boolean snapping = false;

    public ThanosSnapRenderer() {
    }

    // call on
    public void generate() {
        partList.clear();

        locationPos.put(0, new Pos(-3.5, -3.5, -3.5));
        locationPos.put(2, new Pos(3.5, 14, 4.5));
        locationPos.put(4, new Pos(7.5, 14, 4.5));
        locationPos.put(6, new Pos(5.5, 26, 4.5));


        //These next lines of code represent hours of pain

        //BASE LAYER

        //HEAD
        partList.add(new BodyPart(8, 8, 8, 8, 0, 0, 0, 8, 8, 0, 0, 0)); //FrontOfFace
        partList.add(new BodyPart(24, 8, 8, 8, 0, 0, 7, 8, 8, 8, 1, 0)); //BackOfHead
        partList.add(new BodyPart(8, 0, 8, 6, 0, 0, 1, 8, 0, 7, 2, 0)); //Top of head
        partList.add(new BodyPart(16, 1, 8, 6, 0, 7, 1, 8, 7, 7, 3, 0)); //Neck
        partList.add(new BodyPart(16, 9, 6, 6, 0, 1, 1, 0, 7, 7, 4, 0)); //Left
        partList.add(new BodyPart(2, 9, 6, 6, 7, 1, 6, 7, 7, 0, 5, 0)); //Right


        //TORSO
        partList.add(new BodyPart(20, 20, 8, 12, 7, 8, 2, -1, 20, 2, 0, 2)); //Front
        partList.add(new BodyPart(32, 20, 8, 12, 0, 8, 5, 8, 20, 5, 1, 2)); //Back
        partList.add(new BodyPart(29, 21, 2, 10, 0, 9, 3, 0, 19, 5, 4, 2)); //Left
        partList.add(new BodyPart(17, 21, 2, 10, 7, 9, 4, 7, 19, 2, 5, 2)); //Right
        partList.add(new BodyPart(20, 16, 8, 2, 0, 8, 3, 8, 8, 5, 2, 2)); //Top
        partList.add(new BodyPart(28, 16, 8, 2, 0, 19, 3, 8, 19, 5, 3, 2)); //Bottom


        //RIGHT LEG
        partList.add(new BodyPart(4, 20, 4, 12, 7, 20, 2, 3, 32, 2, 0, 5)); //Front
        partList.add(new BodyPart(12, 20, 4, 12, 7, 20, 5, 3, 32, 5, 1, 5)); //Back
        partList.add(new BodyPart(8, 20, 2, 12, 4, 20, 3, 4, 32, 5, 4, 5)); //Left
        partList.add(new BodyPart(0, 20, 2, 12, 7, 20, 3, 7, 32, 5, 5, 5)); //Right
        partList.add(new BodyPart(8, 16, 2, 2, 5, 31, 3, 7, 31, 5, 3, 5)); //Bottom


        //LEFT LEG
        partList.add(new BodyPart(20, 52, 4, 12, 3, 20, 2, -1, 32, 2, 0, 6)); //Front
        partList.add(new BodyPart(28, 52, 4, 12, 3, 20, 5, -1, 32, 5, 1, 6)); //Back
        partList.add(new BodyPart(30, 52, 2, 12, 0, 20, 3, 0, 32, 5, 4, 6)); //Left
        partList.add(new BodyPart(22, 52, 2, 12, 3, 20, 3, 3, 32, 5, 5, 6)); //Right
        partList.add(new BodyPart(24, 50, 2, 2, 1, 31, 3, 3, 31, 5, 3, 6)); //Bottom


        //RIGHT ARM
        partList.add(new BodyPart(36 + 8, 52 - 12 - 16 - 4, 4, 12, 7 + 4, 8, 2, 3 + 4, 32 - 12, 2, 0, 3)); //Front
        partList.add(new BodyPart(44 + 8, 52 - 12 - 16 - 4, 4, 12, 7 + 4, 8, 5, 3 + 4, 32 - 12, 5, 1, 3)); //Back
        partList.add(new BodyPart(46 + 8, 52 - 12 - 16 - 4, 2, 12, 4 + 4, 8, 3, 4 + 4, 32 - 12, 5, 4, 3)); //Left
        partList.add(new BodyPart(38 + 8, 52 - 12 - 16 - 4, 2, 12, 7 + 4, 8, 3, 7 + 4, 32 - 12, 5, 5, 3)); //Right
        partList.add(new BodyPart(40 + 8, 50 - 12 - 16 - 4, 2, 2, 5 + 4, 19, 3, 7 + 4, 31 - 12, 5, 3, 3)); //Bottom
        partList.add(new BodyPart(36 + 8, 50 - 12 - 16 - 4, 2, 2, 5 + 4, 8, 3, 7 + 4, 8, 5, 2, 3)); //Top


        //LEFT ARM
        partList.add(new BodyPart(36, 52, 4, 12, 7 - 8, 8, 2, 3 - 8, 32 - 12, 2, 0, 4)); //Front
        partList.add(new BodyPart(44, 52, 4, 12, 7 - 8, 8, 5, 3 - 8, 32 - 12, 5, 1, 4)); //Back
        partList.add(new BodyPart(46, 52, 2, 12, 4 - 8, 8, 3, 4 - 8, 32 - 12, 5, 4, 4)); //Left
        partList.add(new BodyPart(38, 52, 2, 12, 7 - 8, 8, 3, 7 - 8, 32 - 12, 5, 5, 4)); //Right
        partList.add(new BodyPart(40, 50, 2, 2, 5 - 8, 19, 3, 7 - 8, 31 - 12, 5, 3, 4)); //Bottom
        partList.add(new BodyPart(36, 50, 2, 2, 5 - 8, 8, 3, 7 - 8, 8, 5, 2, 4)); //Top


        //OUTER LAYER

        //HEAD OUTER
        partList.add(new BodyPart(8 + 32, 8, 8, 8, 0, 0, -1, 8, 8, -1, 0, 0)); //FrontOfFace
        partList.add(new BodyPart(24 + 32, 8, 8, 8, 0, 0, 9, 8, 8, 9, 1, 0)); //BackOfHead
        partList.add(new BodyPart(8 + 32, 0, 8, 6, 0, -1, 1, 8, -1, 7, 2, 0)); //Top of head
        partList.add(new BodyPart(16 + 32, 1, 8, 6, 0, 8, 1, 8, 7, 7, 3, 0)); //Neck
        partList.add(new BodyPart(16 + 32, 9, 6, 6, -1, 1, 1, -1, 7, 7, 4, 0)); //Left
        partList.add(new BodyPart(2 + 32, 9, 6, 6, 8, 1, 6, 8, 7, 0, 5, 0)); //Right

        //TORSO OUTER
        partList.add(new BodyPart(20, 20 + 16, 8, 12, 7, 8, 1, -1, 20, 1, 0, 2)); //Front
        partList.add(new BodyPart(32, 20 + 16, 8, 12, 0, 8, 5 + 1, 8, 20, 5 + 1, 1, 2)); //Back
        partList.add(new BodyPart(29, 21 + 16, 2, 10, -1, 9, 3, -1, 19, 5, 4, 2)); //Left
        partList.add(new BodyPart(17, 21 + 16, 2, 10, 8, 9, 4, 8, 19, 2, 5, 2)); //Right
        partList.add(new BodyPart(20, 16 + 16, 8, 2, 0, 7, 3, 8, 7, 5, 2, 2)); //Top
        partList.add(new BodyPart(28, 16 + 16, 8, 2, 0, 20, 3, 8, 20, 5, 3, 2)); //Bottom


        //RIGHT LEG OUTER
        partList.add(new BodyPart(4, 20 + 16, 4, 12, 7, 20, 1, 3, 32, 1, 0, 5)); //Front
        partList.add(new BodyPart(12, 20 + 16, 4, 12, 7, 20, 6, 3, 32, 6, 1, 5)); //Back
        partList.add(new BodyPart(8, 20 + 16, 2, 12, 3, 20, 3, 3, 32, 5, 4, 5)); //Left
        partList.add(new BodyPart(0, 20 + 16, 2, 12, 8, 20, 3, 8, 32, 5, 5, 5)); //Right
        partList.add(new BodyPart(8, 16 + 16, 2, 2, 5, 32, 3, 7, 32, 5, 3, 5)); //Bottom


        //LEFT LEG OUTER
        partList.add(new BodyPart(20 - 16, 52, 4, 12, 3, 20, 1, -1, 32, 1, 0, 6)); //Front
        partList.add(new BodyPart(28 - 16, 52, 4, 12, 3, 20, 6, -1, 32, 6, 1, 6)); //Back
        partList.add(new BodyPart(30 - 16, 52, 2, 12, -1, 20, 3, -1, 32, 5, 4, 6)); //Left
        partList.add(new BodyPart(22 - 16, 52, 2, 12, 4, 20, 3, 4, 32, 5, 5, 6)); //Right
        partList.add(new BodyPart(24 - 16, 50, 2, 2, 1, 32, 3, 3, 32, 5, 3, 6)); //Bottom


        //RIGHT ARM OUTER
        partList.add(new BodyPart(36 + 8, 52 - 12 - 16 - 4 + 16, 4, 12, 7 + 4, 8, 1, 3 + 4, 32 - 12, 1, 0, 3)); //Front
        partList.add(new BodyPart(44 + 8, 52 - 12 - 16 - 4 + 16, 4, 12, 7 + 4, 8, 6, 3 + 4, 32 - 12, 6, 1, 3)); //Back
        partList.add(new BodyPart(46 + 8, 52 - 12 - 16 - 4 + 16, 2, 12, 4 + 4 - 1, 8, 3, 4 + 4 - 1, 32 - 12, 5, 4, 3)); //Left
        partList.add(new BodyPart(38 + 8, 52 - 12 - 16 - 4 + 16, 2, 12, 7 + 4 + 1, 8, 3, 7 + 4 + 1, 32 - 12, 5, 5, 3)); //Right
        partList.add(new BodyPart(40 + 8, 50 - 12 - 16 - 4 + 16, 2, 2, 5 + 4, 20, 3, 7 + 4, 31 - 12 + 1, 5, 3, 3)); //Bottom
        partList.add(new BodyPart(36 + 8, 50 - 12 - 16 - 4 + 16, 2, 2, 5 + 4, 7, 3, 7 + 4, 7, 5, 2, 3)); //Top


        //LEFT ARM
        partList.add(new BodyPart(36 + 16, 52, 4, 12, 7 - 8, 8, 1, 3 - 8, 32 - 12, 1, 0, 4)); //Front
        partList.add(new BodyPart(44 + 16, 52, 4, 12, 7 - 8, 8, 6, 3 - 8, 32 - 12, 6, 1, 4)); //Back
        partList.add(new BodyPart(46 + 16, 52, 2, 12, 4 - 8 - 1, 8, 3, 4 - 8 - 1, 32 - 12, 5, 4, 4)); //Left
        partList.add(new BodyPart(38 + 16, 52, 2, 12, 7 - 8 + 1, 8, 3, 7 - 8 + 1, 32 - 12, 5, 5, 4)); //Right
        partList.add(new BodyPart(40 + 16, 50, 2, 2, 5 - 8, 20, 3, 7 - 8, 31 - 12 + 1, 5, 3, 4)); //Bottom
        partList.add(new BodyPart(36 + 16, 50, 2, 2, 5 - 8, 8 - 1, 3, 7 - 8, 8 - 1, 5, 2, 4)); //Top


    }

    public void remove(Entity entity) {
        if (!enabled)
            return;
        if (entity instanceof EntityPlayer) {
            if (entity.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) < DISTANCE * DISTANCE)
                dust(((EntityPlayer) entity));
        }
    }

    private float seed;

    public boolean dust(EntityPlayer player) {
        if (!realEntity.contains(player.getUniqueID())) {
            return false;
        }

        Long aLong = cancel.get(player.getUniqueID());
        if (aLong != null && System.currentTimeMillis() - aLong < 1000) {
            return false;
        }
        cancel.put(player.getUniqueID(), System.currentTimeMillis());
        ResourceLocation defaultSkinLegacy = DefaultPlayerSkin.getDefaultSkinLegacy();
        InputStream inputstream;
        IResource iresource;
        try {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = ((IMixinSkinManager) Minecraft.getMinecraft().getSkinManager()).getSessionService().getTextures(player.getGameProfile(), false);
            MinecraftProfileTexture minecraftProfileTexture = textures.get(MinecraftProfileTexture.Type.SKIN);
            File file2;
            BufferedImage skin = null;

            if (minecraftProfileTexture != null) {
                File file1 = new File(((IMixinSkinManager) Minecraft.getMinecraft().getSkinManager()).getSkinCacheDir(), minecraftProfileTexture.getHash().length() > 2 ? minecraftProfileTexture.getHash().substring(0, 2) : "xx");
                file2 = new File(file1, minecraftProfileTexture.getHash());
                if (file2.exists()) {
                    inputstream = new FileInputStream(file2);
                    skin = TextureUtil.readBufferedImage(inputstream);
                    if (skin == null || skin.getWidth() != 64 || skin.getHeight() != 64) {
                        skin = null;
                    }
                }
            }

            if (skin == null) { //Default to steve
                iresource = Minecraft.getMinecraft().getResourceManager().getResource(defaultSkinLegacy);
                inputstream = iresource.getInputStream();
                skin = TextureUtil.readBufferedImage(inputstream);
                if (skin == null) {
                    return false;
                }
            }
            for (int a = 0; a < 1; a++) {
                for (BodyPart bodyPart : partList) {
                    for (int j = 0; j < bodyPart.width; j++) {
                        for (int k = 0; k < bodyPart.height; k++) {
                            int x = bodyPart.texX + j;
                            int y1 = bodyPart.texY + k;
                            if (y1 >= skin.getHeight())
                                continue;
                            int rawColor = skin.getRGB(x, y1);
                            int alpha = (rawColor >> 24) & 0xFF;
                            if (alpha == 0) {
                                continue;
                            }

                            int red = (rawColor >> 16) & 0xFF;
                            int green = (rawColor >> 8) & 0xFF;
                            int blue = (rawColor) & 0xFF;
                            double scale = 0.0625F;

                            Pos relCoords = bodyPart.getCoords(j, k);
                            int tmpLayer = 0;

                            //Adjust because our model system is centered around top left of head and we want to center around center of chest
                            relCoords.add(.22 * 1 / scale, 1.95 * 1 / scale, .22 * 1 / scale);
                            relCoords.rotate(0, (float) Math.toRadians(-player.rotationYaw), 0);

                            double xCoord = relCoords.x;
                            double yCoord = relCoords.y;
                            double zCoord = relCoords.z;

                            double y = 0;
                            for (int i = 0; i < player.posY; i++) {
                                if (!player.worldObj.isAirBlock(new BlockPos(player.posX, i, player.posZ)) && player.worldObj.isAirBlock(new BlockPos(player.posX, i + 1, player.posZ))) {
                                    y = i + 1;
                                }
                            }
                            createPixel(
                                    player.posX + xCoord * scale,
                                    y + yCoord * scale,
                                    player.posZ + zCoord * scale,
                                    red,
                                    green,
                                    blue,
                                    255,
                                    xCoord,
                                    yCoord,
                                    zCoord,
                                    seed,
                                    tmpLayer);
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createPixel(double x, double y, double z, int red, int green, int blue, int alpha, double origPosX, double origPosY, double origPosZ, float seed, int layer) {
        dustBoxes.add(new DustBox(red / 255F, green / 255F, blue / 255F, alpha / 255F, x, y, z, origPosX, origPosY, origPosZ, seed, layer));
    }

    // call on
    public void switchWorld(WorldEvent.Unload event) {
        dustBoxes.clear();
        renderBlacklist.clear();
        seed = ThreadLocalRandom.current().nextFloat();

    }

    // call on
    public void onTick(int mode, double speed, boolean blending) {
        if (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            // for loop checks for if it's an actual player, or if it's a watchdog-like entity
            for (EntityPlayer entityPlayer : Minecraft.getMinecraft().theWorld.playerEntities) {
                if (!realEntity.contains(entityPlayer.getUniqueID())) {
                    if (!timeCheck.containsKey(entityPlayer.getUniqueID()))
                        timeCheck.put(entityPlayer.getUniqueID(), 0);
                    int old = timeCheck.get(entityPlayer.getUniqueID());
                    if (old > 100) {
                        if (!realEntity.contains(entityPlayer.getUniqueID()))
                            realEntity.add(entityPlayer.getUniqueID());
                    } else if (!entityPlayer.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
                        timeCheck.put(entityPlayer.getUniqueID(), old + 1);
                }
            }
        }

        if (snapping)
            snapTime += .05;
        if (snapTime > 2.0) {
            snapping = false;
        }
        dustBoxes.removeIf(box -> box.onUpdate(mode, speed, blending));
        Set<UUID> remove = new HashSet<>();
        for (UUID uuid : renderBlacklist.keySet()) {
            Integer integer = renderBlacklist.get(uuid);
            if (integer == 1)
                remove.add(uuid);
            else
                renderBlacklist.put(uuid, integer - 1);
        }
        for (UUID uuid : remove) {
            renderBlacklist.remove(uuid);
        }

    }

    // call on
    public void renderPlayer(RenderPlayerEvent.Pre event) {
        Integer integer = renderBlacklist.get(event.entityPlayer.getUniqueID());
        if (integer != null && integer > 0)
            event.setCanceled(true);
    }

    // render
    public void renderWorld(Render3DEvent event, double distance) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        if (blending) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
        } else {
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
        }
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        for (DustBox dustBox : dustBoxes) {
            dustBox.render((float) event.getPartialTicks(), distance);
        }
        if (!blending) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
        }
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public void snap() {
        snapping = true;
        snapTime = .01;
    }

    // call on
    public void renderTick() {
        if (snapping) {
            double a = .5 * snapTime;
            int mag = (int) Math.min(255, 255 * Math.abs(Math.pow(2, -a)));
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Gui.drawRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(255, 255, 255, mag).getRGB());
        }
    }












    class BodyPart {
        public final int side;
        public final int part;
        private int texX, texY, width, height; //Texture locations in MC skin
        private double startX;
        private double startY;
        private double startZ;
        private double endX;
        private double endY;
        private double endZ;

        public BodyPart(int texX, int texY, int width, int height, double startX, double startY, double startZ, double endX, double endY, double endZ, int side, int part) {
            this.texX = texX;
            this.texY = texY;
            this.width = width;
            this.height = height;
            this.startX = startX;
            this.startY = startY;
            this.startZ = startZ;
            this.endX = endX;
            this.endY = endY;
            this.endZ = endZ;
            this.side = side;
            this.part = part;
        }


        //Translate x and y texture coords into real 3d coords
        public Pos getCoords(double texOne, double texTwo) {
            double newX;
            double newY;
            double newZ;
            if (startX == endX) {
                newX = startX;
                newY = startY + (endY - startY) * (texTwo / height);
                newZ = startZ + (endZ - startZ) * (texOne / width);
            } else if (startY == endY) {
                newX = startX + (endX - startX) * (texOne / width);
                newY = startY;
                newZ = startZ + (endZ - startZ) * (texTwo / height);
            } else {
                newX = startX + (endX - startX) * (texOne / width);
                newY = startY + (endY - startY) * (texTwo / height);
                newZ = startZ;
            }
            Pos pos = new Pos(newX, newY, newZ);
            //Negative because coords are are mult by -1 in Minecraft Model Rendering
            pos.multiply(-1, -1, -1);
            return pos;
        }
    }

    class Pos {
        double x, y, z;

        public Pos(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void rotate(float rotateAngleX, float rotateAngleY, float rotateAngleZ) {
            if (rotateAngleX != 0) {
                double tx = x;
                double ty = y;
                double tz = z;
                x = tx * MathHelper.cos(rotateAngleX) - ty * MathHelper.sin(rotateAngleX);
                y = tx * MathHelper.sin(rotateAngleX) + ty * MathHelper.cos(rotateAngleX);
                z = tz;
            }
            if (rotateAngleY != 0) {
                double tx = x;
                double ty = y;
                double tz = z;
                x = tx * MathHelper.cos(rotateAngleY) + tz * MathHelper.sin(rotateAngleY);
                y = ty;
                z = -tx * MathHelper.sin(rotateAngleY) + tz * MathHelper.cos(rotateAngleY);
            }
            if (rotateAngleZ != 0) {
                double tx = x;
                double ty = y;
                double tz = z;
                x = tx;
                y = ty * MathHelper.cos(rotateAngleZ) - tz * MathHelper.sin(rotateAngleZ);
                z = ty * MathHelper.sin(rotateAngleZ) + tz * MathHelper.cos(rotateAngleZ);
            }
        }

        @Override
        public String toString() {
            return "Pos{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }

        public void add(double xCoord, double yCoord, double zCoord) {
            x += xCoord;
            y += yCoord;
            z += zCoord;
        }

        public void multiply(double xMult, double yMult, double zMult) {
            this.x *= xMult;
            this.y *= yMult;
            this.z *= zMult;
        }
    }

}
