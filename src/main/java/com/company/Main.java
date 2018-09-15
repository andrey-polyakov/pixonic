package com.company;

import com.company.core.SchedulingExecutor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.MILLIS;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
	// write your code here
        SchedulingExecutor e = new SchedulingExecutor();

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            for (int j = 0; j < 50; j++) {

                e.submit(LocalDateTime.now().plusSeconds(i), new Callable() {
                    @Override
                    public Object call() throws Exception {
                        System.out.println("boom" + finalI);
                        return null;
                    }
                });
            }
        }
        Thread.sleep(9001);
        for (int i = 0; i < 200; i++) {
            e.submit(LocalDateTime.now().plus(14, MILLIS).minus(810 + i, MICROS), new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("big boom");
                    return null;
                }
            });
        }
        System.in.read();
    }
}
