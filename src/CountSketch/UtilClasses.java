package CountSketch;

import java.util.Objects;

public class UtilClasses {
  public static class Pair<T, K> {
    private final T left;
    private final K right;

    public Pair(T left, K right) {
      this.left = left;
      this.right = right;
    }

    public static <K, T> Pair<K, T> of(K a, T b) {
      return new Pair<K, T>(a, b);
    }

    public T getLeft() {
      return left;
    }

    public K getRight() {
      return right;
    }

    @Override
    public String toString() {
      return "Pair{" +
              "left=" + left +
              ", right=" + right +
              '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pair<?, ?> pair = (Pair<?, ?>) o;
      return left.equals(pair.left) && right.equals(pair.right);
    }

    @Override
    public int hashCode() {
      return Objects.hash(left, right);
    }
  }


}
