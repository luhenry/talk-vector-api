
package dev.ludovic.presentation.hashcode;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import static dev.ludovic.presentation.BenchmarkSupport.*;

import static jdk.incubator.vector.VectorOperators.*;

public class Hashcode {

  public static int scalar(int[] src) {
    int res = 0;
    for (int i = 0; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }

  public static int scalarUnroll(int[] src) {
    int res = 0;
    int i = 0;
    for (; i < loopBound(src.length, 8); i += 8) {
      res = res      * -1807454463
          + src[i+0] * 1742810335
          + src[i+1] * 887503681
          + src[i+2] * 28629151
          + src[i+3] * 923521
          + src[i+4] * 29791
          + src[i+5] * 961
          + src[i+6] * 31
          + src[i+7];
    }
    for (; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }

  final static VectorSpecies<Integer> D256 = IntVector.SPECIES_256;

  public static int vector(int[] src) {
    int res = 0;
    int i = 0;
    IntVector vcoefs = IntVector.fromArray(D256, new int[] { 1742810335, 887503681, 28629151, 923521, 29791, 961, 31, 1 }, 0);
    for (; i < loopBound(src.length, D256.length()); i += D256.length()) {
      IntVector vsrc = IntVector.fromArray(D256, src, i);
      res = res * -1807454463 + vsrc.mul(vcoefs).reduceLanes(ADD);
    }
    for (; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }

  public static int scalarBackward(int[] src) {
    int res = 0;
    int coef = 1;
    for (int i = src.length - 1; i >= 0; i--) {
      res += src[i] * coef;
      coef *= 31;
    }
    return res;
  }

  public static int vectorBackward(int[] src) {
    int res = 0;
    int coef = 1;
    int i = src.length - 1;
    for (; i >= loopBound(src.length, D256.length()); i--) {
      res += src[i] * coef;
      coef *= 31;
    }
    IntVector vcoef =
      IntVector.fromArray(D256, new int[] { 1742810335, 887503681, 28629151, 923521, 29791, 961, 31, 1 }, 0)
               .mul(coef);
    IntVector vres = IntVector.zero(D256);
    for (i -= D256.length(); i >= 0; i -= D256.length()) {
      IntVector vsrc = IntVector.fromArray(D256, src, i);
      vres = vsrc.mul(vcoef).add(vres);
      vcoef = vcoef.mul(-1807454463);
    }
    res += vres.reduceLanes(ADD);
    return res;
  }

  public static int vectorBackwardUnroll(int[] src) {
    int res = 0;
    int coef = 1;
    int i = src.length - 1;
    for (; i >= loopBound(src.length, D256.length() * 4); i--) {
      res += src[i] * coef;
      coef *= 31;
    }
    IntVector vcoef0 = IntVector.fromArray(D256, new int[] {  1742810335,  887503681,    28629151,     923521,      29791,         961,         31,           1}, 0).mul(coef);
    IntVector vcoef1 = IntVector.fromArray(D256, new int[] {  -510534177, 1507551809,  -505558625, -293403007,  129082719, -1796951359, -196513505, -1807454463}, 0).mul(coef);
    IntVector vcoef2 = IntVector.fromArray(D256, new int[] {   -67006753, 1244764481, -2038056289,  211350913, -408824225,  -844471871, -997072353,  1353309697}, 0).mul(coef);
    IntVector vcoef3 = IntVector.fromArray(D256, new int[] { -2010103841,  350799937,    11316127,  693101697, -254736545,   961614017,   31019807, -2077209343}, 0).mul(coef);
    IntVector vres0 = IntVector.zero(D256);
    IntVector vres1 = IntVector.zero(D256);
    IntVector vres2 = IntVector.zero(D256);
    IntVector vres3 = IntVector.zero(D256);
    for (i -= D256.length() * 4; i >= 0; i -= D256.length() * 4) {
      IntVector vsrc0 = IntVector.fromArray(D256, src, i + D256.length() * 0);
      IntVector vsrc1 = IntVector.fromArray(D256, src, i + D256.length() * 1);
      IntVector vsrc2 = IntVector.fromArray(D256, src, i + D256.length() * 2);
      IntVector vsrc3 = IntVector.fromArray(D256, src, i + D256.length() * 3);
      vres0 = vsrc0.mul(vcoef0).add(vres0);
      vres1 = vsrc1.mul(vcoef1).add(vres1);
      vres2 = vsrc2.mul(vcoef2).add(vres2);
      vres3 = vsrc3.mul(vcoef3).add(vres3);
      vcoef0 = vcoef0.mul(2111290369);
      vcoef1 = vcoef1.mul(2111290369);
      vcoef2 = vcoef2.mul(2111290369);
      vcoef3 = vcoef3.mul(2111290369);
    }
    res += vres0.reduceLanes(ADD) + vres1.reduceLanes(ADD) + vres2.reduceLanes(ADD) + vres3.reduceLanes(ADD);
    return res;
  }
}
