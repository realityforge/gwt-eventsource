package org.realityforge.gwt.eventsource.client.html5;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.gwt.eventsource.client.EventSource;

public class Html5EventSource
  extends EventSource
{
  public static native boolean isSupported() /*-{
    return !!window.EventSource;
  }-*/;

  private EventSourceImpl _eventSource;

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
    checkConnected();
    doClose();
  }

  private void doClose()
  {
    _eventSource.close();
    _eventSource = null;
    onClose();
  }

  @Override
  public void open( @Nonnull final String url, final boolean withCredentials )
  {
    if ( null != _eventSource )
    {
      throw new IllegalStateException( "EventSource already connected" );
    }
    _eventSource = EventSourceImpl.create( this, url, withCredentials );
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

  @Nonnull
  @Override
  public final String getURL()
  {
    checkConnected();
    return _eventSource.getURL();
  }

  @Override
  public final boolean getWithCredentials()
  {
    checkConnected();
    return _eventSource.getWithCredentials();
  }

  private void checkConnected()
  {
    if ( null == _eventSource )
    {
      throw new IllegalStateException( "EventSource not open" );
    }
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
                               client.@org.realityforge.gwt.eventsource.client.EventSource::onMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)( response.lastEventId, response.type, response.data );
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
  }
}
