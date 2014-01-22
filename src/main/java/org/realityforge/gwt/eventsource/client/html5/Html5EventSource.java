package org.realityforge.gwt.eventsource.client.html5;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import javax.annotation.Nonnull;
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
  public void connect( @Nonnull final String url )
  {
    if ( null != _eventSource )
    {
      throw new IllegalStateException( "EventSource already connected" );
    }
    _eventSource = EventSourceImpl.create( this, url );
  }

  @Override
  public final ReadyState getReadyState()
  {
    checkConnected();
    return ReadyState.values()[ _eventSource.getReadyState() ];
  }

  private void checkConnected()
  {
    if ( null == _eventSource )
    {
      throw new IllegalStateException( "EventSource not connected" );
    }
  }

  private final static class EventSourceImpl
    extends JavaScriptObject
  {
    static native EventSourceImpl create( EventSource client, String url )
    /*-{
      var eventSource = new EventSource( url );
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
                               client.@org.realityforge.gwt.eventsource.client.EventSource::onMessage(Ljava/lang/String;)( response.data );
                             } );
      return eventSource;
    }-*/;

    protected EventSourceImpl()
    {
    }

    native int getReadyState() /*-{
      return this.readyState;
    }-*/;

    native void close() /*-{
      this.close();
    }-*/;
  }
}
