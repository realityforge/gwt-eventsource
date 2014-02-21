package org.realityforge.gwt.eventsource.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementation of EventSource based on the Html5 standard.
 */
final class Html5EventSource
  extends EventSource
{
  public static native boolean isSupported() /*-{
    return !!window.EventSource;
  }-*/;

  @Nullable
  private EventSourceImpl _eventSource;
  @Nullable
  private HashSet<String> _subscriptions;

  static class Factory
    implements EventSource.Factory
  {
    @Override
    public EventSource newEventSource()
    {
      return new Html5EventSource( new SimpleEventBus() );
    }
  }

  Html5EventSource( final EventBus eventBus )
  {
    super( eventBus );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close()
  {
    ensureEventSource();
    doClose();
  }

  private void doClose()
  {
    assert null != _eventSource;
    _eventSource.close();
    _eventSource = null;
    _subscriptions = null;
    onClose();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void open( @Nonnull final String url, final boolean withCredentials )
  {
    if ( null != _eventSource )
    {
      throw new IllegalStateException( "EventSource already opened" );
    }
    _eventSource = EventSourceImpl.create( this, url, withCredentials );
    _subscriptions = new HashSet<String>();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final ReadyState getReadyState()
  {
    if ( null == _eventSource )
    {
      return ReadyState.CLOSED;
    }
    else
    {
      return ReadyState.values()[ _eventSource.getReadyState() ];
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean subscribeTo( @Nonnull final String messageType )
    throws IllegalStateException
  {
    final HashSet<String> subscriptions = ensureSubscriptions();
    if ( subscriptions.contains( messageType ) )
    {
      return false;
    }
    else
    {
      subscriptions.add( messageType );
      ensureEventSource().subscribe( messageType );
      return true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean unsubscribeFrom( @Nonnull final String messageType )
    throws IllegalStateException
  {
    if ( ensureSubscriptions().remove( messageType ) )
    {
      ensureEventSource().unsubscribe( messageType );
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String getURL()
  {
    return ensureEventSource().getURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean getWithCredentials()
  {
    return ensureEventSource().getWithCredentials();
  }

  /**
   * @return the underlying implementation.
   * @throws IllegalStateException if no implementation is available as connection is not open.
   */
  private EventSourceImpl ensureEventSource()
  {
    if ( null == _eventSource )
    {
      throw new IllegalStateException( "EventSource not open" );
    }
    return _eventSource;
  }

  /**
   * @return the set of event subscriptions.
   * @throws IllegalStateException if no implementation is available as connection is not open.
   */
  @Nonnull
  protected final HashSet<String> ensureSubscriptions()
  {
    if ( null == _subscriptions )
    {
      throw new IllegalStateException( "EventSource not open" );
    }
    return _subscriptions;
  }

  private final static class EventSourceImpl
    extends JavaScriptObject
  {
    static native EventSourceImpl create( EventSource client, String url, boolean withCredentials ) /*-{
      var eventSource = new EventSource( url, {withCredentials: withCredentials} );
      eventSource.onopen = $entry( function ()
                                   {
                                     client.@org.realityforge.gwt.eventsource.client.EventSource::onOpen()();
                                   } );
      eventSource.onerror = $entry( function ()
                                    {
                                      if ( eventSource.readyState == EventSource.CLOSED )
                                      {
                                        // Connection was closed.
                                        client.@org.realityforge.gwt.eventsource.client.Html5EventSource::doClose()();
                                      }
                                      else
                                      {
                                        client.@org.realityforge.gwt.eventsource.client.EventSource::onError()();
                                      }
                                    } );
      eventSource.onmessage = $entry( function ( response )
                                      {
                                        client.@org.realityforge.gwt.eventsource.client.EventSource::onMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)( response.lastEventId,
                                                                                                                                                                        response.type,
                                                                                                                                                                        response.data );
                                      } );
      return eventSource;
    }-*/;

    protected EventSourceImpl()
    {
    }

    native String getURL() /*-{
      return this.url;
    }-*/;

    native boolean getWithCredentials() /*-{
      return this.withCredentials;
    }-*/;

    native int getReadyState() /*-{
      return this.readyState;
    }-*/;

    native void close() /*-{
      this.close();
    }-*/;

    native void subscribe( String messageType ) /*-{
      this.addEventListener( messageType, this.onmessage );
    }-*/;

    native void unsubscribe( String messageType ) /*-{
      this.removeEventListener( messageType, this.onmessage );
    }-*/;
  }
}
