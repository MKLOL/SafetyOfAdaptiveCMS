package CountSketch;

import java.util.Arrays;

import static java.lang.Math.min;

public class CountMinSketch<T> implements AbstractCountSketch<T> {
  private final int numBuckets;
  private final int numRows;
  private final long[][] count;

  public HashFn<T>[] getHashFns() {
    return hashFns;
  }

  private final HashFn<T>[] hashFns;
  private int operationCount = 0;

  @Override
  public String toString() {
    return "CountMinSketch{" +
            "numBuckets=" + numBuckets +
            ", numRuns=" + numRows +
            ", hashFns=" + Arrays.toString(hashFns) +
            ", operationCount=" + operationCount +
            '}';
  }

  /**
   * Same as previous one only the functions are given.
   */
  public CountMinSketch(int numBuckets, int numRows, HashFn<T>[] hashFns) {
    this.numBuckets = numBuckets;
    this.numRows = numRows;
    if (this.numBuckets <= 0 || this.numRows <= 0) {
      throw new IllegalArgumentException("Invalid CMS size");
    }

    count = new long[this.numRows][this.numBuckets];
    this.hashFns = hashFns;
    if (this.hashFns.length != numRows) {
      throw new IllegalArgumentException("Bucket count must be equal to hash function length");
    }

  }


  public void addElement(T obj, long val) {
    operationCount++;
    for (int i = 0; i < this.numRows; ++i) {
      this.count[i][this.hashFns[i].getHash(obj, this.numBuckets)] += val;
    }
  }

  public long getCount(T obj) {
    operationCount++;
    long ret = Long.MAX_VALUE;
    for (int i = 0; i < this.numRows; ++i) {
      ret = min(ret, this.count[i][this.hashFns[i].getHash(obj, numBuckets)]);
    }
    return ret;
  }

  public void clear() {
    operationCount++;
    for (int i = 0; i < this.numRows; ++i) {
      for (int j = 0; j < this.numBuckets; ++j) {
        this.count[i][j] = 0;
      }
    }
  }


  public int getOperationCount() {
    return operationCount;
  }

}
