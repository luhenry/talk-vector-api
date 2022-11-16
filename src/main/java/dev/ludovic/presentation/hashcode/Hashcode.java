
package dev.ludovic.presentation.hashcode;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import static dev.ludovic.presentation.BenchmarkSupport.loopBound;

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
      res = res      * -1807454463 // 31^8
          + src[i+0] * 1742810335  // 31^7
          + src[i+1] * 887503681   // 31^6
          + src[i+2] * 28629151    // 31^5
          + src[i+3] * 923521      // 31^4
          + src[i+4] * 29791       // 31^3
          + src[i+5] * 961         // 31^2
          + src[i+6] * 31          // 31^1
          + src[i+7] * 1;          // 31^0
    }
    for (; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }

  final static VectorSpecies<Integer> I256 = IntVector.SPECIES_256;

  static final int[] powerOf31Backward = new int[] {
    -1807454463, // 31^8
     1742810335, // 31^7
      887503681, // 31^6
       28629151, // 31^5
         923521, // 31^4
          29791, // 31^3
            961, // 31^2
             31, // 31^1
              1  // 31^0
  };

  public static int vector(int[] src) {
    assert I256.length() == 8;
    IntVector vres = IntVector.zero(I256);
    IntVector vnext = IntVector.broadcast(I256, powerOf31Backward[0]);
    int i = 0;
    for (; i < loopBound(src.length, I256.length()); i += I256.length()) {
      IntVector vsrc = IntVector.fromArray(I256, src, i);
      vres = vres.mul(vnext).add(vsrc);
    }
    ;
    int res = vres.mul(IntVector.fromArray(I256, powerOf31Backward, 1)).reduceLanes(VectorOperators.ADD);
    for (; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }

  static final int[] powerOf31BackwardUnroll = new int[] {
     2111290369, // 31^32
    -2010103841, // 31^31
      350799937, // 31^30
       11316127, // 31^29
      693101697, // 31^28
     -254736545, // 31^27
      961614017, // 31^26
       31019807, // 31^25
    -2077209343, // 31^24
      -67006753, // 31^23
     1244764481, // 31^22
    -2038056289, // 31^21
      211350913, // 31^20
     -408824225, // 31^19
     -844471871, // 31^18
     -997072353, // 31^17
     1353309697, // 31^16
     -510534177, // 31^15
     1507551809, // 31^14
     -505558625, // 31^13
     -293403007, // 31^12
      129082719, // 31^11
    -1796951359, // 31^10
     -196513505, // 31^9
    -1807454463, // 31^8
     1742810335, // 31^7
      887503681, // 31^6
       28629151, // 31^5
         923521, // 31^4
          29791, // 31^3
            961, // 31^2
             31, // 31^1
              1  // 31^0
  };

  public static int vectorUnroll(int[] src) {
    assert I256.length() == 8;
    IntVector vres0 = IntVector.zero(I256);
    IntVector vres1 = IntVector.zero(I256);
    IntVector vres2 = IntVector.zero(I256);
    IntVector vres3 = IntVector.zero(I256);
    IntVector vnext = IntVector.broadcast(I256, powerOf31BackwardUnroll[0]);
    int i = 0;
    for (; i < loopBound(src.length, I256.length()*4); i += I256.length()*4) {
      IntVector vsrc0 = IntVector.fromArray(I256, src, i + I256.length() * 0);
      IntVector vsrc1 = IntVector.fromArray(I256, src, i + I256.length() * 1);
      IntVector vsrc2 = IntVector.fromArray(I256, src, i + I256.length() * 2);
      IntVector vsrc3 = IntVector.fromArray(I256, src, i + I256.length() * 3);
      vres0 = vres0.mul(vnext).add(vsrc0);
      vres1 = vres1.mul(vnext).add(vsrc1);
      vres2 = vres2.mul(vnext).add(vsrc2);
      vres3 = vres3.mul(vnext).add(vsrc3);
    }
    int res = vres0.mul(IntVector.fromArray(I256, powerOf31BackwardUnroll, 1 + I256.length() * 0)).reduceLanes(VectorOperators.ADD)
            + vres1.mul(IntVector.fromArray(I256, powerOf31BackwardUnroll, 1 + I256.length() * 1)).reduceLanes(VectorOperators.ADD)
            + vres2.mul(IntVector.fromArray(I256, powerOf31BackwardUnroll, 1 + I256.length() * 2)).reduceLanes(VectorOperators.ADD)
            + vres3.mul(IntVector.fromArray(I256, powerOf31BackwardUnroll, 1 + I256.length() * 3)).reduceLanes(VectorOperators.ADD);
    for (; i < src.length; i++) {
      res = res * 31 + src[i];
    }
    return res;
  }
}
