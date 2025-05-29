package CountSketch;

import java.util.*;
import java.util.stream.Collectors;

public class CountMinSketchBreaker {
  private final long RND_SIZE = 1000000000L;
  private final long START_SIZE = 1000000000L * 20L;
  private final long TARGET_SIZE = START_SIZE * 200L;

  private final AbstractCountSketch<Integer> cms;
  private final Random rn = new Random();
  private int untilFirstHit = 0;

  public CountMinSketchBreaker(AbstractCountSketch<Integer> cms) {
    this.cms = cms;
  }

  /**
   * Computation function for computin f(x,i), see pdf for details
   *
   * @param cache, to memoize the results
   * @param i,x,   the parameters of function f
   * @param B      what the current B values is
   * @return returns f(i,x)
   */
  private static double compute(Map<UtilClasses.Pair<Integer, Integer>, Double> cache, int i, int x, double B) {
    if (i == x) {
      return Math.pow(1 / B, i);
    }
    if (x == 0) {
      return Math.pow((1 - 1 / B), i);
    }


    if (cache.containsKey(UtilClasses.Pair.of(i, x))) {
      return cache.get(UtilClasses.Pair.of(i, x));
    }
    double ret = (1 - 1 / B) * compute(cache, i - 1, x, B) + 1 / B * compute(cache, i - 1, x - 1, B);
    cache.put(UtilClasses.Pair.of(i, x), ret);
    return ret;
  }

  /**
   * A counter for how long it took until cms.get(target) is not zero
   *
   * @return the count
   */
  public int getUntilFirstHit() {
    return untilFirstHit;
  }

  /**
   * Tries to reconstruct in case two elements colieded.
   *
   * @param revhm map fro value to element
   * @param val   value we're trying to obtain
   * @return an array of what elements can cause this. If the solution is not unique we return an empty list
   */
  private static ArrayList<Long> tryReconstruct(AbstractCountSketch<Integer> cms, Map<Long, Long> revhm, long val, Integer target, int sz) {
    int cnt = 0;
    ArrayList<Long> arrx = new ArrayList<>();
    for (Long x : revhm.keySet()) {
      if (revhm.containsKey(val - x) && x < val - x) {
        ++cnt;
        arrx.add(revhm.get(x));
        arrx.add(revhm.get(val - x));
      }
    }

    if (cnt == 1) {
      return arrx;
    } else {
      long curVal = cms.getCount(target);
      for (Long x : arrx) {
        cms.addElement(x.intValue(), -1);
        long updatedVal = cms.getCount(target);
        cms.addElement(x.intValue(), +1);
        if (updatedVal != curVal) {
          ArrayList<Long> ret = new ArrayList<>();
          ret.add(x);
          return ret;
        }
      }
      return new ArrayList<>();
    }
  }

  /**
   * Auxiliery function to add to CMS since we use it in multiple places
   *
   * @param cur    current element
   * @param target target of our attack
   * @param hm     map from element to value
   * @param revhm  reverse of hm, map from value to element
   * @return true if we've succesfully added it. we might not add it in some cases like current element / value collision
   */
  private boolean addToCms(long cur, long target, Map<Long, Long> hm, Map<Long, Long> revhm) {
    if (cur == target || hm.containsKey(cur)) return false;
    long valToAdd = START_SIZE + rn.nextInt((int) RND_SIZE);
    if (revhm.containsKey(valToAdd)) {
      return false;
    }
    hm.put(cur, valToAdd);
    revhm.put(valToAdd, cur);
    cms.addElement((int) cur, valToAdd);
    return true;
  }

  public static Integer removeRandomElement(ArrayList<Integer> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }

    Random random = new Random();
    int randomIndex = random.nextInt(list.size());
    Integer ret = list.get(randomIndex);
    list.set(randomIndex, list.get(list.size() - 1));
    list.remove(list.size() - 1);
    return ret;
  }

  /**
   * Tries to break the element target
   *
   * @param target the target
   * @return a list of elements that increase target's weight
   */
  public List<Integer> breakElement(Integer target) {
    Map<Long, Long> hm = new HashMap<>();
    Map<Long, Long> revhm = new HashMap<>();
    ArrayList<Integer> allElements = new ArrayList<>();
    int untilBreak = 0;
    Set<Integer> arx = new HashSet<>();
    while (untilBreak < 1 && cms.getCount(target) < TARGET_SIZE) {
      cms.clear();
      hm.clear();
      revhm.clear();
      for (Integer alreadyKnown : arx) {
        cms.addElement(alreadyKnown, TARGET_SIZE + 1);
      }
      if (allElements.size() == 0) {
        int nrq = 0;
        while (cms.getCount(target) == 0) {
          ++nrq;
          if (nrq % 500000 == 0) {
            System.out.println("CURRENT SIZE IS BIG, VALUE IS: " + cms.getCount(target));
            System.out.println("CURRENT SIZE IS BIG, VALUE IS: " + (allElements.size());
          }
          int cur = (int) (rn.nextLong() % START_SIZE);
          if (addToCms(cur, target, hm, revhm)) {
            allElements.add(cur);
          }
        }
        this.untilFirstHit = allElements.size();
      } else {
        for (Integer cur : allElements) {
          addToCms(cur, target, hm, revhm);
        }
      }


      int nrn = 0;
      while (cms.getCount(target) < TARGET_SIZE) {
        ++nrn;
        if (nrn % 100000 == 0) {
          System.out.println("Count of target is:" + cms.getCount(target));
        }
        long val = cms.getCount(target);
        if (val >= 3 * START_SIZE) {
          while (cms.getCount(target) >= 3 * START_SIZE) {
            Integer as = removeRandomElement(allElements);
            Long valt = hm.get(as.longValue());
            hm.remove(as.longValue());
            revhm.remove(valt);
            cms.addElement(as, -valt);
          }
        } else if (val >= 2 * START_SIZE) {
          ArrayList<Long> recArr = tryReconstruct(cms, revhm, val, target, 2);

          if (recArr.size() == 0) {
            System.out.println("FAILED AFTER SECOND ATTEMPT TYPE:" + arx.size());
            System.out.println("Info:");
            System.out.println(val / START_SIZE);
            System.out.println(hm.size());
            System.out.println("\n\n");
            break;
          }
          arx.add(recArr.get(0).intValue());
          cms.addElement(recArr.get(0).intValue(), TARGET_SIZE);
        } else {
          arx.add(revhm.get(val).intValue());
          cms.addElement(revhm.get(val).intValue(), TARGET_SIZE);
        }
      }

      ++untilBreak;
    }
    int nrn = 0;
    while (cms.getCount(target) < TARGET_SIZE) {
      int toAdd = -1;
      ++nrn;
      if (nrn % 1000000 == 0) {
        System.out.println("Count for target is:" + cms.getCount(target));
      }
      long currentVal = cms.getCount(target);
      do {
        ++toAdd;
        cms.addElement(allElements.get(toAdd), 1);
      } while (cms.getCount(target) == currentVal);
      arx.add(allElements.get(toAdd));
      cms.addElement(allElements.get(toAdd), TARGET_SIZE);
    }
    return arx.stream().collect(Collectors.toList());
  }

  /**
   * Same as above only additive fashion.
   */
  public List<Integer> breakElementAdditive(Integer x) {
    Random rn = new Random();
    ArrayList<Integer> arr = new ArrayList<>();
    ArrayList<Integer> arx = new ArrayList<>();

    while (cms.getCount(x) < TARGET_SIZE) {
      long last = cms.getCount(x);
      while (cms.getCount(x) == last) {
        int cur = rn.nextInt();
        if (cur == x) continue;
        cms.addElement(cur, 1);
        arr.add(cur);
      }
      int lastv = arr.get(arr.size() - 1);
      arx.add(lastv);
      cms.addElement(lastv, TARGET_SIZE);
    }
    return arx;
  }

  /**
   * This calculates the bad expected value, see pdf for details
   *
   * @param B
   * @param expectedValue
   * @return the expected value
   */
  private int getExpectedValueBad(Double B, double expectedValue) {
    double current = 0.0;
    double minError = Double.MAX_VALUE;
    int currentBest = 0;
    for (int l = 1; l <= B * 1000; ++l) {
      current += 1.0 / (1 - Math.pow((1 - 1.0 / B), l));
      if (Math.abs(current - expectedValue) < minError) {
        minError = Math.abs(current - expectedValue);
        currentBest = l;
      }
    }
    return currentBest;
  }

  private int getExpectedValueCorrect(Double B, double expectedValue) {
    double minError = Double.MAX_VALUE;
    int currentBest = 0;
    int N = (int) 1e3;
    double[] dp = getListOfExpectedValues(B, N);
    for (int i = 0; i < N; ++i) {
      if (Math.abs(expectedValue - dp[i]) < minError) {
        minError = Math.abs(expectedValue - dp[i]);
        currentBest = i;
      }
    }
    return currentBest;
  }

  /**
   * This does the dynamic programming as explained inside the pdf
   *
   * @param B
   * @param N
   * @return
   */
  public double[] getListOfExpectedValues(double B, int N) {
    double[] dp = new double[N + 1];
    dp[0] = 0;
    Map<UtilClasses.Pair<Integer, Integer>, Double> cache = new HashMap<>();
    for (int i = 1; i <= N; ++i) {
      double p = compute(cache, i, 0, B);
      double cummulativeSum = 1;
      for (int j = i - 1; j >= 0; --j) {
        cummulativeSum += dp[j] * compute(cache, i, i - j, B);
      }
      dp[i] = cummulativeSum / (1.0 - p);
    }
    return dp;
  }

  /**
   * Get the size of the cms
   *
   * @param target
   * @param attack
   * @param attackWork how much work was necessary to produce the attack
   * @return Pair of elements that are the CMS size
   */
  public UtilClasses.Pair<Double, Double> findCMSSize(int target, List<Integer> attack, int attackWork) {
    Set<Integer> valueSet = attack.stream().collect(Collectors.toSet());
    ArrayList<Integer> arr = new ArrayList<>();
    int TO_TEST = 1000;
    double cnt = 0;
    for (int TT = 0; TT < TO_TEST * 2; ++TT) {
      Collections.shuffle(attack);
      cms.clear();

      for (int i = 0; i < attack.size() - 1; ++i) {
        Integer a = attack.get(i);
        cms.addElement(a, 1);
      }
      int cntcur = 0;
      while (cms.getCount(target) == 0) {
        Integer newa = rn.nextInt(1000000000);
        if (valueSet.contains(newa)) {
          continue;
        }
        cms.addElement(newa, 1);
        cntcur++;
      }
      arr.add(cntcur);
    }

    for (int i = 0; i < TO_TEST; ++i) {
      cnt += arr.get(i);
    }
    double total = 0;
    for (int i = 0; i < TO_TEST; ++i) {
      cms.clear();
      int NRN = 0;
      while (cms.getCount(target) == 0) {
        cms.addElement(rn.nextInt(1000000000), 1);
        ++NRN;
      }
      total += NRN;
    }
    double B = cnt / TO_TEST - (10.0 * attack.size() / attackWork);
    return new UtilClasses.Pair<>(B, 1.0 * getExpectedValueCorrect(B, total / TO_TEST));
  }


}
