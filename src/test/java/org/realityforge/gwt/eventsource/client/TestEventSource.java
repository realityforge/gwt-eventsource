package org.realityforge.gwt.eventsource.client;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import javax.annotation.Nonnull;

final class TestEventSource
  extends EventSource
{
  static class Factory
    implements EventSource.Factory
  {
    @Override
    public EventSource newEventSource()
    {
      return new TestEventSource( new SimpleEventBus() );
    }
  }

  TestEventSource( final EventBus eventBus )
  {
    super( eventBus );
  }

  @Override
  public void connect( @Nonnull final String server )
  {
  }

  @Override
  public void close()
  {
  }

  @Override
  public ReadyState getReadyState()
  {
    return null;
  }
}
