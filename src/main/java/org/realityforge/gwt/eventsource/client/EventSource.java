package org.realityforge.gwt.eventsource.client;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.gwt.eventsource.client.event.CloseEvent;
import org.realityforge.gwt.eventsource.client.event.ErrorEvent;
import org.realityforge.gwt.eventsource.client.event.MessageEvent;
import org.realityforge.gwt.eventsource.client.event.OpenEvent;
import org.realityforge.gwt.eventsource.client.html5.Html5EventSource;

/**
 * Adapter class for the browser based EventSource.
 *
 * <p>
 *   This is the browser base interface to <a href="http://dev.w3.org/html5/eventsource/">Server-Sent Events</a>.
 * </p>
 */
public abstract class EventSource
{
  /**
   * Interface used to construct EventSource instances.
   */
  public interface Factory
  {
    EventSource newEventSource();
  }

  /**
   * State enum representing state of eventSource.
   */
  public enum ReadyState
  {
    CONNECTING, OPEN, CLOSED
  }

  private static SupportDetector g_supportDetector;
  private static Factory g_factory;
  private final EventBus _eventBus;

  /**
   * Create an EventSource if supported by the platform.
   *
   * This method will use the registered factory to create the EventSource instance.
   *
   * @return an EventSource instance, if supported by the platform, null otherwise.
   */
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

  /**
   * Register a factory to be used to construct EventSource instances.
   * This is not usually used as the built in browser based factory will
   * be detected and used if available. The register method is typically used
   * by test frameworks.
   *
   * @param factory the factory to register.
   */
  public static void register( @Nonnull final Factory factory )
  {
    g_factory = factory;
  }

  /**
   * Deregister factory if the specified factory is the registered factory.
   *
   * @param factory the factory to deregister.
   * @return true if able to deregister, false otherwise.
   */
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

  /**
   * Construct a new EventSource.
   *
   * @param eventBus the EventBus to use.
   */
  protected EventSource( final EventBus eventBus )
  {
    _eventBus = eventBus;
  }

  /**
   * Open a connection to specified url, defaulting to setting the withCredentials option to false.
   *
   * @param url the url to open.
   * @throws IllegalStateException if event source is already open.
   */
  public final void open( @Nonnull final String url )
    throws IllegalStateException
  {
    open( url, false );
  }

  /**
   * Open a connection to specified url.
   *
   * @param url the url to open.
   * @param withCredentials the withCredentials option.
   * @throws IllegalStateException if event source is already open.
   */
  public abstract void open( @Nonnull String url, boolean withCredentials )
    throws IllegalStateException;

  /**
   * Close the EventSource and stop receiving MessageEvents.
   *
   * @throws IllegalStateException if the event source is not open.
   */
  public abstract void close()
    throws IllegalStateException;

  /**
   * The value of the url that was used to open the EventSource.
   *
   * @return the url.
   * @throws IllegalStateException if the event source is not open.
   */
  @Nonnull
  public abstract String getURL()
    throws IllegalStateException;

  /**
   * The value of the withCredentials option used to open the EventSource.
   *
   * @return the withCredentials option.
   * @throws IllegalStateException if the event source is not open.
   */
  public abstract boolean getWithCredentials()
    throws IllegalStateException;

  /**
   * Specify a new message type to start receiving MessageEvents from.
   *
   * @param messageType the messageType value.
   * @return true if subscribe occurred, false if already subscribed.
   * @throws IllegalStateException if the event source is not open.
   * @throws IllegalArgumentException if an invalid value for is specified for messageType.
   */
  public abstract boolean subscribeTo( @Nonnull String messageType )
    throws IllegalStateException, IllegalArgumentException;

  /**
   * Specify a message type to stop receiving events from.
   *
   * @param messageType the messageType value.
   * @return true if the unsubscribe occurred, false if not already subscribed.
   * @throws IllegalStateException if the event source is not open.
   * @throws IllegalArgumentException if an invalid value for is specified for messageType.
   */
  public abstract boolean unsubscribeFrom( @Nonnull String messageType )
    throws IllegalStateException;

  /**
   * @return the state of the EventSource.
   */
  @Nonnull
  public abstract ReadyState getReadyState();

  /**
   * Add listener for open events.
   *
   * @param handler the event handler.
   * @return the HandlerRegistration that manages the listener.
   */
  @Nonnull
  public final HandlerRegistration addOpenHandler( @Nonnull OpenEvent.Handler handler )
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
  public final HandlerRegistration addCloseHandler( @Nonnull CloseEvent.Handler handler )
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
  public final HandlerRegistration addMessageHandler( @Nonnull MessageEvent.Handler handler )
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
  public final HandlerRegistration addErrorHandler( @Nonnull ErrorEvent.Handler handler )
  {
    return _eventBus.addHandler( ErrorEvent.getType(), handler );
  }

  /**
   * Fire an Open event.
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
  protected final void onMessage( @Nullable final String lastEventId,
                                  @Nonnull final String type,
                                  @Nonnull final String data )
  {
    _eventBus.fireEventFromSource( new MessageEvent( this, lastEventId, type, data ), this );
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
