package org.realityforge.gwt.eventsource.client;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import javax.annotation.Nonnull;
import org.realityforge.gwt.eventsource.client.event.CloseEvent;
import org.realityforge.gwt.eventsource.client.event.ErrorEvent;
import org.realityforge.gwt.eventsource.client.event.MessageEvent;
import org.realityforge.gwt.eventsource.client.event.OpenEvent;
import org.realityforge.gwt.eventsource.client.html5.Html5EventSource;

public abstract class EventSource
{
  public interface Factory
  {
    EventSource newEventSource();
  }

  // ready state
  public enum ReadyState
  {
    CONNECTING, OPEN, CLOSED
  }

  private static SupportDetector g_supportDetector;
  private static Factory g_factory;
  private final EventBus _eventBus;

  public static EventSource newEventSourceIfSupported()
  {
    if ( null == g_factory && isSupported() )
    {
      register( getSupportDetector().newFactory() );
      return g_factory.newEventSource();
    }
    return ( null != g_factory ) ? g_factory.newEventSource() : null;
  }

  /**
   * @return true if newEventSourceIfSupported() will return a non-null value, false otherwise.
   */
  public static boolean isSupported()
  {
    return ( null != g_factory ) || GWT.isClient() && getSupportDetector().isSupported();
  }

  public static void register( @Nonnull final Factory factory )
  {
    g_factory = factory;
  }

  public static boolean deregister( @Nonnull final Factory factory )
  {
    if ( g_factory != factory )
    {
      return false;
    }
    else
    {
      g_factory = null;
      return true;
    }
  }

  public EventSource( final EventBus eventBus )
  {
    _eventBus = eventBus;
  }

  public final void open( @Nonnull final String url )
    throws IllegalStateException
  {
    open( url, false );
  }

  public abstract void open( @Nonnull String url, boolean withCredentials )
    throws IllegalStateException;

  public abstract void close()
    throws IllegalStateException;

  @Nonnull
  public abstract String getURL()
    throws IllegalStateException;

  public abstract boolean getWithCredentials()
    throws IllegalStateException;

  @Nonnull
  public abstract ReadyState getReadyState();

  @Nonnull
  public final HandlerRegistration addOpenHandler( @Nonnull OpenEvent.Handler handler )
  {
    return _eventBus.addHandler( OpenEvent.getType(), handler );
  }

  @Nonnull
  public final HandlerRegistration addCloseHandler( @Nonnull CloseEvent.Handler handler )
  {
    return _eventBus.addHandler( CloseEvent.getType(), handler );
  }

  @Nonnull
  public final HandlerRegistration addMessageHandler( @Nonnull MessageEvent.Handler handler )
  {
    return _eventBus.addHandler( MessageEvent.getType(), handler );
  }

  @Nonnull
  public final HandlerRegistration addErrorHandler( @Nonnull ErrorEvent.Handler handler )
  {
    return _eventBus.addHandler( ErrorEvent.getType(), handler );
  }

  /**
   * Fire a Connected event.
   */
  protected final void onOpen()
  {
    _eventBus.fireEventFromSource( new OpenEvent( this ), this );
  }

  /**
   * Fire a Close event.
   */
  protected final void onClose()
  {
    _eventBus.fireEventFromSource( new CloseEvent( this ), this );
  }

  /**
   * Fire a Message event.
   */
  protected final void onMessage( final String data )
  {
    _eventBus.fireEventFromSource( new MessageEvent( this, data ), this );
  }

  /**
   * Fire an Error event.
   */
  protected final void onError()
  {
    _eventBus.fireEventFromSource( new ErrorEvent( this ), this );
  }

  /**
   * Detector for browser support.
   */
  private static class SupportDetector
  {
    public boolean isSupported()
    {
      return Html5EventSource.isSupported();
    }

    public Factory newFactory()
    {
      return new Html5EventSource.Factory();
    }
  }

  /**
   * Detector for browsers without EventSource support.
   */
  @SuppressWarnings( "unused" )
  private static class NoSupportDetector
    extends SupportDetector
  {
    @Override
    public boolean isSupported()
    {
      return false;
    }

    @Override
    public Factory newFactory()
    {
      return null;
    }
  }

  private static SupportDetector getSupportDetector()
  {
    if ( null == g_supportDetector )
    {
      g_supportDetector = com.google.gwt.core.shared.GWT.create( SupportDetector.class );
    }
    return g_supportDetector;
  }
}
