package CountSketch;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class KthElementsStream<T extends Comparable<T>> {
  private final TreeSet<CountPair<T>> countPairTreeSet = new TreeSet<>();
  private final int maxSize;

  public KthElementsStream(int maxSize) {
    this.maxSize = maxSize;
  }

  public void add(CountPair<T> element) {
    countPairTreeSet.add(element);
    while (countPairTreeSet.size() > maxSize) {
      countPairTreeSet.remove(countPairTreeSet.first());
    }
  }

  public Set<CountPair<T>> getTopK() {
    return this.countPairTreeSet;
  }

  public static class CountPair<T extends Comparable<T>> implements Comparable<CountPair<T>> {
    private final T element;
    private final Double count;

    public CountPair(T element, Double count) {
      this.element = element;
      this.count = count;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CountPair<?> countPair = (CountPair<?>) o;
      return element.equals(countPair.element) && count.equals(countPair.count);
    }

    @Override
    public int hashCode() {
      return Objects.hash(element, count);
    }

    public T getElement() {
      return element;
    }

    public Double getCount() {
      return count;
    }

    @Override
    public int compareTo(CountPair<T> o) {
      if (count.compareTo(o.count) == 0) {
        return element.compareTo(o.element);
      }
      return count.compareTo(o.count);
    }
  }
}
