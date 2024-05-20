package dev.px.leapfrog.Client.Manager.Structures;

import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.GUI.HUD.Impl.TestElement;

import java.util.ArrayList;

public class ElementManager {

    private ArrayList<Element> elements = new ArrayList<>();

    public ElementManager() {
       Add(new TestElement());
    }

    public void Add(Element element) {
        this.elements.add(element);
    }

    public ArrayList<Element> getElements() {
        return elements;
    }
}
