package org.realityforge.gwt.eventsource.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.realityforge.gwt.eventsource.client.TestEventSource;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

public class EventBasedEventSourceListenerTest
{
  @Test
  public void handlerInteractions()
  {
    final EventBasedEventSourceListener listener = new EventBasedEventSourceListener( new SimpleEventBus() );
    final TestEventSource eventSource = new TestEventSource();

    {
      final OpenEvent.Handler handler = mock( OpenEvent.Handler.class );
      final HandlerRegistration registration = listener.addOpenHandler( handler );
      listener.onOpen( eventSource );
      verify( handler, only() ).onOpenEvent( any( OpenEvent.class ) );
      registration.removeHandler();
      listener.onOpen( eventSource );
      verify( handler, atMost( 1 ) ).onOpenEvent( any( OpenEvent.class ) );
    }

    {
      final CloseEvent.Handler handler = mock( CloseEvent.Handler.class );
      final HandlerRegistration registration = listener.addCloseHandler( handler );
      listener.onClose( eventSource );
      final CloseEvent expected = new CloseEvent( eventSource );
      verify( handler, only() ).onCloseEvent( refEq( expected, "source" ) );
      registration.removeHandler();
      listener.onClose( eventSource );
      verify( handler, atMost( 1 ) ).onCloseEvent( any( CloseEvent.class ) );
    }

    {
      final MessageEvent.Handler handler = mock( MessageEvent.Handler.class );
      final HandlerRegistration registration = listener.addMessageHandler( handler );
      listener.onMessage( eventSource, "0", "time", "Blah" );
      final MessageEvent expected = new MessageEvent( eventSource, "0", "time", "Blah" );
      verify( handler, only() ).onMessageEvent( refEq( expected, "source" ) );
      registration.removeHandler();
      listener.onMessage( eventSource, "0", "time", "Blah" );
      verify( handler, atMost( 1 ) ).onMessageEvent(  any( MessageEvent.class )  );
    }

    {
      final ErrorEvent.Handler handler = mock( ErrorEvent.Handler.class );
      final HandlerRegistration registration = listener.addErrorHandler( handler );
      listener.onError( eventSource );
      verify( handler, only() ).onErrorEvent( refEq( new ErrorEvent( eventSource ), "source" ) );
      registration.removeHandler();
      listener.onError( eventSource );
      verify( handler, atMost( 1 ) ).onErrorEvent( any( ErrorEvent.class ) );
    }
  }
}
