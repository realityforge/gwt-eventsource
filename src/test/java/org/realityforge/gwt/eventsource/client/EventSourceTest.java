package org.realityforge.gwt.eventsource.client;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import org.mockito.Mockito;
import org.realityforge.gwt.eventsource.client.event.CloseEvent;
import org.realityforge.gwt.eventsource.client.event.ErrorEvent;
import org.realityforge.gwt.eventsource.client.event.MessageEvent;
import org.realityforge.gwt.eventsource.client.event.OpenEvent;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class EventSourceTest
{
  @Test
  public void registryTest()
  {
    assertNull( EventSource.newEventSourceIfSupported() );
    assertFalse( EventSource.isSupported() );
    final TestEventSource.Factory factory = new TestEventSource.Factory();
    EventSource.register( factory );
    assertTrue( EventSource.isSupported() );
    assertNotNull( EventSource.newEventSourceIfSupported() );
    assertTrue( EventSource.deregister( factory ) );
    assertFalse( EventSource.isSupported() );
    assertNull( EventSource.newEventSourceIfSupported() );
    assertFalse( EventSource.deregister( factory ) );
  }

  @Test
  public void handlerInteractions()
  {
    final TestEventSource webSocket = new TestEventSource( new SimpleEventBus() );

    {
      final OpenEvent.Handler handler = mock( OpenEvent.Handler.class );
      final HandlerRegistration registration = webSocket.addOpenHandler( handler );
      webSocket.onOpen();
      verify( handler, only() ).onOpenEvent( Mockito.<OpenEvent>anyObject() );
      registration.removeHandler();
      webSocket.onOpen();
      verify( handler, atMost( 1 ) ).onOpenEvent( Mockito.<OpenEvent>anyObject() );
    }

    {
      final CloseEvent.Handler handler = mock( CloseEvent.Handler.class );
      final HandlerRegistration registration = webSocket.addCloseHandler( handler );
      webSocket.onClose();
      final CloseEvent expected = new CloseEvent( webSocket );
      verify( handler, only() ).onCloseEvent( Mockito.<CloseEvent>refEq( expected, "source" ) );
      registration.removeHandler();
      webSocket.onClose();
      verify( handler, atMost( 1 ) ).onCloseEvent( Mockito.<CloseEvent>anyObject() );
    }

    {
      final MessageEvent.Handler handler = mock( MessageEvent.Handler.class );
      final HandlerRegistration registration = webSocket.addMessageHandler( handler );
      webSocket.onMessage( "0", "time", "Blah" );
      final MessageEvent expected = new MessageEvent( webSocket, "0", "time", "Blah" );
      verify( handler, only() ).onMessageEvent( Mockito.<MessageEvent>refEq( expected, "source" ) );
      registration.removeHandler();
      webSocket.onMessage( "0", "time", "Blah" );
      verify( handler, atMost( 1 ) ).onMessageEvent( Mockito.<MessageEvent>anyObject() );
    }

    {
      final ErrorEvent.Handler handler = mock( ErrorEvent.Handler.class );
      final HandlerRegistration registration = webSocket.addErrorHandler( handler );
      webSocket.onError();
      verify( handler, only() ).onErrorEvent( Mockito.<ErrorEvent>anyObject() );
      registration.removeHandler();
      webSocket.onError();
      verify( handler, atMost( 1 ) ).onErrorEvent( Mockito.<ErrorEvent>anyObject() );
    }
  }
}
