
package dev.ludovic.presentation.dotproduct;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import static dev.ludovic.presentation.BenchmarkSupport.loopBound;

public class DotProduct {

  public static double scalar(double[] x, double[] y, int n) {
    double sum = 0.0;
    for (int i = 0; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }

  public static void main(String[] args) {
    double x[] = new double[] { 1.0, 2.0, 3.0, 4.0 };
    double y[] = new double[] { 5.0, 6.0, 7.0, 8.0 };
    double res = 0.0;
    for (int i = 0; i < 1000 * 1000; i++) {
      res += scalar(x, y, 4);
    }
    long start = System.nanoTime();
    for (int i = 0; i < 1000 * 1000 * 1000; i++) {
      res += scalar(x, y, 4);
    }
    long end = System.nanoTime();
    System.out.println("time = " + (end - start) / 1000 / 1000 + "ms, res = " + res);
  }

  public static double scalarUnroll(double[] x, double[] y, int n) {
    double sum0 = 0.0;
    double sum1 = 0.0;
    double sum2 = 0.0;
    double sum3 = 0.0;
    int i = 0;
    for (; i < loopBound(n, 4); i += 4) {
      sum0 += x[i+0] * y[i+0];
      sum1 += x[i+1] * y[i+1];
      sum2 += x[i+2] * y[i+2];
      sum3 += x[i+3] * y[i+3];
    }
    double sum = sum0 + sum1 + sum2 + sum3;
    for (; i < n; i++) {
        sum += x[i] * y[i];
    }
    return sum;
  }

  public static double scalarUnrollALot(double[] x, double[] y, int n) {
    double sum0 = 0.0;
    double sum1 = 0.0;
    double sum2 = 0.0;
    double sum3 = 0.0;
    double sum4 = 0.0;
    double sum5 = 0.0;
    double sum6 = 0.0;
    double sum7 = 0.0;
    double sum8 = 0.0;
    double sum9 = 0.0;
    double sum10 = 0.0;
    double sum11 = 0.0;
    double sum12 = 0.0;
    double sum13 = 0.0;
    double sum14 = 0.0;
    double sum15 = 0.0;
    double sum16 = 0.0;
    double sum17 = 0.0;
    double sum18 = 0.0;
    double sum19 = 0.0;
    double sum20 = 0.0;
    double sum21 = 0.0;
    double sum22 = 0.0;
    double sum23 = 0.0;
    double sum24 = 0.0;
    double sum25 = 0.0;
    double sum26 = 0.0;
    double sum27 = 0.0;
    double sum28 = 0.0;
    double sum29 = 0.0;
    double sum30 = 0.0;
    double sum31 = 0.0;
    int i = 0;
    for (; i < loopBound(n, 32); i += 32) {
      sum0 += x[i+0] * y[i+0];
      sum1 += x[i+1] * y[i+1];
      sum2 += x[i+2] * y[i+2];
      sum3 += x[i+3] * y[i+3];
      sum4 += x[i+4] * y[i+4];
      sum5 += x[i+5] * y[i+5];
      sum6 += x[i+6] * y[i+6];
      sum7 += x[i+7] * y[i+7];
      sum8 += x[i+8] * y[i+8];
      sum9 += x[i+9] * y[i+9];
      sum10 += x[i+10] * y[i+10];
      sum11 += x[i+11] * y[i+11];
      sum12 += x[i+12] * y[i+12];
      sum13 += x[i+13] * y[i+13];
      sum14 += x[i+14] * y[i+14];
      sum15 += x[i+15] * y[i+15];
      sum16 += x[i+16] * y[i+16];
      sum17 += x[i+17] * y[i+17];
      sum18 += x[i+18] * y[i+18];
      sum19 += x[i+19] * y[i+19];
      sum20 += x[i+20] * y[i+20];
      sum21 += x[i+21] * y[i+21];
      sum22 += x[i+22] * y[i+22];
      sum23 += x[i+23] * y[i+23];
      sum24 += x[i+24] * y[i+24];
      sum25 += x[i+25] * y[i+25];
      sum26 += x[i+26] * y[i+26];
      sum27 += x[i+27] * y[i+27];
      sum28 += x[i+28] * y[i+28];
      sum29 += x[i+29] * y[i+29];
      sum30 += x[i+30] * y[i+30];
      sum31 += x[i+31] * y[i+31];
    }
    double sum = sum0 + sum1 + sum2 + sum3 + sum4 + sum5 + sum6 + sum7
                + sum8 + sum9 + sum10 + sum11 + sum12 + sum13 + sum14 + sum15
                + sum16 + sum17 + sum18 + sum19 + sum20 + sum21 + sum22 + sum23
                + sum24 + sum25 + sum26 + sum27 + sum28 + sum29 + sum30 + sum31;
    for (; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }

  final static VectorSpecies<Double> DMAX = DoubleVector.SPECIES_MAX;

  public static double vector(double[] x, double[] y, int n) {
    DoubleVector vsum = DoubleVector.zero(DMAX);
    int i = 0;
    for (; i < DMAX.loopBound(n); i += DMAX.length()) {
      DoubleVector vx = DoubleVector.fromArray(DMAX, x, i);
      DoubleVector vy = DoubleVector.fromArray(DMAX, y, i);
      vsum = vx.mul(vy).add(vsum);

    }
    double sum = vsum.reduceLanes(VectorOperators.ADD);
    for (; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }

  public static double vectorUnroll(double[] x, double[] y, int n) {
    DoubleVector vsum0 = DoubleVector.zero(DMAX);
    DoubleVector vsum1 = DoubleVector.zero(DMAX);
    DoubleVector vsum2 = DoubleVector.zero(DMAX);
    DoubleVector vsum3 = DoubleVector.zero(DMAX);
    int i = 0;
    for (; i < loopBound(n, DMAX.length() * 4); i += DMAX.length() * 4) {
      DoubleVector vx0 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 0);
      DoubleVector vy0 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 0);
      DoubleVector vx1 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 1);
      DoubleVector vy1 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 1);
      DoubleVector vx2 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 2);
      DoubleVector vy2 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 2);
      DoubleVector vx3 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 3);
      DoubleVector vy3 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 3);
      vsum0 = vx0.mul(vy0).add(vsum0);
      vsum1 = vx1.mul(vy1).add(vsum1);
      vsum2 = vx2.mul(vy2).add(vsum2);
      vsum3 = vx3.mul(vy3).add(vsum3);
    }
    double sum = vsum0.reduceLanes(VectorOperators.ADD)
                + vsum1.reduceLanes(VectorOperators.ADD)
                + vsum2.reduceLanes(VectorOperators.ADD)
                + vsum3.reduceLanes(VectorOperators.ADD);
    for (; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }

  public static double vectorUnrollALot(double[] x, double[] y, int n) {
    DoubleVector vsum0 = DoubleVector.zero(DMAX);
    DoubleVector vsum1 = DoubleVector.zero(DMAX);
    DoubleVector vsum2 = DoubleVector.zero(DMAX);
    DoubleVector vsum3 = DoubleVector.zero(DMAX);
    DoubleVector vsum4 = DoubleVector.zero(DMAX);
    DoubleVector vsum5 = DoubleVector.zero(DMAX);
    DoubleVector vsum6 = DoubleVector.zero(DMAX);
    DoubleVector vsum7 = DoubleVector.zero(DMAX);
    DoubleVector vsum8 = DoubleVector.zero(DMAX);
    DoubleVector vsum9 = DoubleVector.zero(DMAX);
    DoubleVector vsum10 = DoubleVector.zero(DMAX);
    DoubleVector vsum11 = DoubleVector.zero(DMAX);
    DoubleVector vsum12 = DoubleVector.zero(DMAX);
    DoubleVector vsum13 = DoubleVector.zero(DMAX);
    DoubleVector vsum14 = DoubleVector.zero(DMAX);
    DoubleVector vsum15 = DoubleVector.zero(DMAX);
    DoubleVector vsum16 = DoubleVector.zero(DMAX);
    DoubleVector vsum17 = DoubleVector.zero(DMAX);
    DoubleVector vsum18 = DoubleVector.zero(DMAX);
    DoubleVector vsum19 = DoubleVector.zero(DMAX);
    DoubleVector vsum20 = DoubleVector.zero(DMAX);
    DoubleVector vsum21 = DoubleVector.zero(DMAX);
    DoubleVector vsum22 = DoubleVector.zero(DMAX);
    DoubleVector vsum23 = DoubleVector.zero(DMAX);
    DoubleVector vsum24 = DoubleVector.zero(DMAX);
    DoubleVector vsum25 = DoubleVector.zero(DMAX);
    DoubleVector vsum26 = DoubleVector.zero(DMAX);
    DoubleVector vsum27 = DoubleVector.zero(DMAX);
    DoubleVector vsum28 = DoubleVector.zero(DMAX);
    DoubleVector vsum29 = DoubleVector.zero(DMAX);
    DoubleVector vsum30 = DoubleVector.zero(DMAX);
    DoubleVector vsum31 = DoubleVector.zero(DMAX);
    int i = 0;
    for (; i < loopBound(n, DMAX.length() * 32); i += DMAX.length() * 32) {
      DoubleVector vx0 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 0);
      DoubleVector vy0 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 0);
      DoubleVector vx1 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 1);
      DoubleVector vy1 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 1);
      DoubleVector vx2 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 2);
      DoubleVector vy2 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 2);
      DoubleVector vx3 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 3);
      DoubleVector vy3 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 3);
      DoubleVector vx4 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 4);
      DoubleVector vy4 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 4);
      DoubleVector vx5 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 5);
      DoubleVector vy5 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 5);
      DoubleVector vx6 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 6);
      DoubleVector vy6 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 6);
      DoubleVector vx7 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 7);
      DoubleVector vy7 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 7);
      DoubleVector vx8 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 8);
      DoubleVector vy8 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 8);
      DoubleVector vx9 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 9);
      DoubleVector vy9 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 9);
      DoubleVector vx10 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 10);
      DoubleVector vy10 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 10);
      DoubleVector vx11 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 11);
      DoubleVector vy11 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 11);
      DoubleVector vx12 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 12);
      DoubleVector vy12 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 12);
      DoubleVector vx13 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 13);
      DoubleVector vy13 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 13);
      DoubleVector vx14 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 14);
      DoubleVector vy14 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 14);
      DoubleVector vx15 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 15);
      DoubleVector vy15 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 15);
      DoubleVector vx16 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 16);
      DoubleVector vy16 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 16);
      DoubleVector vx17 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 17);
      DoubleVector vy17 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 17);
      DoubleVector vx18 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 18);
      DoubleVector vy18 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 18);
      DoubleVector vx19 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 19);
      DoubleVector vy19 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 19);
      DoubleVector vx20 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 20);
      DoubleVector vy20 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 20);
      DoubleVector vx21 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 21);
      DoubleVector vy21 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 21);
      DoubleVector vx22 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 22);
      DoubleVector vy22 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 22);
      DoubleVector vx23 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 23);
      DoubleVector vy23 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 23);
      DoubleVector vx24 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 24);
      DoubleVector vy24 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 24);
      DoubleVector vx25 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 25);
      DoubleVector vy25 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 25);
      DoubleVector vx26 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 26);
      DoubleVector vy26 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 26);
      DoubleVector vx27 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 27);
      DoubleVector vy27 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 27);
      DoubleVector vx28 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 28);
      DoubleVector vy28 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 28);
      DoubleVector vx29 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 29);
      DoubleVector vy29 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 29);
      DoubleVector vx30 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 30);
      DoubleVector vy30 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 30);
      DoubleVector vx31 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 31);
      DoubleVector vy31 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 31);
      vsum0 = vx0.mul(vy0).add(vsum0);
      vsum1 = vx1.mul(vy1).add(vsum1);
      vsum2 = vx2.mul(vy2).add(vsum2);
      vsum3 = vx3.mul(vy3).add(vsum3);
      vsum4 = vx4.mul(vy4).add(vsum4);
      vsum5 = vx5.mul(vy5).add(vsum5);
      vsum6 = vx6.mul(vy6).add(vsum6);
      vsum7 = vx7.mul(vy7).add(vsum7);
      vsum8 = vx8.mul(vy8).add(vsum8);
      vsum9 = vx9.mul(vy9).add(vsum9);
      vsum10 = vx10.mul(vy10).add(vsum10);
      vsum11 = vx11.mul(vy11).add(vsum11);
      vsum12 = vx12.mul(vy12).add(vsum12);
      vsum13 = vx13.mul(vy13).add(vsum13);
      vsum14 = vx14.mul(vy14).add(vsum14);
      vsum15 = vx15.mul(vy15).add(vsum15);
      vsum16 = vx16.mul(vy16).add(vsum16);
      vsum17 = vx17.mul(vy17).add(vsum17);
      vsum18 = vx18.mul(vy18).add(vsum18);
      vsum19 = vx19.mul(vy19).add(vsum19);
      vsum20 = vx20.mul(vy20).add(vsum20);
      vsum21 = vx21.mul(vy21).add(vsum21);
      vsum22 = vx22.mul(vy22).add(vsum22);
      vsum23 = vx23.mul(vy23).add(vsum23);
      vsum24 = vx24.mul(vy24).add(vsum24);
      vsum25 = vx25.mul(vy25).add(vsum25);
      vsum26 = vx26.mul(vy26).add(vsum26);
      vsum27 = vx27.mul(vy27).add(vsum27);
      vsum28 = vx28.mul(vy28).add(vsum28);
      vsum29 = vx29.mul(vy29).add(vsum29);
      vsum30 = vx30.mul(vy30).add(vsum30);
      vsum31 = vx31.mul(vy31).add(vsum31);
    }
    double sum = vsum0.reduceLanes(VectorOperators.ADD)
                + vsum1.reduceLanes(VectorOperators.ADD)
                + vsum2.reduceLanes(VectorOperators.ADD)
                + vsum3.reduceLanes(VectorOperators.ADD)
                + vsum4.reduceLanes(VectorOperators.ADD)
                + vsum5.reduceLanes(VectorOperators.ADD)
                + vsum6.reduceLanes(VectorOperators.ADD)
                + vsum7.reduceLanes(VectorOperators.ADD)
                + vsum8.reduceLanes(VectorOperators.ADD)
                + vsum9.reduceLanes(VectorOperators.ADD)
                + vsum10.reduceLanes(VectorOperators.ADD)
                + vsum11.reduceLanes(VectorOperators.ADD)
                + vsum12.reduceLanes(VectorOperators.ADD)
                + vsum13.reduceLanes(VectorOperators.ADD)
                + vsum14.reduceLanes(VectorOperators.ADD)
                + vsum15.reduceLanes(VectorOperators.ADD)
                + vsum16.reduceLanes(VectorOperators.ADD)
                + vsum17.reduceLanes(VectorOperators.ADD)
                + vsum18.reduceLanes(VectorOperators.ADD)
                + vsum19.reduceLanes(VectorOperators.ADD)
                + vsum20.reduceLanes(VectorOperators.ADD)
                + vsum21.reduceLanes(VectorOperators.ADD)
                + vsum22.reduceLanes(VectorOperators.ADD)
                + vsum23.reduceLanes(VectorOperators.ADD)
                + vsum24.reduceLanes(VectorOperators.ADD)
                + vsum25.reduceLanes(VectorOperators.ADD)
                + vsum26.reduceLanes(VectorOperators.ADD)
                + vsum27.reduceLanes(VectorOperators.ADD)
                + vsum28.reduceLanes(VectorOperators.ADD)
                + vsum29.reduceLanes(VectorOperators.ADD)
                + vsum30.reduceLanes(VectorOperators.ADD)
                + vsum31.reduceLanes(VectorOperators.ADD);
    for (; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }

  public static double vectorUnrollFma(double[] x, double[] y, int n) {
    DoubleVector vsum0 = DoubleVector.zero(DMAX);
    DoubleVector vsum1 = DoubleVector.zero(DMAX);
    DoubleVector vsum2 = DoubleVector.zero(DMAX);
    DoubleVector vsum3 = DoubleVector.zero(DMAX);
    int i = 0;
    for (; i < loopBound(n, DMAX.length() * 4); i += DMAX.length() * 4) {
      DoubleVector vx0 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 0);
      DoubleVector vy0 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 0);
      DoubleVector vx1 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 1);
      DoubleVector vy1 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 1);
      DoubleVector vx2 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 2);
      DoubleVector vy2 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 2);
      DoubleVector vx3 = DoubleVector.fromArray(DMAX, x, i + DMAX.length() * 3);
      DoubleVector vy3 = DoubleVector.fromArray(DMAX, y, i + DMAX.length() * 3);
      vsum0 = vx0.fma(vy0, vsum0);
      vsum1 = vx1.fma(vy1, vsum1);
      vsum2 = vx2.fma(vy2, vsum2);
      vsum3 = vx3.fma(vy3, vsum3);
    }
    double sum = vsum0.reduceLanes(VectorOperators.ADD)
                + vsum1.reduceLanes(VectorOperators.ADD)
                + vsum2.reduceLanes(VectorOperators.ADD)
                + vsum3.reduceLanes(VectorOperators.ADD);
    for (; i < n; i++) {
      sum += x[i] * y[i];
    }
    return sum;
  }
}
