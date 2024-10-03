package dev.px.leapfrog.API.Module.Setting;

public class Slot {

    private int slot;

    public Slot(int slot) {
        this.slot = slot;
    }

    public boolean isHotbar() {
        return slot <= 9;
    }

    public boolean isAssigned() {
        return slot >= 1 && slot <= 9;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getSlotName() {
        switch (slot) {
            case -1:
                return "None";
            case 0:
                return "None";
            case 1:
                return "First";
            case 2:
                return "Second";
            case 3:
                return "Third";
            case 4:
                return "Fourth";
            case 5:
                return "Fifth";
            case 6:
                return "Sixth";
            case 7:
                return "Seventh";
            case 8:
                return "Eighth";
            case 9:
                return "Ninth";
            default:
                return String.valueOf(slot);
        }
    }

}
