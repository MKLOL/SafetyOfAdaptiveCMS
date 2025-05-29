import CountSketch.AbstractCountSketch;
import apache.cms.CountMinSketchImpl;

import java.util.Random;

public class ApacheCMSWrapper implements AbstractCountSketch<Integer> {
  public CountMinSketchImpl cms;
  private int cnt;

  public ApacheCMSWrapper(int rows, int buckets) {
    Random rn = new Random();
    cms = new CountMinSketchImpl(buckets, rows, rn.nextInt());
  }

  @Override
  public void addElement(Integer s, long val) {
    ++cnt;
    cms.add(s, val);
  }

  @Override
  public long getCount(Integer s) {
    ++cnt;
    return cms.estimateCount(s);
  }

  @Override
  public void clear() {
    ++cnt;
    cms.clear();
  }

  @Override
  public int getOperationCount() {
    return cnt;
  }
}
