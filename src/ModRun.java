import CountSketch.HashFnInt;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;

public class ModRun {
  public static void main(String[] args) throws Exception {
    experiment1();
    experiment2();
    experiment3();
    experiment4();
  }

  public static void experiment1() throws Exception {
    Random rn = new Random();
    int TT = 20;
    int B = 200;
    FileWriter fileWriter = new FileWriter("modtest_3.out");
    for (int PRIME = 2000; PRIME <= 4000; ++PRIME) {
      if (!HashFnInt.isPrime(PRIME)) continue;

      for (int i = 0; i < TT; ++i) {
        ModExperiment experiment = new ModExperiment(rn.nextInt(PRIME - 1) + 1, rn.nextInt(PRIME - 1) + 1, PRIME, B);
        fileWriter.write(PRIME + " " + experiment.runFunctionResult() + "\n");
        fileWriter.flush();
      }
    }
  }

  public static void experiment2() throws Exception {
    Random rn = new Random();
    int TT = 20;
    int B = 200;
    FileWriter fileWriter = new FileWriter("modtest_2.out");
    for (int PRIME = 2000; PRIME <= 4000; ++PRIME) {
      if (!HashFnInt.isPrime(PRIME)) continue;
      for (int i = 0; i < TT; ++i) {
        ModExperiment experiment = new ModExperiment(rn.nextInt(PRIME - 1) + 1, rn.nextInt(PRIME - 1) + 1, PRIME, B);
        fileWriter.write(PRIME + " " + experiment.runRandomCollision() + "\n");
        fileWriter.flush();
      }
    }
  }

  public static void experiment3() throws Exception {
    Random rn = new Random();
    int TT = 10000;
    int pp = 100;
    int P = 666013;
    int a1 = 0, a2 = 0, b1 = 0, b2 = 0;
    FileWriter fileWriter = new FileWriter("target_collision.out");

    for (int B = 200; B <= 1000; B += 50) {
      int collisions = 0;
      for (int t = 0; t < TT; ++t) {
        // new pair of hashes
        if (t % pp == 0) {
          collisions = 0;
          a1 = rn.nextInt(P - 1) + 1;
          a2 = rn.nextInt(P - 1) + 1;
          b1 = rn.nextInt(P);
          b2 = rn.nextInt(P);
        }
        int target = rn.nextInt(P);
        int h1 = ((a1 * target + b1) % P) % B;
        int h2 = ((a2 * target + b2) % P) % B;
        for (int x = 0; x < P; ++x) {
          if (x == target) continue;
          if (((a1 * x + b1) % P) % B == h1 && ((a2 * x + b2) % P) % B == h2) {
            ++collisions;
            break;
          }
        }
        if ((t + 1) % pp == 0) {
          fileWriter.write(B + " " + 1.0 * collisions / pp + "\n");
        }
      }
      fileWriter.flush();
    }
  }

  public static void experiment4() throws Exception {
    Random rn = new Random();
    int P = 15377;
    FileWriter fileWriter = new FileWriter("ratio.out");

    for (int B = 115; B <= 245; B += 10) {
      for (int t = 1; t <= 10; ++t) {
        int num = 0;
        int a1 = rn.nextInt(P - 1) + 1;
        int b1 = rn.nextInt(P);
        int b2 = rn.nextInt(P);

        for (int i = 1; i < P; ++i) {
          int a2 = i;
          HashSet<Pair<Integer, Integer>> hs = new HashSet<>();
          for (int x = 0; x < P; ++x) {
            int h1 = ((a1 * x + b1) % P) % B;
            int h2 = ((a2 * x + b2) % P) % B;
            Pair<Integer, Integer> p = new Pair<>(h1, h2);
            if (hs.contains(p)) break;
            hs.add(p);
          }
          if (hs.size() == P) ++num;
        }
        fileWriter.write(B + " " + 1.0 * num / (P - 1) + "\n");
      }
      fileWriter.flush();
    }
  }
}
