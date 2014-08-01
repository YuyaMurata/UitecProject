package testpack;

import java.util.Random;

import org.spaceroots.mantissa.random.MersenneTwister;

public class RandomRange {
    public static void main(String[] args){
        MersenneTwister r = new MersenneTwister();
        //Random r = new Random();

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for(int i = 0; i < 1000; ++i){
        	r.setSeed(i);
            double x = r.nextDouble();

            if(x > max) max = x;
            if(x < min) min = x;
        }
        System.out.printf("min=%.4f max=%.4f%n", min, max);
    }
}
