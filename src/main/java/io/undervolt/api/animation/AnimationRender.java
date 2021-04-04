package io.undervolt.api.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class AnimationRender extends Gui {

    private float duration;
    private AnimationTimings timing;
    private float start;
    public float deltaTime = 0;
    private boolean reverse;


    public AnimationRender(float duration) {
        this.duration = duration;
        this.timing = AnimationTimings.QUAD;
        this.reverse = false;
    }

    public AnimationRender(float duration, boolean reverse) {
        this.duration = duration;
        this.timing = AnimationTimings.QUAD;
        this.reverse = reverse;
    }

    public AnimationRender(float duration, AnimationTimings timing) {
        this.duration = duration;
        this.timing = timing;
        this.reverse = false;
    }

    public AnimationRender(float duration, AnimationTimings timing, boolean reverse) {
        this.duration = duration;
        this.timing = timing;
        this.reverse = reverse;
    }

    public void init(){
        start = Minecraft.getSystemTime();
    }

    public void render(){
        float timeFraction = (Minecraft.getSystemTime() - start) / duration;

        if (timeFraction > 1) timeFraction = 1;

        this.deltaTime = resolveDeltaTime(timeFraction);

        if (timeFraction > 1) return;
    }

    public float reverse(float reverseMode){
       return reverseMode - ( this.deltaTime * reverseMode);
    }


    public void  toggle(){
        setReverse(!isReverse());
        this.init();
        render();
    }


    public float resolveDeltaTime(float timeFraction){
        switch (timing){
            case BOUNCE:
                return AnimationAlgorithms.bounce(timeFraction);
            case QUAD:
               return AnimationAlgorithms.quad(timeFraction);

            case ARC:
                return AnimationAlgorithms.circ(timeFraction);

            case LINEAR:
            default:
                return AnimationAlgorithms.easeInOut(timeFraction);
        }
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public AnimationTimings getTiming() {
        return timing;
    }

    public void setTiming(AnimationTimings timing) {
        this.timing = timing;
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
}
