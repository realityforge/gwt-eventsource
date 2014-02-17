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
  public void open( @Nonnull final String url, final boolean withCredentials )
  {
  }

  @Override
  public boolean subscribeTo( @Nonnull final String messageType )
    throws IllegalStateException
  {
    return false;
  }

  @Override
  public boolean unsubscribeFrom( @Nonnull final String messageType )
    throws IllegalStateException
  {
    return false;
  }

  @Override
  @Nonnull
  public String getURL()
  {
    return "";
  }

  @Override
  public boolean getWithCredentials()
  {
    return false;
  }

  @Override
  public void close()
  {
  }

  @Nonnull
  @Override
  public ReadyState getReadyState()
  {
    return ReadyState.CLOSED;
  }
}
