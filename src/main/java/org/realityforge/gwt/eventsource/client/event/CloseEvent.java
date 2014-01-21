package org.realityforge.gwt.eventsource.client.event;

import com.google.gwt.event.shared.EventHandler;
import javax.annotation.Nonnull;
import org.realityforge.gwt.eventsource.client.EventSource;
import org.realityforge.gwt.eventsource.client.event.CloseEvent.Handler;

/**
 * Event fired when the web socket is closed.
 */
public class CloseEvent
  extends EventSourceEvent<Handler>
{
  public interface Handler
    extends EventHandler
  {
    void onCloseEvent( @Nonnull CloseEvent event );
  }

  private static final Type<Handler> TYPE = new Type<Handler>();

  public static Type<Handler> getType()
  {
    return TYPE;
  }

  public CloseEvent( @Nonnull final EventSource eventSource )
  {
    super( eventSource );
  }

  @Override
  public Type<Handler> getAssociatedType()
  {
    return CloseEvent.getType();
  }

  @Override
  protected void dispatch( @Nonnull final Handler handler )
  {
    handler.onCloseEvent( this );
  }
}
