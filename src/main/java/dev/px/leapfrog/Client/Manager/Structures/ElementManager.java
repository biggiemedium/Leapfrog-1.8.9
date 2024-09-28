package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.HUD.Impl.*;
import dev.px.leapfrog.Client.GUI.HUD.UI.GuiHUDEditor;
import dev.px.leapfrog.LeapFrog;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;

public class ElementManager {

    private ArrayList<Element> elements = new ArrayList<>();

    public ElementManager() {
        Add(new ArrayListElement());
       Add(new CoordinatesElement());
       Add(new CPSElement());
       Add(new FPSElement());
       Add(new PingElement());
       Add(new RadarElement());
       Add(new SpeedElement());
       Add(new TestElement());
       Add(new TimeElement());
       Add(new WatermarkElement());

        Collections.sort(elements);
    }

    public void Add(Element element) {
        this.elements.add(element);
    }

    public Element getElement(String name) {
        for(Element e : this.elements) {
            if(e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }
}
