package org.realityforge.gwt.eventsource.client.event;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.gwt.eventsource.client.EventSource;
import org.realityforge.gwt.eventsource.client.EventSourceListener;

/**
 * An EventSourceListener implementation that fires events.
 */
public class EventBasedEventSourceListener
  implements EventSourceListener
{
  private final EventBus _eventBus;

  /**
   * Construct a new listener creating a new EventBus to use.
   */
  public EventBasedEventSourceListener()
  {
    this( new SimpleEventBus() );
  }

  /**
   * Construct a new listener.
   *
   * @param eventBus the EventBus to use.
   */
  public EventBasedEventSourceListener( @Nonnull final EventBus eventBus )
  {
    _eventBus = eventBus;
  }

  /**
   * Add listener for open events.
   *
   * @param handler the event handler.
   * @return the HandlerRegistration that manages the listener.
   */
  @Nonnull
  public final HandlerRegistration addOpenHandler( @Nonnull final OpenEvent.Handler handler )
  {
    return _eventBus.addHandler( OpenEvent.getType(), handler );
  }

  /**
   * Add listener for close events.
   *
   * @param handler the event handler.
   * @return the HandlerRegistration that manages the listener.
   */
  @Nonnull
  public final HandlerRegistration addCloseHandler( @Nonnull final CloseEvent.Handler handler )
  {
    return _eventBus.addHandler( CloseEvent.getType(), handler );
  }

  /**
   * Add listener for message events.
   *
   * @param handler the event handler.
   * @return the HandlerRegistration that manages the listener.
   */
  @Nonnull
  public final HandlerRegistration addMessageHandler( @Nonnull final MessageEvent.Handler handler )
  {
    return _eventBus.addHandler( MessageEvent.getType(), handler );
  }

  /**
   * Add listener for error events.
   *
   * @param handler the event handler.
   * @return the HandlerRegistration that manages the listener.
   */
  @Nonnull
  public final HandlerRegistration addErrorHandler( @Nonnull final ErrorEvent.Handler handler )
  {
    return _eventBus.addHandler( ErrorEvent.getType(), handler );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onOpen( @Nonnull final EventSource eventSource )
  {
    _eventBus.fireEventFromSource( new OpenEvent( eventSource ), eventSource );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onClose( @Nonnull final EventSource eventSource )
  {
    _eventBus.fireEventFromSource( new CloseEvent( eventSource ), eventSource );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onMessage( @Nonnull final EventSource eventSource,
                               @Nullable final String lastEventId,
                               @Nonnull final String type,
                               @Nonnull final String data )
  {
    _eventBus.fireEventFromSource( new MessageEvent( eventSource, lastEventId, type, data ), eventSource );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onError( @Nonnull final EventSource eventSource )
  {
    _eventBus.fireEventFromSource( new ErrorEvent( eventSource ), eventSource );
  }
}
