import java.util.concurrent.TimeUnit;

public class Main {
  void main() throws InterruptedException {
    var latch = new MyCountDownLatch2(1);

    new Thread(
            () -> {
              boolean isCompletedNormally = false;
              try {
                isCompletedNormally = latch.await(1, TimeUnit.SECONDS);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              IO.println("I'm ready now! " + isCompletedNormally);
            })
        .start();

    new Thread(
            () -> {
              boolean isCompletedNormally = false;
              try {
                isCompletedNormally = latch.await(1, TimeUnit.MINUTES);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              IO.println("Me too! " + isCompletedNormally);
            })
        .start();

    Thread.sleep(3000);
    latch.countDown();
  }
}
