/*
 * Copyright (c) 2016 Savoir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

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
        logger.info().field("thread", Thread.currentThread().getName()).message("Message #: " + i).log();
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
