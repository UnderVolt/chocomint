package io.undervolt.api.ui.widgets.controllers;

import io.undervolt.api.animation.*;
import io.undervolt.api.animation.curves.ICurve;
import io.undervolt.utils.MathUtil;
import io.undervolt.utils.Multithreading;
import net.minecraft.client.Minecraft;

import java.util.function.Function;

public class AnimationController implements Runnable {

    private Double duration;
    private Long start;
    public double value;
    private TransformFunction reverse;
    private ICurve animationTransform;
    private PostFrame frame;
    private AnimationVector from, to;
    private AnimationStatus status = AnimationStatus.WAITING;
    private double lowerBound = 0, upperBound = 1;

    public AnimationController(Double duration, AnimationVector from, AnimationVector to) {
        this.duration = duration;
        this.from = from;
        this.to = to;
    }

    public AnimationController() {
        this(14.5, Animations.easeInOut()[0], Animations.easeInOut()[1]);
    }

    public AnimationController(Double duration) {
        this(duration, Animations.easeInOut()[0], Animations.easeInOut()[1]);
    }

    public AnimationController(AnimationVector from, AnimationVector to) {
        this(14.5, from, to);
    }

    public AnimationController(Double... points) {
        this(14.5, new AnimationVector(points[0], points[1]), new AnimationVector(points[2], points[3]));
    }

    public AnimationController(AnimationVector... vectors) {
        this(14.5, vectors[0], vectors[1]);

    }

    public double forward() {
        return this.animate();
    }

    protected double create(Function<UnitBezier, Double> create) {
        UnitBezier bezier = new UnitBezier(from, to);
        return create.apply(bezier);
    }

    protected double animate() {
        if (start == null) start = Minecraft.getSystemTime();

        double timeFraction = ((Minecraft.getSystemTime() - start) / (duration * 1000.0));
        double value = this.create(unitBezier -> unitBezier.solve(timeFraction));

        this.value = this.animationTransform != null ? this.animationTransform.transform(value) : value;

        if (status == AnimationStatus.REVERSE) {
            System.out.println(value == lowerBound);
            if (value >= upperBound) {
                this.value = this.reverse.onRun(value);
                return this.value;
            } else if (value == lowerBound) {
                return this.value;
            }
        }

        this.frame.onRun(value);

        return this.value;
    }

    public AnimationController setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    public AnimationController setUpperBound(double upperBound) {
        this.upperBound = upperBound;
        return this;
    }


    public AnimationController setAnimations(AnimationVector from, AnimationVector to) {
        this.from = from;
        this.to = to;

        return this;
    }

    public AnimationController setAnimations(Double... points) {
        if (points.length != 4) {
            throw new IllegalArgumentException("Points length needs to be 4");
        }

        this.from = new AnimationVector(points[0], points[1]);
        this.to = new AnimationVector(points[2], points[3]);

        return this;
    }

    public AnimationController setAnimations(AnimationVector... vectors) {
        if (vectors.length != 2) {
            throw new IllegalArgumentException("Vectors length needs to be 4");
        }

        this.from = vectors[0];
        this.to = vectors[1];

        return this;
    }

    public AnimationController setFrom(AnimationVector from) {
        this.from = from;
        return this;
    }

    public AnimationController setFrom(Double... points) {
        if (points.length != 2) {
            throw new IllegalArgumentException("Points length needs to be 2");
        }

        this.from = new AnimationVector(points[0], points[1]);
        return this;
    }

    public AnimationController setTo(AnimationVector to) {
        this.to = to;
        return this;
    }

    public AnimationController setTo(Double... points) {
        if (points.length != 2) {
            throw new IllegalArgumentException("Points length needs to be 2");
        }

        this.to = new AnimationVector(points[0], points[1]);
        return this;
    }

    public AnimationController Reverse(double from) {
        status = AnimationStatus.REVERSE;
        this.upperBound = from;
        this.reverse = new TransformFunction() {
            @Override
            public double onRun(double fraction) {
                return from - (fraction * from);
            }
        };

        return this;
    }

    public void setFrame(PostFrame frame) {
        this.frame = frame;
    }

    public AnimationController setCurve(ICurve animationTransform) {
        this.animationTransform = animationTransform;
        return this;
    }

    @Override
    public void run() {
        this.animate();
    }

    public interface TransformFunction {
        double onRun(double fraction);
    }

    public interface PostFrame {
        void onRun(double frame);
    }
}
