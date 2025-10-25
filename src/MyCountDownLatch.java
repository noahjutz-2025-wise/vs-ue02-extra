public class MyCountDownLatch {
  private int count;

  public MyCountDownLatch(int count) {
    assert count >= 0;
    this.count = count;
  }

  public synchronized void countDown() {
    count--;
    if (count == 0) {
      notifyAll();
    }
  }

  public synchronized void await() {
    while (count > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
  }
}
