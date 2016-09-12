﻿namespace Command.Commands
{
  //can be a builder
  public class ConcreteResult<T> : Result<T>
  {
    public T Value { get; set; }
  }
}