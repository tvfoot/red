package io.oldering.tvfoot.red.event;

public class Event<T> {
  public Status status;
  private T data;
  private Throwable error;
}
