import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

  public synchronized boolean await(long timeout, TimeUnit unit) {
    var timer = new Timer();
    var task = new TimerTask() {
      @Override
      public void run() {
        synchronized (MyCountDownLatch.this) {
          MyCountDownLatch.this.notifyAll();
        }
      }
    };

    var startTime = System.currentTimeMillis();
    var delay = unit.toMillis(timeout);
    timer.schedule(task, delay);

    while (System.currentTimeMillis() < startTime + delay && count > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    return System.currentTimeMillis() < startTime + delay;
  }
}
