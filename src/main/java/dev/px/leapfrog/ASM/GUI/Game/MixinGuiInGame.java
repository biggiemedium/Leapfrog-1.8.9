package dev.px.leapfrog.ASM.GUI.Game;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import dev.px.leapfrog.API.Event.Render.RenderScoreboardEvent;
import dev.px.leapfrog.API.Util.Render.Animation.SimpleAnimation;
import dev.px.leapfrog.API.Util.Render.Shaders.RoundedShader;
import dev.px.leapfrog.Client.Module.Render.HotbarModification;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Predicate;

import java.awt.*;
import java.util.Collection;
import java.util.List;

@Mixin(GuiIngame.class)
public abstract class MixinGuiInGame extends Gui {

    @Shadow protected long healthUpdateCounter;

    @Shadow
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");

    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    public SimpleAnimation simpleAnimation = new SimpleAnimation(0.0F);

    private Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    public void onScoreboardRender(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_, CallbackInfo ci) {
        RenderScoreboardEvent event = new RenderScoreboardEvent(p_180475_1_, p_180475_2_);
        LeapFrog.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            ci.cancel();
        } else if(LeapFrog.settingsManager.SCOREBOARD.getValue()) {
            Scoreboard scoreboard = event.getObjective().getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(event.getObjective());
            List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>()
            {
                public boolean apply(Score p_apply_1_)
                {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }
            }));

            ci.cancel();

            if (list.size() > 15)
            {
                collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
            }
            else
            {
                collection = list;
            }

            int i = mc.fontRendererObj.getStringWidth(event.getObjective().getDisplayName());

            for (Score score : collection)
            {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = Math.max(i, mc.fontRendererObj.getStringWidth(s));
            }

            int scoreHeight = collection.size() * mc.fontRendererObj.FONT_HEIGHT;
            int scoreY = event.getScaledResolution().getScaledHeight() / 2 + scoreHeight / 3;
            int k1 = 3;
            int l1 =  event.getScaledResolution().getScaledWidth() - i - k1;
            int j = 0;
            int offsetY = 0;

            for(Score offsetAdjustment : collection) {
                offsetY++;
            }
            offsetY++;

            RoundedShader.drawRound(l1 - 2, scoreY - offsetY * mc.fontRendererObj.FONT_HEIGHT, event.getScaledResolution().getScaledWidth() - k1, scoreHeight + mc.fontRendererObj.FONT_HEIGHT, 4, new Color(26, 26, 26, 180));

            for (Score score1 : collection)
            {
                ++j;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());

                int k = scoreY - j * mc.fontRendererObj.FONT_HEIGHT;
                int l = event.getScaledResolution().getScaledWidth() - k1 + 2;
                // Background
                //Gui.drawRect(l1 - 2, k, l, k + mc.fontRendererObj.FONT_HEIGHT, 1342177280);
                //RoundedShader.drawRound(l1 - 2, k, l, k + mc.fontRendererObj.FONT_HEIGHT, 4, ColorUtil.applyOpacity(new Color(1342177280), 190));
                mc.fontRendererObj.drawString(s1, l1, k, 553648127);

                if (j == collection.size()) {
                    String s3 = event.getObjective().getDisplayName();
                    //RoundedShader.drawRound(l1 - 2, k - mc.fontRendererObj.FONT_HEIGHT - 1, l, k - 1, 4, ColorUtil.applyOpacity(new Color(1610612736), 100));
                    //RoundedShader.drawRound(l1 - 2, k - 1, l, k, 4, ColorUtil.applyOpacity(new Color(1342177280), 100));
                    mc.fontRendererObj.drawString(s3, l1 + i / 2 - mc.fontRendererObj.getStringWidth(s3) / 2, k - mc.fontRendererObj.FONT_HEIGHT, 553648127);
                }
            }

        }
    }

    /**
     * @param sr
     * @param partialTicks
     * Make these stupid lines go away
     */
    @Overwrite
    public void renderTooltip(ScaledResolution sr, float partialTicks) {
        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {

            boolean animation = LeapFrog.moduleManager.getModuleByClass(HotbarModification.class).animation.getValue();
            int animationSpeed = LeapFrog.moduleManager.getModuleByClass(HotbarModification.class).speed.getValue();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            this.zLevel = -90.0F;

            simpleAnimation.setAnimation(entityplayer.inventory.currentItem * 20, animationSpeed);
            int itemX = i - 91 + (animation ? (int) simpleAnimation.getValue() : (entityplayer.inventory.currentItem * 20));

            if(LeapFrog.moduleManager.isModuleToggled(HotbarModification.class)) {
                switch(LeapFrog.moduleManager.getModuleByClass(HotbarModification.class).mode.getValue()) {
                    case Default:
                        this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                        this.drawTexturedModalRect(i - 91 - 1 + (animation ? simpleAnimation.getValue() : (entityplayer.inventory.currentItem * 20)), sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
                        break;
                    case Clear:
                        drawRect(itemX, sr.getScaledHeight() - 22, itemX + 22, sr.getScaledHeight(), new Color(230, 230, 230, 180).getRGB());
                        break;
                    case Round:
                        RoundedShader.drawRound(i - 91, sr.getScaledHeight() - 22, 180, 22, 4, new Color(26, 26, 26, 180));
                        RoundedShader.drawRound(itemX, sr.getScaledHeight() - 22, 22, sr.getScaledHeight(), 4, new Color(230, 230, 230, 150));
                }
            }else {
                this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }

            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

}
