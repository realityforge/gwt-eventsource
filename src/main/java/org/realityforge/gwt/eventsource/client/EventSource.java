package org.realityforge.gwt.eventsource.client;

import com.google.gwt.core.shared.GWT;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  @Nonnull
  private EventSourceListener _listener = NullEventSourceListener.INSTANCE;

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
   * Return the listener associated with the EventSource.
   *
   * @return the listener associated with the EventSource.
   */
  @Nonnull
  public final EventSourceListener getListener()
  {
    return _listener;
  }

  /**
   * Set the listener to receive messages from the EventSource.
   *
   * @param listener the listener to receive messages from the EventSource.
   */
  public final void setListener( @Nullable final EventSourceListener listener )
  {
    _listener = null == listener ? NullEventSourceListener.INSTANCE : listener;
  }

  /**
   * Fire an Open event.
   */
  protected final void onOpen()
  {
    getListener().onOpen( this );
  }

  /**
   * Fire a Close event.
   */
  protected final void onClose()
  {
    getListener().onClose( this );
  }

  /**
   * Fire a Message event.
   */
  protected final void onMessage( @Nullable final String lastEventId,
                                  @Nonnull final String type,
                                  @Nonnull final String data )
  {
    getListener().onMessage( this, lastEventId, type, data );
  }

  /**
   * Fire an Error event.
   */
  protected final void onError()
  {
    getListener().onError( this );
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
