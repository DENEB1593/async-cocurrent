package io.study.deneb;

import io.study.deneb.task.Task;
import io.study.deneb.task.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
//sync time: 5021 ms
//async time: 1014 ms

public class Main {

  private static TaskService taskService = new TaskService();

  public static void main(String[] args) {
    var taskList = new ArrayList<Task>();
    taskList.add(new Task("task1", "task1 content"));
    taskList.add(new Task("task2", "task2 content"));
    taskList.add(new Task("task3", "task3 content"));
    taskList.add(new Task("task4", "task4 content"));
    taskList.add(new Task("task5", "task5 content"));

    long start = System.currentTimeMillis();
    // 동기(sync) 방식: 4초 소요
    proceedWithSync(taskList);
    long end = System.currentTimeMillis();
    System.out.printf("sync time: %s ms%n", end - start);

    start = System.currentTimeMillis();
    // 동기(sync) 방식: 4초 소요
    proceedWithAsync(taskList);
    end = System.currentTimeMillis();
    System.out.printf("async time: %s ms%n", end - start);

  }

  public static List<String> proceedWithSync(List<Task> taskList) {
    return taskList.stream()
      .map(taskService::proceed)
      .collect(toList());
  }

  public static List<String> proceedWithAsync(List<Task> taskList) {
    List<CompletableFuture<String>> asyncFuture = taskList.stream()
      .map(task -> CompletableFuture.supplyAsync(() -> taskService.proceed(task)))
      .collect(toList());

    return asyncFuture.stream()
      .map(CompletableFuture::join)
      .collect(toList());

  }
}
