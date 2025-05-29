package CountSketch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

public class UtilAlgorithms {

  private static <T> ArrayList<T> getArrayList(List<T> arr) {
    return arr.stream().collect(toCollection(ArrayList<T>::new));
  }

  private static <T extends Comparable<T>> T getKthElementUtil(ArrayList<T> arr, int stx, int drx, int pos, Random rn) {
    T piv = arr.get(rn.nextInt(arr.size()));
    int st = stx;
    int dr = drx;
    if (st == dr) {
      return arr.get(st);
    }
    do {
      while (arr.get(st).compareTo(piv) < 0) {
        ++st;
      }
      while (arr.get(dr).compareTo(piv) > 0) {
        --dr;
      }
      if (st <= dr) {
        T aux = arr.get(st);
        arr.set(st, arr.get(dr));
        arr.set(dr, aux);
        ++st;
        --dr;
      }
    } while (st <= dr);

    if (stx <= pos && pos <= dr) {
      return getKthElementUtil(arr, stx, dr, pos, rn);
    } else if (st <= pos && pos <= drx) {
      return getKthElementUtil(arr, st, drx, pos, rn);
    }
    return arr.get(pos);
  }

  public static <T extends Comparable<T>> T getKthElement(List<T> arr, int pos) {
    if (pos < 0 || pos >= arr.size()) {
      throw new IllegalArgumentException("Array range is bad");
    }
    Random rn = new Random(123);
    return getKthElementUtil(arr.stream().collect(toCollection(ArrayList<T>::new)), 0, arr.size() - 1, pos, rn);
  }

  public static int getKthElementBrut(List<Integer> arr, int pos) {
    if (pos < 0 || pos >= arr.size()) {
      throw new IllegalArgumentException("Array range is bad");
    }
    arr = getArrayList(arr);
    Collections.sort(arr);
    return arr.get(pos);
  }

  public static double getDoubleInRange(double left, double right, Random rn) {
    double x = rn.nextDouble();
    return left + x * (right - left);
  }
}
