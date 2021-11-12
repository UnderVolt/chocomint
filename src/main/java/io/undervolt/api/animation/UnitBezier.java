package io.undervolt.api.animation;

public class UnitBezier {

    AnimationVector start, end, coefficient, b, a;
    double epsilon;

    public UnitBezier(AnimationVector start, AnimationVector end) {
        this.start = start;
        this.end = end;

        this.coefficient = new AnimationVector(3.0 * start.getX(), 3.0 * start.getY());
        this.b = new AnimationVector(3.0 * (end.getX() - start.getX()) - coefficient.getX(), 3.0 * (end.getY() - start.getY()) - coefficient.getY());
        this.a = new AnimationVector(1.0 - this.coefficient.getX() - this.b.getX(), 1.0 - this.coefficient.getY() - this.b.getY());

        this.epsilon = 1e-6;
    }


    public double solve(double fraction) {
        return this.sampleCurveY(this.solveCurveX(fraction));
    }

    private double sampleCurveX(double t) {
        return ((this.a.getX() * t + this.b.getX()) * t + this.coefficient.getX()) * t;
    }

    private double sampleCurveY(double t) {
        return ((this.a.getY() * t + this.b.getY()) * t + this.coefficient.getY()) * t;
    }

    private double sampleCurveDerX(double t) {
        return (3.0 * this.a.getX() * t + 2.0 * this.b.getX()) * t * this.coefficient.getX();
    }

    private double solveCurveX(double t) {
        double t0, t1, t2, x2, d2, i;

        for (t2 = t, i = 0; i < 8; i++) {
            x2 = this.sampleCurveX(t2) - t;
            if (Math.abs(x2) < epsilon) {
                return t2;
            }

            d2 = this.sampleCurveDerX(t2);

            if (Math.abs(d2) < epsilon)
                break;

            t2 = t2 - x2 / d2;
        }

        t0 = 0;
        t1 = 1;
        t2 = t;

        if (t2 < t0) return t0;
        if (t2 > t1) return t1;

        while (t0 < t1) {
            x2 = this.sampleCurveX(t2);
            if (Math.abs(x2 - t) < epsilon) {
                return t2;
            }

            if (t > x2) t0 = t2;
            else t1 = t2;

            t2 = (t1 - t0) * .5 + t0;
        }

        return t2;
    }
}
