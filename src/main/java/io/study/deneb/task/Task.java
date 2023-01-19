package io.study.deneb.task;

public class Task {
  private final String name;
  private final String content;

  public Task(String name, String content) {
    this.name = name;
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "Task{" +
      "name='" + name + '\'' +
      ", content='" + content + '\'' +
      '}';
  }
}
