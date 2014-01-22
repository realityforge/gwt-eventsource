package org.realityforge.gwt.eventsource.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import javax.annotation.Nonnull;
import org.realityforge.gwt.eventsource.client.EventSource;
import org.realityforge.gwt.eventsource.client.event.OpenEvent.Handler;

/**
 * Event fired when web socket successfully connects.
 */
public class OpenEvent
  extends EventSourceEvent<Handler>
{
  public interface Handler
    extends EventHandler
  {
    void onOpenEvent( @Nonnull OpenEvent event );
  }

  private static final GwtEvent.Type<Handler> TYPE = new Type<>();

  public static GwtEvent.Type<Handler> getType()
  {
    return TYPE;
  }

  public OpenEvent( @Nonnull final EventSource eventSource )
  {
    super( eventSource );
  }

  @Override
  public GwtEvent.Type<Handler> getAssociatedType()
  {
    return OpenEvent.getType();
  }

  @Override
  protected void dispatch( @Nonnull final Handler handler )
  {
    handler.onOpenEvent( this );
  }
}
