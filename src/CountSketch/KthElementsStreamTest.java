package CountSketch;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KthElementsStreamTest {
  @Test
  public void testKthElement() {
    Random rnd = new Random(333);
    int N = 1234;

    for (int TT = 0; TT <= 1000; ++TT) {
      ArrayList<KthElementsStream.CountPair<Integer>> arr = new ArrayList<>();
      for (int i = 0; i < N; ++i) {
        arr.add(new KthElementsStream.CountPair<>(i, rnd.nextDouble() * N * 1000));
      }
      int nr = rnd.nextInt(N / 2) + 5;
      KthElementsStream<Integer> kthElementsStream = new KthElementsStream<Integer>(nr);
      Collections.shuffle(arr, rnd);
      for (int i = 0; i < arr.size(); ++i) {
        kthElementsStream.add(arr.get(i));
      }
      Collections.sort(arr);
      List<Double> goodSet = arr.subList(arr.size() - nr, arr.size()).stream().map(x -> x.getCount()).collect(Collectors.toList());
      List<Double> currentSet = kthElementsStream.getTopK().stream().map(x -> x.getCount()).collect(Collectors.toList());
      Collections.sort(goodSet);
      Collections.sort(currentSet);
      Assert.assertEquals(goodSet, currentSet);
    }
  }
}
