void main() throws InterruptedException {
  var latch = new MyCountDownLatch(1);

  new Thread(() -> {
    var isCompletedNormally = latch.await(1, TimeUnit.SECONDS);
    IO.println("I'm ready now! " + isCompletedNormally);
  }).start();

  new Thread(() -> {
    var isCompletedNormally = latch.await(1, TimeUnit.MINUTES);
    IO.println("Me too! " + isCompletedNormally);
  }).start();

  Thread.sleep(3000);
  latch.countDown();
}
