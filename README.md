### 구성
    - 작업(Task)를 5개 수행한다.
    - 하나의 작업은 1초의 수행 시간을 가진다.
    - 순차, 병렬 처리 간 수행 시간의 차이를 파악한다.
### 작업생성
    var taskList = new ArrayList<Task>();
    taskList.add(new Task("task1", "task1 content"));
    taskList.add(new Task("task2", "task2 content"));
    taskList.add(new Task("task3", "task3 content"));
    taskList.add(new Task("task4", "task4 content"));
    taskList.add(new Task("task5", "task5 content"));
Task 객체 5개를 생성한다.

### 동기처리
      taskList.stream()
      .map(taskService::proceed)
      .collect(toList());
작업목록(taskList)을 생성하여 작업처리를 진행한다.
 
### 비동기처리
    List<CompletableFuture<String>> asyncFuture = taskList.stream()
      .map(task -> CompletableFuture.supplyAsync(() -> taskService.proceed(task)))
      .collect(toList());

    return asyncFuture.stream()
      .map(CompletableFuture::join)
      .collect(toList());

작업을 비동기 처리 후 완료가 될때 까지 blocking 후 작업 내역을 반환한다.<br>
join()의 경우 생성된 Thread들의 작업 처리 때 까지 대기한다.


### ThreadPool
쓰레드 풀의 경우 기본(ForkJoinPool)을 사용하였다.<br>
ForkJoinPool은 작업양에 따라 Thread의 생성을 동적으로 조정할 수 있어 유연한 풀 생성이 가능하다.<br>
하지만, Thread 생성 제어 통제권을 개발자가 제어하는 경우 무제한 Thread 생성에 대한 상황을 방지할 수 있어
시스템 안정성에 기여할 수 있다<br>
예를들어 동시에 사용되는 작업의 개수가 명확히 정해진 상황이라면 아래의 옵션도 고려해볼만 한다.<br>

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
100개의 일련 작업이 고정되어 있는 경우 100개의 Thread을 고정적으로 가지는 풀을 생성한다.
해당 테스트를 위해 delay를 10초로 부여하고 실행결과 10.29s가 소요됨을 확인할 수 있었다.
<br><br>
위 방법의 경우 잘못된 작업양을 예측하여 아래의 처럼 pool size를 실작업에 비해 작게 설정하는 경우

    var taskList = create100Tasks();
    var executor = Executors.newFixedThreadPool(taskList.size() / 2 + 1);
    // 100개의 작업 동시처리이나.. 어떠한 이유로 51개의 pool을 설정하였다.

결과적으로 51개의 작업을 동시 처리 후 나머지 49개의 작업처리로
2단계에 처리가 완료되었다. (소요시간 20.46s)

