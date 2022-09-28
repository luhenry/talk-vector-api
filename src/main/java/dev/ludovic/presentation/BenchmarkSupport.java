package dev.ludovic.presentation;

import java.util.Arrays;
import java.util.Random;

public class BenchmarkSupport {

    private static final Random rand = new Random(0);
    
    public static double[] randomDoubleArray(int n) {
        double[] res = new double[n];
        for (int i = 0; i < n; i++) {
            res[i] = rand.nextDouble();
        }
        return res;
    }
    
    public static int[] randomIntArray(int n) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = rand.nextInt();
        }
        return res;
    }

    public static byte[] randomAsciiByteArray(int n, byte[] characters) {
        byte[] res = new byte[n];
        for (int i = 0; i < n; i++) {
            res[i] = characters[rand.nextInt(characters.length)];
        }
        return res;
    }

    public static byte[] randomAsciiByteArray(int n) {
        byte[] res = new byte[n];
        for (int i = 0; i < n; i++) {
            res[i] = (byte)(rand.nextInt(0, 128));
        }
        return res;
    }

    public static byte[] randomUtf8ByteArray(int n) {
        int size = 0;
        byte[] res = new byte[3*n];
        for (int i = 0; i < n; i++) {
            switch(rand.nextInt(1, 4)) {
            case 1:
                res[size++] = (byte)(rand.nextInt(0, 128));
                break;
            case 2:
                res[size++] = (byte)(rand.nextInt(0, 1<<5) | 0xc0);
                res[size++] = (byte)(rand.nextInt(0, 1<<6) | 0x80);
                break;
            case 3:
                res[size++] = (byte)(rand.nextInt(0, 1<<4) | 0xe0);
                res[size++] = (byte)(rand.nextInt(0, 1<<6) | 0x80);
                res[size++] = (byte)(rand.nextInt(0, 1<<6) | 0x80);
                break;
            }
        }
        return Arrays.copyOf(res, size);
    }

    public static int loopBound(int length, int stride) {
        return length - (length % stride);
    }
}
