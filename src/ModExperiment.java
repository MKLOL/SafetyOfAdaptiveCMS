import CountSketch.HashFnInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ModExperiment {
  HashFnInt hashFnInt;
  int P, B;
  ArrayList<HashFnInt> possible = new ArrayList<>();
  ArrayList<Pair> equality = new ArrayList<>();
  ArrayList<Pair> functResult = new ArrayList<>();
  Random rn = new Random();

  private class Pair {
    int x, y;

    public Pair(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private void init() {
    for (int i = 1; i < P; ++i) {
      for (int j = 1; j < P; ++j) {
        possible.add(new HashFnInt(i, j, P, B));
      }
    }
  }

  private void addRandomCollision() {
    Map<Integer, Integer> S = new HashMap<>();
    while (true) {
      int x = rn.nextInt(P - 1) + 1;
      int y = hashFnInt.getHash(x, B);
      if (S.containsKey(y) && S.get(y) != x) {
        equality.add(new Pair(x, S.get(y)));
        return;
      }
      S.put(y, x);
    }
  }

  private void addFuncResult() {
    int x = rn.nextInt(P - 1) + 1;
    int y = hashFnInt.getHash(x, B);
    this.functResult.add(new Pair(x, y));
  }

  private void addRandomCollision(int t) {
    int x1 = t;
    int y1 = hashFnInt.getHash(x1, B);
    while (true) {
      int x = rn.nextInt(P - 1) + 1;
      int y = hashFnInt.getHash(x, B);
      if (y1 == y) {
        equality.add(new Pair(x, x1));
        return;
      }
    }
  }

  public ModExperiment(int a, int b, int P, int B) {
    hashFnInt = new HashFnInt(a, b, P, B);
    this.P = P;
    this.B = B;
    init();
  }

  private void update() {
    ArrayList<HashFnInt> np = new ArrayList<>();
    for (HashFnInt f : possible) {
      boolean ok = true;
      for (Pair p : equality) {
        if (f.getHash(p.x, B) != f.getHash(p.y, B)) {
          ok = false;
          break;
        }
      }
      for (Pair p : functResult) {
        if (f.getHash(p.x, B) != p.y) {
          ok = false;
          break;
        }
      }
      if (ok) {
        np.add(f);
      }
    }
    possible = np;
  }

  private void printFns() {
    for (HashFnInt f : possible) {
      System.out.println(f.getA() + " " + f.getB());
    }
  }

  public int runRandomCollision() {
    int NRN = 0;
    while (true) {
      ++NRN;
      if (NRN % 2000 == 0) {
        System.out.println("Current size is:" + this.possible.size());
      }
      this.addRandomCollision();
      this.update();
      if (this.possible.size() == 2) {
        break;
      }
    }
    return this.equality.size();
  }

  public int runFunctionResult() {
    int NRN = 0;
    while (true) {
      ++NRN;
      if (NRN % 100 == 0) {
        System.out.println("Current size is:" + this.possible.size());
      }
      this.addFuncResult();
      this.update();
      if (this.possible.size() == 1) {
        break;
      }
    }
    return this.functResult.size();
  }

  public void runTargetResult(int t) {
    while (true) {
      this.addRandomCollision(t);
      this.update();
      if (this.possible.size() == 1) {
        break;
      }
    }
    System.out.println("Final size of list " + this.functResult.size());
  }
}
