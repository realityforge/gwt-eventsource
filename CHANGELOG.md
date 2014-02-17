## 0.4:

* Add getLastEventId() and getMessageType() methods to MessageEvent to expose underlying message
  characteristics.
* Ensure EventSource.getReadyState() will always return a non null value (CLOSED if not connected).
* Support a variant of EventSource.open() that sets withCredentials option.
* Rename EventSource.connect() to EventSource.open() to match terminology of underlying construct.
* Add support for EventSource.getURL() and EventSource.getWithCredentials() methods.

## 0.3:

* Restore JDK 6 compatibility.

## 0.2:

* Support EventSource.isSupported() method that will return true if EventSource.newEventSourceIfSupported()
  will not return a null value.

## 0.1:

* Initial release
