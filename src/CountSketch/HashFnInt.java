package CountSketch;

import java.util.ArrayList;
import java.util.Random;


/**
 * A family of functions
 * Return ((a * x + b) % p) % BUCKETSIZE;
 */
public class HashFnInt implements HashFn<Integer> {
  private static final Random random = new Random();
  private static final int MIN_VAL = 2000000;

  public int getA() {
    return a;
  }

  public int getB() {
    return b;
  }

  public int getMod() {
    return mod;
  }

  public int getNumBuckets() {
    return numBuckets;
  }

  private final int a;
  private final int b;
  private final int mod;
  private final int numBuckets;

  public HashFnInt(int a, int b, int mod, int numBuckets) {
    this.a = a;
    this.b = b;
    this.mod = mod;
    this.numBuckets = numBuckets;
  }

  /**
   * Checks if a number is prime
   *
   * @param x the number to check
   * @return if it's prime it returns true
   */
  public static boolean isPrime(int x) {
    if (x == 1) return false;
    for (int i = 2; i * i <= x; ++i) {
      if (x % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static int getRandomPrime() {
    // Shouldn't take too long because primes are abundant
    for (int i = MIN_VAL + random.nextInt(MIN_VAL) + 20; ; ++i) {
      if (isPrime(i)) {
        return i;
      }
    }
  }

  public static HashFn getRandomHashFn(int n, int prime) {
    int a = random.nextInt(MIN_VAL) + 1;
    int b = random.nextInt(MIN_VAL) + 1;

    return new HashFnInt(a, b, prime, n);
  }

  /**
   * Gets a random list of hashes
   */
  public static HashFn getRandomHashFn(int n) {
    int start = random.nextInt(MIN_VAL) + 5 * MIN_VAL;
    int prime;
    // Shouldn't take too long because primes are abundant
    for (int i = start; ; ++i) {
      if (isPrime(i)) {
        prime = i;
        break;
      }
    }
    int a = random.nextInt(MIN_VAL) + 1;
    int b = random.nextInt(MIN_VAL) + 1;

    return new HashFnInt(a, b, prime, n);
  }

  public static ArrayList<HashFn<Integer>> intHashes(int n) {
    ArrayList<HashFn<Integer>> arr = new ArrayList<HashFn<Integer>>();
    for (int i = 0; i < n; ++i) {
      arr.add(HashFnInt.getRandomHashFn(n));
    }
    return arr;
  }

  public static ArrayList<HashFn<Integer>> intHashesFixedPrime(int n) {
    int prime = getRandomPrime();
    ArrayList<HashFn<Integer>> arr = new ArrayList<HashFn<Integer>>();
    for (int i = 0; i < n; ++i) {
      arr.add(HashFnInt.getRandomHashFn(n, prime));
    }
    return arr;
  }

  public static ArrayList<HashFn<Integer>> intHashesFixedPrime(int n, int prime) {
    ArrayList<HashFn<Integer>> arr = new ArrayList<HashFn<Integer>>();
    for (int i = 0; i < n; ++i) {
      arr.add(HashFnInt.getRandomHashFn(n, prime));
    }
    return arr;
  }

  /**
   * @param val Computes the hash value of val
   * @return return the ((a * val + b) % p) % size;
   */
  @Override
  public int getHash(Integer val, int size) {
    return Math.abs((int) ((1L * this.a * val + this.b) % this.mod) % size);
  }

  @Override
  public String toString() {
    return "HashFnInt{" +
            "a=" + a +
            ", b=" + b +
            ", mod=" + mod +
            ", numBuckets=" + numBuckets +
            '}';
  }
}
