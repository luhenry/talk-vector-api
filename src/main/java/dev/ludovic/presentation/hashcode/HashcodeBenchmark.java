
package dev.ludovic.presentation.hashcode;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import static dev.ludovic.presentation.BenchmarkSupport.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 3, jvmArgs = "--add-modules=jdk.incubator.vector")
@Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
public class HashcodeBenchmark {

  @Param({"100000"})
  public int n;

  public int[] arr;

  @Setup
  public void setup() {
    arr = randomIntArray(n);
  }

  @Benchmark
  public int scalar() {
    return Hashcode.scalar(arr);
  }

  @Benchmark
  public int scalarUnroll() {
    return Hashcode.scalarUnroll(arr);
  }

  @Benchmark
  public int vector() {
    return Hashcode.vector(arr);
  }

  @Benchmark
  public int scalarBackward() {
    return Hashcode.scalarBackward(arr);
  }

  @Benchmark
  public int vectorBackward() {
    return Hashcode.vectorBackward(arr);
  }
  @Benchmark
  public int vectorBackwardUnroll() {
    return Hashcode.vectorBackwardUnroll(arr);
  }
}
