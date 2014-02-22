gwt-eventsource
---------------

[![Build Status](https://secure.travis-ci.org/realityforge/gwt-eventsource.png?branch=master)](http://travis-ci.org/realityforge/gwt-eventsource)

A simple library to provide eventsource support to GWT.

Quick Start
===========

The simplest way to use the library is to add the following dependency
into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.gwt.eventsource</groupId>
   <artifactId>gwt-eventsource</artifactId>
   <version>0.6</version>
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
  eventSource.addOpenHandler( new OpenEvent.Handler()
  {
    @Override
    public void onOpenEvent( @Nonnull final OpenEvent event )
    {
      // Connected!
    }
  } );
  eventSource.addMessageHandler( new MessageEvent.Handler()
  {
    @Override
    public void onMessageEvent( @Nonnull final MessageEvent event )
    {
      //Handle message
    }
  } );
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

Appendix
--------

* [Mozilla Using server-sent events](https://developer.mozilla.org/en-US/docs/Server-sent_events/Using_server-sent_events)
* [W3C: Server-Sent Events](http://dev.w3.org/html5/eventsource/)
