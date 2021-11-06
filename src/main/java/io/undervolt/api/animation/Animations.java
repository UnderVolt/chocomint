package io.undervolt.api.animation;

public class Animations {

    public static AnimationVector[] ease(){
        return new AnimationVector[]{
                new AnimationVector(.25, 0.1),
                new AnimationVector(.25, .1)
        };
    }

    public static AnimationVector[] linear(){
        return new AnimationVector[]{
                new AnimationVector(0, 0),
                new AnimationVector(1, 1)
        };
    }

    public static AnimationVector[] easeIn(){
        return new AnimationVector[]{
                new AnimationVector(0.42, 0),
                new AnimationVector(1, 1)
        };
    }

    public static AnimationVector[] easeOut(){
        return new AnimationVector[]{
                new AnimationVector(0, 0),
                new AnimationVector(.58, 1)
        };
    }

    public static AnimationVector[] easeInOut(){
        return new AnimationVector[]{
                new AnimationVector(0.42, 0),
                new AnimationVector(.58, 1)
        };
    }

}
