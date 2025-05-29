
import apache.cms.CountMinSketchImpl;
public class TestMain {
  public static void main(String args[]) {
    CountMinSketchImpl countMinSketch = new CountMinSketchImpl(20, 200, 2);
    countMinSketch.add(1, 10);
    countMinSketch.add(1, 20);
    countMinSketch.add(3, 21);
    System.out.println(countMinSketch.estimateCount(1));
    System.out.println(countMinSketch.estimateCount(3));

  }
}
