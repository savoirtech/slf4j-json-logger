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
import com.savoirtech.log.slf4j.json.LoggerFactory;

   Logger logger = LoggerFactory.getLogger(this.getClass());
   logger.info()
       .message("It works!")
       .log();
````
- With collections:
````
   logger.trace()
       .message("Report executed")
       .map("someStats", map)
       .list("customers", list)
       .field("year", "2016")
       .log();
````
- Gson is used to serialize objects.  The collections and objects passed in can be arbitrarily complex and/or custom.
- This library supports lambdas in order to lazily evaluate the logged objects and only evaluate if the log level is enabled.  Should be used when the log information is expensive to generate.
````
   logger.error()
       .message(() -> "Something expensive")
       .log();
````