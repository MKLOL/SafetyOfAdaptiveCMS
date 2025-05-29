package CountSketch;

public class ModInt {
  private long x, p;

  public ModInt(long x, long p) {
    this.x = x % p;
    this.p = p;
  }

  public long getX() {
    return x;
  }

  public long getP() {
    return p;
  }

  public ModInt add(long x) {
    this.x = (this.x + x) % p;
    return this;
  }

  public ModInt sub(long x) {
    this.x = (this.x - x + p) % p;
    return this;
  }

  public ModInt add(int x) {
    return this.add((long) x);

  }

  public ModInt sub(int x) {
    return this.sub((long) x);
  }

  public ModInt add(ModInt other) {
    assert other.p == this.p;
    return this.add(other.x);
  }

  public ModInt sub(ModInt other) {
    assert other.p == this.p;
    return this.sub(other.x);
  }

  public ModInt mult(ModInt other) {
    assert other.p == this.p;
    this.x = (this.x * other.x) % p;
    return this;
  }

  public ModInt pow(long pw) {
    ModInt result = new ModInt(1, p);
    ModInt base = new ModInt(x, p);
    while (pw > 0) {
      if ((pw & 1) != 0) {
        result.mult(base);
      }
      base.mult(base);
      pw >>= 1;
    }
    return result;
  }

  public ModInt div(ModInt other) {
    assert other.p == this.p;
    ModInt inv = other.pow(p - 2);
    this.mult(inv);
    return this;
  }

  public ModInt div(long y) {
    return this.div(new ModInt(y, p));
  }

  public ModInt div(int y) {
    return this.div((long) y);
  }
}