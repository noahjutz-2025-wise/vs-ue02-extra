import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatch2 {
  private int count = 0;
  private final int max;

  public MyCountDownLatch2(int max) {
    this.max = max;
  }

  private final Lock l = new ReentrantLock();
  private final Condition condition = l.newCondition();

  public void await() {
    l.lock();
    while (count < max) {
      try {
        condition.await();
      } catch (InterruptedException _) {
      }
    }
    l.unlock();
  }

  public void countDown() {
    l.lock();
    count++;
    if (count >= max) {
      condition.signalAll();
    }
    l.unlock();
  }

  public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
    var nanos = unit.toNanos(timeout);

    l.lock();
    try {
      while (count < max) {
        if (nanos <= 0) {
          return false;
        }
        nanos = condition.awaitNanos(nanos);
      }
      return true;
    } finally {
      l.unlock();
    }
  }
}
