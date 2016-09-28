﻿namespace StateWithImplicitContextPassing.Interfaces
{
  public interface LightSwitchContext
  {
    void MoveTo(LightSwitchState nextState);
    void RegisterSwitchOn();
  }
}