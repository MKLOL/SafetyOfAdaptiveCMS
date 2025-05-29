package CountSketch;

/**
 * Generic interface for CM / CMS
 */
public interface AbstractCountSketch<T> {

  /**
   * Adds an element with a certain weight
   *
   * @param s   the element to add
   * @param val the weight
   */
  void addElement(T s, long val);

  /**
   * Gets the count of the element
   *
   * @param s the element
   * @return the count
   */
  long getCount(T s);

  /**
   * Clears the datastructure (aka makes all counts be 0)
   */
  void clear();

  int getOperationCount();
}
