slf4j-json-logger
======

The need for logging in an arbitrary JSON format is growing as more logging frameworks require this in order to
index fields for search and analytics.

Existing logging frameworks have poor support for the full JSON spec and often use shortcuts to extend functionality, such
as using the MDC context to map extra key/value pairs.

This is an effort to provide support for the full JSON spec while still using industry convention (slf4j API).

Logging configuration
===========
- Because this library is building the entire log message including things normally provided by the logging implementation, such as timestamp and level, the logging output should be configured to output the message verbatim and only the message.
- Example log4j2 properties configuration:
````
log4j.appender.out.layout.ConversionPattern=%m%n
````

LoggerFactory
===========
- Convention based static factory with familiar getLogger methods
- Factory contains a static field controlling the date format.  Override as desired.
- Default date format and example output:
````
yyyy-MM-dd HH:mm:ss.SSSZ
{"message":"It works!","level":"INFO","timestamp":"2016-03-29 11:28:23.709-0400"}
````

Logger
===========
- Uses a builder pattern to help define the JSON structures being added to the log message
- Requires the log level as the first method called
- Simple example:
````
import com.savoirtech.logging.slf4j.json.LoggerFactory;

   Logger logger = LoggerFactory.getLogger(this.getClass());
   logger.info()
       .message("It works!")
       .log();
````
- With collections:
````
   Map<String, String> map = new HashMap<>();
   map.put("numberSold", "0");

   List<String> list = new ArrayList<>();
   list.add("Acme");
   list.add("Sun");

   logger.trace()
       .message("Report executed")
       .map("someStats", map)
       .list("customers", list)
       .field("year", "2016")
       .log();

{"message":"Report executed","someStats":{"numberSold":"0"},"customers":["Acme","Sun"],"year":"2016","level":"TRACE","timestamp":"2016-03-29 12:55:50.945-0400"}
````
- Gson is used to serialize objects.  The collections and objects passed in can be arbitrarily complex and/or custom.
- This library supports lambdas in order to lazily evaluate the logged objects and only evaluate if the log level is enabled.  Should be used when the log information is expensive to generate.
````
   logger.error()
       .message(() -> "Something expensive")
       .log();
````
- .message() is a convenience method for .field("message", "your message")
- Information placed in the MDC will be logged under a top level "MDC" key in the JSON structure.  Care should be taken
to not set a field, map or list at this key as it will be overwritten.
````
    Map<String, String> map = new HashMap<>();
    map.put("TTL", "90000");
    map.put("persistenceTime", "30000");

    MDC.put("caller", "127.0.0.1");

    logger.info()
        .message("Service trace")
        .map("someStats", map)
        .log();

{"message":"Service trace","someStats":{"persistenceTime":"30000","TTL":"90000"},"level":"INFO","timestamp":"2016-03-29 13:23:37.906-0400","MDC":{"caller":"127.0.0.1"}}
````