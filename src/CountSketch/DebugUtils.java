package CountSketch;

import java.util.List;

public class DebugUtils {
  /**
   * Just prints a list!
   *
   * @param ls the list
   */
  public static void printDoubleList(List<Double> ls) {
    for (Double x : ls) {
      System.out.print(x + " ");
    }
    System.out.println();
  }
}
