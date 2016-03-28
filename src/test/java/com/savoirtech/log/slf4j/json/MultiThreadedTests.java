package com.savoirtech.log.slf4j.json;

import com.savoirtech.log.slf4j.json.logger.Logger;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * For micro-benchmarking
 */
public class MultiThreadedTests {
  int numThreads = 8;
  int iterationsPerThread = 10000;

  @Ignore
  @Test
  public void jsonMultiThreadedTest() throws InterruptedException {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    List<Thread> threads = new ArrayList<>();

    for (int threadNumber = 0; threadNumber < numThreads; threadNumber++) {
      Thread thread = new Thread(new jsonLogRunnable(logger));
      threads.add(thread);
    }

    for (Thread thread : threads) {
      thread.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }
  }

  private class jsonLogRunnable implements Runnable {
    private Logger logger;

    public jsonLogRunnable(Logger logger) {
      this.logger = logger;
    }

    @Override
    public void run() {
      for (int i = 0; i < iterationsPerThread; i++) {
        logger.info().category(Thread.currentThread().getName()).message("Message #: " + i).log();
      }
    }
  }

  @Ignore
  @Test
  public void slf4jMultiThreadedTest() throws InterruptedException {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    List<Thread> threads = new ArrayList<>();

    for (int threadNumber = 0; threadNumber < numThreads; threadNumber++) {
      Thread thread = new Thread(new slf4jLogRunnable(logger));
      threads.add(thread);
    }

    for (Thread thread : threads) {
      thread.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }
  }

  private class slf4jLogRunnable implements Runnable {
    private org.slf4j.Logger logger;

    public slf4jLogRunnable(org.slf4j.Logger logger) {
      this.logger = logger;
    }

    @Override
    public void run() {
      for (int i = 0; i < iterationsPerThread; i++) {
        logger.info(Thread.currentThread().getName() + ", Message #: " + i);
      }
    }
  }
}
