package org.realityforge.gwt.eventsource.client;

final class NullEventSourceListener
  extends EventSourceListenerAdapter
{
  static final NullEventSourceListener INSTANCE = new NullEventSourceListener();

  private NullEventSourceListener()
  {
  }
}
