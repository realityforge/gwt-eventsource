package org.realityforge.gwt.eventsource.client.event;

import com.google.gwt.event.shared.EventHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.gwt.eventsource.client.EventSource;
import org.realityforge.gwt.eventsource.client.event.MessageEvent.Handler;

/**
 * Event fired when a message arrives on EventSource.
 */
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

  @Nullable
  private final String _lastEventId;
  @Nonnull
  private final String _messageType;
  @Nonnull
  private final String _data;

  public MessageEvent( @Nonnull final EventSource eventSource,
                       @Nullable final String lastEventId,
                       @Nonnull final String messageType,
                       @Nonnull final String data )
  {
    super( eventSource );
    _lastEventId = lastEventId;
    _messageType = messageType;
    _data = data;
  }

  @Nullable
  public String getLastEventId()
  {
    return _lastEventId;
  }

  @Nonnull
  public String getMessageType()
  {
    return _messageType;
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
