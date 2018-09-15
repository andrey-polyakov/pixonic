package com.company;

import com.company.core.SchedulingExecutor;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SchedulingExecutorTest {

    @Test
    public void basicTest() throws InterruptedException {
        int count = 5;
        CountDownLatch l = new CountDownLatch(count);
        SchedulingExecutor systemUnderTest = new SchedulingExecutor();
        for (int ii = 0; ii < count; ii++) {
            systemUnderTest.submit(LocalDateTime.now().plusSeconds(1).plusNanos(555), () -> {
                l.countDown();
                return null;
            });
        }
        l.await(2, TimeUnit.SECONDS);
        systemUnderTest.terminate();
    }

    @Test
    public void singleWorkerTest() throws InterruptedException {
        int count = 5;
        CountDownLatch l = new CountDownLatch(count);
        SchedulingExecutor systemUnderTest = new SchedulingExecutor(1);
        for (int ii = 0; ii < count; ii++) {
            systemUnderTest.submit(LocalDateTime.now(), () -> {
                l.countDown();
                return null;
            });
        }
        l.await(2, TimeUnit.SECONDS);
    }

    @Test
    public void shouldNotSleepTest() throws InterruptedException {
        int count = 5;
        CountDownLatch l = new CountDownLatch(count);
        SchedulingExecutor systemUnderTest = new SchedulingExecutor(1);
        for (int ii = 0; ii < count; ii++) {
            systemUnderTest.submit(LocalDateTime.now().minusMinutes(1), () -> {
                l.countDown();
                return null;
            });
        }
        l.await(1, TimeUnit.SECONDS);
    }

    @Test
    public void yieldLoopTest() throws InterruptedException {
        LocalDateTime stubNow = LocalDateTime.now();
        CountDownLatch l = new CountDownLatch(1);
        SchedulingExecutor systemUnderTest = new SchedulingExecutor(1) {
            @Override
            public LocalDateTime now() {
                return stubNow.minusNanos(400);
            }
        };
        systemUnderTest.submit(LocalDateTime.now(), () -> {
            l.countDown();
            return null;
        });
        l.await(2, TimeUnit.SECONDS);
    }

}
