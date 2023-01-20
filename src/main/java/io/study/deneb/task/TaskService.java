package io.study.deneb.task;

import java.util.concurrent.TimeUnit;

public class TaskService {

  public String proceed(Task task) {
    //System.out.println(task + " started");
    try {
      delay();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    //System.out.println(task + " end");
    return "success";
  }

  public void delay() throws InterruptedException {
    TimeUnit.SECONDS.sleep(10);
    System.out.printf("current thread[%s] : %s\n",
      Thread.currentThread().getId(), Thread.currentThread().getName());
  }

}
