package org.realityforge.gwt.eventsource.client;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EventSourceTest
{
  @Test
  public void registryTest()
  {
    assertNull( EventSource.newEventSourceIfSupported() );
    assertFalse( EventSource.isSupported() );
    final TestEventSource.Factory factory = new TestEventSource.Factory();
    EventSource.register( factory );
    assertTrue( EventSource.isSupported() );
    assertNotNull( EventSource.newEventSourceIfSupported() );
    assertTrue( EventSource.deregister( factory ) );
    assertFalse( EventSource.isSupported() );
    assertNull( EventSource.newEventSourceIfSupported() );
    assertFalse( EventSource.deregister( factory ) );
  }
}
