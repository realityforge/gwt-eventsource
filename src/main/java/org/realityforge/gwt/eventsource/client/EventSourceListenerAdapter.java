package org.realityforge.gwt.eventsource.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Adapter to sub-class to make creating a listener easier.
 */
public class EventSourceListenerAdapter
  implements EventSourceListener
{
  /**
   * {@inheritDoc}
   */
  @Override
  public void onOpen( @Nonnull final EventSource eventSource )
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onClose( @Nonnull final EventSource eventSource )
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onMessage( @Nonnull final EventSource eventSource,
                         @Nullable final String lastEventId,
                         @Nonnull final String type,
                         @Nonnull final String data )
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onError( @Nonnull final EventSource eventSource )
  {
  }
}
