package org.realityforge.gwt.eventsource.client.event;

import com.google.gwt.event.shared.EventHandler;
import javax.annotation.Nonnull;
import org.realityforge.gwt.eventsource.client.EventSource;
import org.realityforge.gwt.eventsource.client.event.MessageEvent.Handler;

public class MessageEvent
  extends EventSourceEvent<Handler>
{
  public interface Handler
    extends EventHandler
  {
    void onMessageEvent( @Nonnull MessageEvent event );
  }

  private static final Type<Handler> TYPE = new Type<Handler>();

  public static Type<Handler> getType()
  {
    return TYPE;
  }

  private final String _data;

  public MessageEvent( @Nonnull final EventSource eventSource, @Nonnull final String data )
  {
    super( eventSource );
    _data = data;
  }

  @Nonnull
  public String getData()
  {
    return _data;
  }

  @Override
  public Type<Handler> getAssociatedType()
  {
    return MessageEvent.getType();
  }

  @Override
  protected void dispatch( @Nonnull final Handler handler )
  {
    handler.onMessageEvent( this );
  }
}
