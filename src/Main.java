// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import CountSketch.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;

public class Main {

  static HashFnInt getHash(HashFn<Integer> h) {
    if (h instanceof HashFnInt) {
      return (HashFnInt) h;
    }
    return null;
  }

  static void doubleHashExperiment1() throws Exception {
    int rows = 2;
    int buckets = 2000;
    CountMinSketch<Integer> cms = new CountMinSketch<>(buckets, rows, HashFnInt.intHashesFixedPrime(rows).toArray(new HashFnInt[rows]));
    HashFnInt hf = getHash(cms.getHashFns()[0]);
    cms.clear();
    int CNT = 0;
    Random rn = new Random();
    int T = rn.nextInt() % 10000;
    int hs = hf.getHash(T, buckets);
    for (int k = 1; ; ++k) {
      int p = hf.getMod();
      ModInt i = new ModInt(0, p);
      ModInt mhs = new ModInt(hs, p);
      ModInt mk = new ModInt(k, p);
      ModInt ma = new ModInt(hf.getA(), p);
      ModInt mb = new ModInt(hf.getB(), p);
      mhs.add(mb.getX() * k).sub(mb).div(ma);
      cms.addElement((int) mhs.getX(), 1);
      if (cms.getCount(T) == 1) {
        System.out.println("Correct, value of k is: " + k);
        break;
      }
    }

  }

  static final int BUCKET_LIMIT = 200000;

  static void breakCMSExperiment1(boolean randomOracleMode) throws Exception {
    FileWriter writer = new FileWriter("exp1_" + randomOracleMode + ".out");
    int NUM_BUCKETS = 2000;
    while (NUM_BUCKETS <= BUCKET_LIMIT) {
      System.out.println("Currently solving for bucket size: " + NUM_BUCKETS);
      int CNT = 100;
      int hash_cnt = (int) (Math.log(NUM_BUCKETS) / Math.log(2.0));
      for (int TT = 0; TT < CNT; ++TT) {
        int target = 20;
        CountMinSketch<Integer> cms;
        if (!randomOracleMode) {
          cms = new CountMinSketch<Integer>(NUM_BUCKETS, hash_cnt, HashFnInt.intHashesFixedPrime(hash_cnt).toArray(new HashFnInt[hash_cnt]));
        } else {
          cms = new CountMinSketch<Integer>(NUM_BUCKETS, hash_cnt, RandomOracleIntHash.getHashes(hash_cnt).toArray(new RandomOracleIntHash[hash_cnt]));
        }
        CountMinSketchBreaker breaker = new CountMinSketchBreaker(cms);
        List<Integer> toBreak = breaker.breakElement(target);
        cms.clear();
        for (Integer x : toBreak) {
          cms.addElement(x, 1);
        }
        if (cms.getCount(target) != 1) {
          System.out.println("ERROR #2");
          continue;
        }

        writer.write(cms.getOperationCount() + " " + 1.0 * toBreak.size() / breaker.getUntilFirstHit() + " " + NUM_BUCKETS + " " + hash_cnt + "\n");
        writer.flush();
      }
      if (NUM_BUCKETS < 100000) {
        NUM_BUCKETS += 2000;
      } else {
        NUM_BUCKETS += 5000;
      }
    }
  }

  static void breakCMSExperiment2(boolean randomOracleMode) throws Exception {
    int NUM_BUCKETS = 2000;

    FileWriter writer = new FileWriter(new File("exp2_" + randomOracleMode + ".out"));

    while (NUM_BUCKETS <= BUCKET_LIMIT) {
      int CNT = 100;
      int hash_cnt = (int) (Math.log(NUM_BUCKETS) / Math.log(2.0));
      for (int TT = 0; TT < CNT; ++TT) {
        int target = 20;
        CountMinSketch<Integer> cms;
        if (!randomOracleMode) {
          cms = new CountMinSketch<Integer>(NUM_BUCKETS, hash_cnt, HashFnInt.intHashesFixedPrime(hash_cnt).toArray(new HashFnInt[hash_cnt]));
        } else {
          cms = new CountMinSketch<Integer>(NUM_BUCKETS, hash_cnt, RandomOracleIntHash.getHashes(hash_cnt).toArray(new RandomOracleIntHash[hash_cnt]));
        }
        CountMinSketchBreaker breaker = new CountMinSketchBreaker(cms);
        List<Integer> toBreak = breaker.breakElementAdditive(target);
        cms.clear();

        for (Integer x : toBreak) {
          cms.addElement(x, 1);
        }
        if (cms.getCount(target) != 1) {
          System.out.println("ERROR #1");
          continue;
        }

        writer.write(cms.getOperationCount() + " " + 1.0 * toBreak.size() / breaker.getUntilFirstHit() + " " + NUM_BUCKETS + " " + hash_cnt + "\n");
        writer.flush();
      }
      if (NUM_BUCKETS < 100000) {
        NUM_BUCKETS += 2000;
      } else {
        NUM_BUCKETS += 5000;
      }
    }
  }

  static void breakCMSExperiment2Apache() throws Exception {
    int NUM_BUCKETS = 2000;

    FileWriter writer = new FileWriter(new File("exp2_apache" + ".out"));

    while (NUM_BUCKETS <= BUCKET_LIMIT) {
      int CNT = 100;
      int hash_cnt = (int) (Math.log(NUM_BUCKETS) / Math.log(2.0));
      for (int TT = 0; TT < CNT; ++TT) {
        int target = 20;
        AbstractCountSketch<Integer> cms;
        cms = new ApacheCMSWrapper(NUM_BUCKETS, hash_cnt);

        CountMinSketchBreaker breaker = new CountMinSketchBreaker(cms);
        List<Integer> toBreak = breaker.breakElementAdditive(target);
        cms.clear();

        for (Integer x : toBreak) {
          cms.addElement(x, 1);
        }
        if (cms.getCount(target) != 1) {
          System.out.println("ERROR #3");
          continue;
        }

        writer.write(cms.getOperationCount() + " " + 1.0 * toBreak.size() / breaker.getUntilFirstHit() + " " + NUM_BUCKETS + " " + hash_cnt + "\n");
        writer.flush();
      }
      if (NUM_BUCKETS < 100000) {
        NUM_BUCKETS += 2000;
      } else {
        NUM_BUCKETS += 5000;
      }
    }
  }

  static void breakCMSExperiment1Apache() throws Exception {
    FileWriter writer = new FileWriter("exp1_" + "apache" + ".out");
    int NUM_BUCKETS = 2000;
    while (NUM_BUCKETS <= BUCKET_LIMIT) {
      System.out.println("Currently solving for bucket number: " + NUM_BUCKETS);
      int CNT = 100;
      int hash_cnt = (int) (Math.log(NUM_BUCKETS) / Math.log(2.0));
      for (int TT = 0; TT < CNT; ++TT) {
        int target = 20;
        AbstractCountSketch<Integer> cms = new ApacheCMSWrapper(NUM_BUCKETS, hash_cnt);
        CountMinSketchBreaker breaker = new CountMinSketchBreaker(cms);
        List<Integer> toBreak = breaker.breakElement(target);
        cms.clear();
        for (Integer x : toBreak) {
          cms.addElement(x, 1);
        }
        if (cms.getCount(target) != 1) {
          System.out.println("ERROR #4");
          continue;
        }

        writer.write(cms.getOperationCount() + " " + 1.0 * toBreak.size() / breaker.getUntilFirstHit() + " " + NUM_BUCKETS + " " + hash_cnt + "\n");
        writer.flush();
      }
      if (NUM_BUCKETS < 100000) {
        NUM_BUCKETS += 2000;
      } else {
        NUM_BUCKETS += 5000;
      }
    }
  }

  public static void main(String[] args) throws Exception {
    //doubleHashExperiment1();
    breakCMSExperiment1Apache();
    breakCMSExperiment2Apache();
    //breakCMSExperiment1(false);
    //breakCMSExperiment2(false);
    //breakCMSExperiment1(true);
    //breakCMSExperiment2(true);
    /*breakCMSExperiment2(true);*/
  }

}