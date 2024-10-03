package dev.px.leapfrog.ASM.Entity;

import com.mojang.authlib.GameProfile;
import dev.px.leapfrog.API.Event.Event;
import dev.px.leapfrog.API.Event.Player.PlayerMotionEvent;
import dev.px.leapfrog.API.Event.Player.PlayerSendChatEvent;
import dev.px.leapfrog.API.Event.Player.PlayerSlowDownEvent;
import dev.px.leapfrog.API.Event.Player.PlayerUpdateEvent;
import dev.px.leapfrog.API.Util.Render.ChatUtil;
import dev.px.leapfrog.LeapFrog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow public abstract boolean isRidingHorse();

    @Shadow public MovementInput movementInput;

    @Shadow protected int sprintToggleTimer;

    @Shadow private float horseJumpPower;

    @Shadow protected abstract boolean isCurrentViewEntity();

    @Shadow private int horseJumpPowerCounter;

    @Shadow protected abstract void sendHorseJump();

    @Shadow protected Minecraft mc;

    @Shadow public float timeInPortal;

    @Shadow public int sprintingTicksLeft;

    @Shadow public float prevTimeInPortal;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isBlockLoaded(Lnet/minecraft/util/BlockPos;)Z", shift = At.Shift.AFTER))
    public void onPreUpdate(CallbackInfo ci) {
        PlayerUpdateEvent event = new PlayerUpdateEvent(Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isCurrentViewEntity()Z", shift = At.Shift.AFTER), cancellable = true)
    public void onWalkingUpdatePre(CallbackInfo ci) {
        PlayerMotionEvent event = new PlayerMotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(event);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onWalkingUpdatePost(CallbackInfo ci) {
        PlayerMotionEvent event = new PlayerMotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround, Event.Stage.Post);
        LeapFrog.EVENT_BUS.post(event);
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void onChat(String p_sendChatMessage_1_, CallbackInfo ci) {
        PlayerSendChatEvent event = new PlayerSendChatEvent(p_sendChatMessage_1_);
        LeapFrog.EVENT_BUS.post(event);
        if(event.isCancelled()) {
            ci.cancel();
        }

        // idk why this doesn't work in command manager but just use this here
        String message = event.getMessage();
        if (!message.startsWith(".")) return;
        message = message.substring(1);
        final String[] args = message.split(" ");


        System.out.println("Message: " + message);
        System.out.println("Args: " + Arrays.toString(args));

        AtomicBoolean commandFound = new AtomicBoolean(false);

        try {
            LeapFrog.commandManager.getCommands().stream()
                    .filter(command -> {
                        // Print command arguments for debugging
                        System.out.println("Command arguments: " + Arrays.toString(command.getArguments()));
                        return Arrays.stream(command.getArguments())
                                .anyMatch(expression -> expression.equalsIgnoreCase(args[0]));
                    })
                    .forEach(command -> {
                        commandFound.set(true);
                        command.execute(args);
                    });
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        if (!commandFound.get()) {
            ChatUtil.sendClientSideMessage("command.unknown");
        }

        ci.cancel();
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onPlayerUpdate(CallbackInfo ci) {
        /*
        PlayerUpdateEvent event = new PlayerUpdateEvent(Event.Stage.Pre);
        LeapFrog.EVENT_BUS.post(event);
         */
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"), cancellable = true)
    public void onLivingUpdate2(CallbackInfo ci) {
        ci.cancel();
        this.onLivingUpdateModified();
    }

    public void onLivingUpdateModified()
    {
        if (this.sprintingTicksLeft > 0)
        {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0)
            {
                this.setSprinting(false);
            }
        }

        if (this.sprintToggleTimer > 0)
        {
            --this.sprintToggleTimer;
        }

        this.prevTimeInPortal = this.timeInPortal;

        if (this.inPortal)
        {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }

            if (this.timeInPortal == 0.0F)
            {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F)
            {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F)
            {
                this.timeInPortal = 1.0F;
            }
        }
        else
        {
            if (this.timeInPortal > 0.0F)
            {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F)
            {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = this.movementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();

        PlayerSlowDownEvent event = new PlayerSlowDownEvent();
        LeapFrog.EVENT_BUS.post(event);

        if(!event.isCancelled()) {
            if (this.isUsingItem() && !this.isRiding()) {
                this.movementInput.moveStrafe *= 0.2F;
                this.movementInput.moveForward *= 0.2F;
                this.sprintToggleTimer = 0;
            }
        }

        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
        boolean flag3 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
        {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
            {
                this.sprintToggleTimer = 7;
            }
            else
            {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
        {
            this.setSprinting(true);
        }

        if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
        {
            this.setSprinting(false);
        }

        if (this.capabilities.allowFlying)
        {
            if (this.mc.playerController.isSpectatorMode())
            {
                if (!this.capabilities.isFlying)
                {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!flag && this.movementInput.jump)
            {
                if (this.flyToggleTimer == 0)
                {
                    this.flyToggleTimer = 7;
                }
                else
                {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.capabilities.isFlying && this.isCurrentViewEntity())
        {
            if (this.movementInput.sneak)
            {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
            }

            if (this.movementInput.jump)
            {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
            }
        }

        if (this.isRidingHorse())
        {
            if (this.horseJumpPowerCounter < 0)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0)
                {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.movementInput.jump)
            {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!flag && this.movementInput.jump)
            {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            }
            else if (flag)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10)
                {
                    this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
                }
                else
                {
                    this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        }
        else
        {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode())
        {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
}
