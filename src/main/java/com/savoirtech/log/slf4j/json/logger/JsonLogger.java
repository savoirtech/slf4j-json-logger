package com.savoirtech.log.slf4j.json.logger;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface JsonLogger {

  /**
   * Set top level category field
   */
  JsonLogger category(String category);

  /**
   * Set top level category field as a lambda or supplier that is lazily evaluated only if the message is logged
   */
  JsonLogger category(Supplier<String> category);

  /**
   * Set top level message field
   */
  JsonLogger message(String message);

  /**
   * Set top level message field as a lambda or supplier that is lazily evaluated only if the message is logged
   */
  JsonLogger message(Supplier<String> message);

  /**
   * Add a map to the JSON hierarchy
   */
  JsonLogger map(String key, Map map);

  /**
   * Add a map to the JSON hierarchy as a lambda or supplier that is lazily evaluated only if the message is logged
   */
  JsonLogger map(String key, Supplier<Map> map);

  /**
   * Add a list to the JSON hierarchy
   */
  JsonLogger list(String key, List list);

  /**
   * Add a list to the JSON hierarchy as a lambda or supplier that is lazily evaluated only if the message is logged
   */
  JsonLogger list(String key, Supplier<List> list);

  /**
   * Add a top level field
   */
  JsonLogger field(String key, String value);

  /**
   * Add a top level field as a lambda or supplier that is lazily evaluated only if the message is logged
   */
  JsonLogger field(String key, Supplier<String> value);

  /**
   * Log the formatted message
   */
  void log();
}
