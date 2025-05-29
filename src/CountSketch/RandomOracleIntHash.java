package CountSketch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomOracleIntHash implements HashFn<Integer> {
  private Random rn;
  private int countdown;
  Map<Integer, Integer> h;

  public RandomOracleIntHash() {
    rn = new Random();
    countdown = rn.nextInt(100) + 100;
    h = new HashMap<>();
  }

  void refresh() {
    rn = new Random();
    countdown = rn.nextInt(100) + 100;
  }

  @Override
  public int getHash(Integer s, int size) {
    if (h.get(s) == null) {
      --countdown;
      h.put(s, rn.nextInt(size));
      if (countdown < 0) {
        refresh();
      }
    }
    return h.get(s);
  }

  public static ArrayList<RandomOracleIntHash> getHashes(int n) {
    ArrayList<RandomOracleIntHash> r = new ArrayList<>();
    for (int i = 0; i < n; ++i) {
      r.add(new RandomOracleIntHash());
    }
    return r;
  }
}
