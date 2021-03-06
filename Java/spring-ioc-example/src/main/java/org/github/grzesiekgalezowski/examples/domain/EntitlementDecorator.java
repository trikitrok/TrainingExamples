package org.github.grzesiekgalezowski.examples.domain;

import org.github.grzesiekgalezowski.examples.domain.BusinessEntitlement;
import org.github.grzesiekgalezowski.examples.domain.Entitlement;

public class EntitlementDecorator implements Entitlement {
  public EntitlementDecorator(final Entitlement e, final String str) {
    System.out.println(this.getClass() + ": ");
    System.out.println("-> " + e.getClass());
    System.out.println("-> " + str);

  }
}
