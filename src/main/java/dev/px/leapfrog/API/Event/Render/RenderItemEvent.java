package dev.px.leapfrog.API.Event.Render;

import dev.px.leapfrog.API.Event.Event;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class RenderItemEvent extends Event {
// enumaction, useItem, animationProgression, partialTicks, swingProgress, itemToRender

    private EnumAction enumAction;
    private boolean useItem;
    private float animationProgress;
    private float partialTicks;
    private float swingProgress;
    private ItemStack itemToRender;

    public RenderItemEvent(EnumAction enumAction, boolean useItem, float animationProgress, float partialTicks, float swingProgress, ItemStack itemToRender) {
        this.enumAction = enumAction;
        this.useItem = useItem;
        this.animationProgress = animationProgress;
        this.partialTicks = partialTicks;
        this.swingProgress = swingProgress;
        this.itemToRender = itemToRender;
    }

    public EnumAction getEnumAction() {
        return enumAction;
    }

    public void setEnumAction(EnumAction enumAction) {
        this.enumAction = enumAction;
    }

    public boolean isUseItem() {
        return useItem;
    }

    public void setUseItem(boolean useItem) {
        this.useItem = useItem;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public void setSwingProgress(float swingProgress) {
        this.swingProgress = swingProgress;
    }

    public ItemStack getItemToRender() {
        return itemToRender;
    }

    public void setItemToRender(ItemStack itemToRender) {
        this.itemToRender = itemToRender;
    }
}
