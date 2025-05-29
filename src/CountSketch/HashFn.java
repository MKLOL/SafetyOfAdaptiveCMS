package CountSketch;

public interface HashFn<T> {
  /**
   * Get a hash of element
   *
   * @param s    the element
   * @param size the range from 0 to size we should return
   * @return the return value
   */
  int getHash(T s, int size);
}
