package io.undervolt.api.animation.curves;

public class Curves {

    public static  class ElasticOutCurve implements ICurve{

        double period;

        public ElasticOutCurve() {
            this(.4);
        }

        public ElasticOutCurve(double period) {
            this.period = period;
        }

        @Override
        public double transform(double t) {
            final double s = period / 4.0;
            return Math.pow(2.0, -10 * t) * Math.sin((t - s) * (Math.PI * 2.0) / period) + 1.0;
        }

    }



    public static  class ElasticInCurve implements ICurve{

        double period;

        public ElasticInCurve() {
            this(.4);
        }

        public ElasticInCurve(double period) {
            this.period = period;
        }

        @Override
        public double transform(double t) {
            if (t <= 0.5) { // first half of the animation
                return quad(2 * t) / 2;
            } else { // second half of the animation
                return (2 - quad(2 * (1 - t))) / 2;
            }
        }

        public static double quad(double delta){
            return quad(delta, 2);
        }

        public static double quad(double delta, int power){
            return (float) Math.pow(delta, power);
        }

    }

}

