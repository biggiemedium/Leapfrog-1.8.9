package dev.px.leapfrog.Client.GUI.HUD.Impl;

import dev.px.leapfrog.API.Event.Render.Render2DEvent;
import dev.px.leapfrog.Client.GUI.HUD.Element;
import dev.px.leapfrog.Client.Module.Module;
import dev.px.leapfrog.Client.Module.Setting;
import dev.px.leapfrog.LeapFrog;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Element.ElementInterface(name = "ArrayList", description = "Displays Toggled elements in an array")
public class ArrayListElement extends Element {

    public ArrayListElement() {
        super(50, 0);
    }



    @Override
    public void onRender(Render2DEvent event) {

        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        float positionX = getX();
        float positionY = getY();

        boolean isRightSide = positionX > (screenWidth / 2);

        List<Module> enabledModules = LeapFrog.moduleManager.getModules().stream()
                .filter(m -> m.isToggled() && !m.isDrawn())
                .collect(Collectors.toList());

        if (positionY < screenHeight / 2) {
            enabledModules.sort((m1, m2) ->
                    Double.compare(font.getStringWidth(m2.getName() + (m2.arrayDetails().equalsIgnoreCase("") ? "" : " (" + m2.arrayDetails() + ")")), font.getStringWidth(m1.getName() + (m1.arrayDetails().equalsIgnoreCase("") ? "" : " (" + m1.arrayDetails() + ")")))
            );
        } else {
            enabledModules.sort((m1, m2) ->
                    Double.compare(font.getStringWidth(m1.getName() + (m1.arrayDetails().equalsIgnoreCase("") ? "" : " (" + m1.arrayDetails() + ")")), font.getStringWidth(m2.getName() + (m2.arrayDetails().equalsIgnoreCase("") ? "" : " (" + m2.arrayDetails() + ")")))
            );
        }

        int elementCount = enabledModules.size();
        double elementHeight = font.getHeight();

        double startY = (positionY < screenHeight / 2) ? positionY : (positionY - (elementCount * elementHeight));


        for (int i = 0; i < elementCount; i++) {
            Module module = enabledModules.get(i);
            String element = module.getName() + (module.arrayDetails().equalsIgnoreCase("") ? "" : " (" + module.arrayDetails() + ")");
            double currentY = startY + (i * elementHeight);
            double currentX = isRightSide ? (positionX - font.getStringWidth(element) - 5) : positionX;
            font.drawStringWithClientColor(element, currentX, currentY, true);
        }
    }

}

