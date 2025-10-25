void main() throws InterruptedException {
  var latch = new MyCountDownLatch(1);

  new Thread(() -> {
    latch.await();
    IO.println("I'm ready now!");
  }).start();

  new Thread(() -> {
    latch.await();
    IO.println("Me too!");
  }).start();

  Thread.sleep(2000);
  latch.countDown();
}
