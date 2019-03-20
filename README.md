# Deprecated

> This project has been deprecated in favour of the EventSource support provided by
> [Elemental2](https://github.com/google/elemental2). Elemental2 is closer to the
> browser, future compatible and easier to maintain.

---

# gwt-eventsource

[![Build Status](https://secure.travis-ci.org/realityforge/gwt-eventsource.svg?branch=master)](http://travis-ci.org/realityforge/gwt-eventsource)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.gwt.eventsource/gwt-eventsource.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.gwt.eventsource%22%20a%3A%22gwt-eventsource%22)

A simple library to provide eventsource support to GWT.

## Quick Start

The simplest way to use the library is to add the following dependency
into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.gwt.eventsource</groupId>
   <artifactId>gwt-eventsource</artifactId>
   <version>1.0</version>
   <scope>provided</scope>
</dependency>
```

Then you add the following snippet into the .gwt.xml file.

```xml
<module rename-to='myapp'>
  ...

  <!-- Enable the eventsource library -->
  <inherits name="org.realityforge.gwt.eventsource.EventSource"/>
</module>
```

Then you can interact with the EventSource from within the browser.

```java
final EventSource eventSource = EventSource.newEventSourceIfSupported();
if ( null != eventSource )
{
  eventSource.setListener( new EventSourceListenerAdapter() {
    @Override
    public void onOpen( @Nonnull final EventSource eventSource )
    {
      // Connected!
    }

    @Override
    public void onMessage( @Nonnull final EventSource eventSource,
                           @Nullable final String lastEventId,
                           @Nonnull final String type,
                           @Nonnull final String data )
    {
      //Handle message
    }
  });
  eventSource.connect( "http://example.com/someurl.ext" );
  ...
  // Optionally listen to messages on message type other than "message"
  eventSource.subscribeTo( "someMessageType" );
}
```

This should be sufficient to put together a simple EventSource application.

A very simple example of this code is available in the
[gwt-eventsource-example](https://github.com/realityforge/gwt-eventsource-example)
project.

## Appendix

* [Mozilla Using server-sent events](https://developer.mozilla.org/en-US/docs/Server-sent_events/Using_server-sent_events)
* [W3C: Server-Sent Events](http://dev.w3.org/html5/eventsource/)
