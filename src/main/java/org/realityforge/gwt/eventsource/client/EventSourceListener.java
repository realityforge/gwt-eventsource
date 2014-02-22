package org.realityforge.gwt.eventsource.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Listener interface for receiving events from an EventSource.
 */
public interface EventSourceListener
{
  /**
   * Fire an Open event.
   */
  void onOpen( @Nonnull EventSource eventSource );

  /**
   * Fire a Close event.
   */
  void onClose( @Nonnull EventSource eventSource );

  /**
   * Fire a Message event.
   */
  void onMessage( @Nonnull EventSource eventSource,
                  @Nullable String lastEventId,
                  @Nonnull String type,
                  @Nonnull String data );

  /**
   * Fire an Error event.
   */
  void onError( @Nonnull EventSource eventSource );
}
