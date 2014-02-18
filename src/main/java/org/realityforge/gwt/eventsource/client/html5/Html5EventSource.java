package org.realityforge.gwt.eventsource.client.html5;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.gwt.eventsource.client.EventSource;

public class Html5EventSource
  extends EventSource
{
  public static native boolean isSupported() /*-{
    return !!window.EventSource;
  }-*/;

  @Nullable
  private EventSourceImpl _eventSource;
  @Nullable
  private HashSet<String> _subscriptions;

  public static class Factory
    implements EventSource.Factory
  {
    @Override
    public EventSource newEventSource()
    {
      return new Html5EventSource( new SimpleEventBus() );
    }
  }

  public Html5EventSource( final EventBus eventBus )
  {
    super( eventBus );
  }

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

  @Nonnull
  @Override
  public final String getURL()
  {
    return ensureEventSource().getURL();
  }

  @Override
  public final boolean getWithCredentials()
  {
    return ensureEventSource().getWithCredentials();
  }

  private EventSourceImpl ensureEventSource()
  {
    if ( null == _eventSource )
    {
      throw new IllegalStateException( "EventSource not open" );
    }
    return _eventSource;
  }

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
                                        client.@org.realityforge.gwt.eventsource.client.html5.Html5EventSource::doClose()();
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
