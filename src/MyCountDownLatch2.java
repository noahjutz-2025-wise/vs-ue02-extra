import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatch2 {
  private int count = 0;
  private final int max;
  private final Timer t = new Timer();

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
    var time = System.currentTimeMillis();
    t.schedule(
        new TimerTask() {
          @Override
          public void run() {
            l.lock();
            System.out.println("SIGNALALL");
            condition.signalAll();
            l.unlock();
          }
        },
        unit.toMillis(timeout));

    l.lock();
    while (System.currentTimeMillis() < time + unit.toMillis(timeout) && count < max) {
      condition.await();
    }
    l.unlock();

    l.lock();
    try {
      return count >= max;
    } finally {
      l.unlock();
    }
  }
}
