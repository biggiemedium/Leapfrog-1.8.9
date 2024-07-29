package dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations;

import dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Impl.SmoothStepAnimation;

public class ContinualAnimation {

    private float output, endpoint;

    private dev.px.leapfrog.API.Util.Render.Animation.TenacityAnimations.Animation animation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);

    public void animate(float destination, int ms) {
        output = endpoint - (float) animation.getValue();
        endpoint = destination;
        if (output != (endpoint - destination)) {
            animation = new SmoothStepAnimation(ms, endpoint - output, Direction.BACKWARDS);
        }
    }


    public boolean isDone() {
        return output == endpoint || animation.isDone();
    }

    public float getOutput() {
        output = endpoint - (float) animation.getValue();
        return output;
    }

}
