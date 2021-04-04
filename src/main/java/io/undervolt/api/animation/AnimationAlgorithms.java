package io.undervolt.api.animation;

public class AnimationAlgorithms {

    public static float linear(float delta){
        return delta;
    }

    public static float easeInOut(float delta){
        if (delta <= 0.5) { // first half of the animation
            return quad(2 * delta) / 2;
        } else { // second half of the animation
            return (2 - quad(2 * (1 - delta))) / 2;
        }
    }

    public static float quad(float delta){
        return quad(delta, 2);
    }

    public static float quad(float delta, int power){
        return (float) Math.pow(delta, power);
    }

    public static float circ(float delta){
        return (float) (1 - Math.sin(Math.cos(delta)));
    }

    public static float bounce(float delta){
        for (int i = 0, j = 1; true; i+=j, j /=2) {
            if (delta >= (7 - 4 * i) / 11) {
                return (float) (-Math.pow((11 - 6 * i - 11 * delta) / 4, 2) + Math.pow(j, 2));
            }
        }
    }

    public static float back(float x, float delta){
        return (float) (Math.pow(delta, 2) * ((x + 1) * delta - x));
    }

    public static float elastic(float x, float delta){
        return (float) (Math.pow(2, 10 * (delta - 1)) * Math.cos(20 * Math.PI * x / 3 * delta));
    }

}
