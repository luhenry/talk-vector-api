
package dev.ludovic.presentation.dotproduct;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import static dev.ludovic.presentation.BenchmarkSupport.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 3, jvmArgs = "--add-modules=jdk.incubator.vector")
@Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
public class DotProductBenchmark {

  @Param({"100000"})
  public int n;

  public double[] x;
  public double[] y;

  @Setup
  public void setup() {
    x = randomDoubleArray(n);
    y = randomDoubleArray(n);
  }

  @Benchmark
  public double scalar() {
    return DotProduct.scalar(x, y, n);
  }

  @Benchmark
  public double scalarUnroll() {
    return DotProduct.scalarUnroll(x, y, n);
  }

  @Benchmark
  public double scalarUnrollALot() {
    return DotProduct.scalarUnrollALot(x, y, n);
  }

  @Benchmark
  public double vector() {
    return DotProduct.vector(x, y, n);
  }

  @Benchmark
  public double vectorUnroll() {
    return DotProduct.vectorUnroll(x, y, n);
  }

  @Benchmark
  public double vectorUnrollALot() {
    return DotProduct.vectorUnrollALot(x, y, n);
  }

  @Benchmark
  public double vectorUnrollFma() {
    return DotProduct.vectorUnrollFma(x, y, n);
  }
}
