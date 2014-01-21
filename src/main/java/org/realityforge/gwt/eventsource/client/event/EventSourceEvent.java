package org.realityforge.gwt.eventsource.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import javax.annotation.Nonnull;
import org.realityforge.gwt.eventsource.client.EventSource;

/**
 * Base class of all events originating from EventSource.
 */
public abstract class EventSourceEvent<H extends EventHandler>
  extends GwtEvent<H>
{
  private final EventSource _eventSource;

  protected EventSourceEvent( @Nonnull final EventSource eventSource )
  {
    _eventSource = eventSource;
  }

  @Nonnull
  public final EventSource getEventSource()
  {
    return _eventSource;
  }
}
