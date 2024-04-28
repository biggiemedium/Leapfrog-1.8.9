package dev.px.leapfrog.Client.Module.Misc;

import dev.px.leapfrog.API.Module.Type;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.WorldSettings;

@Module.ModuleInterface(name = "Fake Player", type = Type.Misc, description = "Summons client sided player")
public class FakePlayer extends Module {

    private EntityOtherPlayerMP fakePlayer;

    Setting<Boolean> copyInventory = create(new Setting<>("Copy Inventory", true));

    @Override
    public void onEnable() {
        this.fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        fakePlayer.rotationPitch = mc.thePlayer.rotationPitch;
        fakePlayer.rotationYaw = mc.thePlayer.rotationYaw;
        fakePlayer.setGameType(WorldSettings.GameType.SURVIVAL);
        mc.theWorld.addEntityToWorld(-69420, fakePlayer);

        if(copyInventory.getValue()) {
            fakePlayer.inventory.copyInventory(mc.thePlayer.inventory);
        }
    }

    @Override
    public void onDisable() {
        if(mc.theWorld.loadedEntityList.contains(fakePlayer) && fakePlayer != null) {
            mc.theWorld.removeEntity(fakePlayer);
        }
    }
}
