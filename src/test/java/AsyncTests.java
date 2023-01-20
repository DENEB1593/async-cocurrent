import io.study.deneb.task.Task;
import io.study.deneb.task.TaskService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

class AsyncTests {

  private static TaskService taskService = new TaskService();

  @Test
  void fixedTasksTest() {
    var taskList = create100Tasks();
    var executor = Executors.newFixedThreadPool(taskList.size());

    var futures = taskList.stream()
      .map(task ->
        CompletableFuture.supplyAsync(() ->
          taskService.proceed(task), executor))
      .toList();

    var results = futures.stream()
      .map(CompletableFuture::join)
      .toList();
  }

  @Test
  void fixedPoolWithHalfTasksTest() {
    var taskList = create100Tasks();
    var executor = Executors.newFixedThreadPool(taskList.size() / 2 + 1);

    var futures = taskList.stream()
      .map(task ->
        CompletableFuture.supplyAsync(() ->
          taskService.proceed(task), executor))
      .toList();

    var results = futures.stream()
      .map(CompletableFuture::join)
      .toList();
  }

  static List<Task> create100Tasks() {
    var taskList = new ArrayList<Task>();
    for (int i = 1; i <= 100; i++) {
      taskList.add(
        new Task("task" + i,  "task" + i + " content"));
    }
    return taskList;

  }
}
