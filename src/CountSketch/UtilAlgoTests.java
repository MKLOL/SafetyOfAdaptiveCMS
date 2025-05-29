package CountSketch;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class UtilAlgoTests {
  @Test
  public void testKthElement() {
    Random rnd = new Random(333);
    int N = 25;
    ArrayList<Integer> arr = new ArrayList<>();
    for (int i = 0; i < N; ++i) {
      arr.add(i);
    }
    for (int TT = 0; TT <= 1000; ++TT) {
      Collections.shuffle(arr, rnd);
      int pos = rnd.nextInt(arr.size() - 1);
      Assert.assertEquals((int) UtilAlgorithms.<Integer>getKthElement(arr, pos), UtilAlgorithms.getKthElementBrut(arr, pos));
    }
  }
}