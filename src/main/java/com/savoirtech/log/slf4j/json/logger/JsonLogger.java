/*
 * Copyright (c) 2016 Savoir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.savoirtech.log.slf4j.json.logger;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface JsonLogger {

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
   * Add an arbitrary JsonElement object to the top level with the given key.
   */
  JsonLogger json(String key, JsonElement jsonElement);

  /**
   * Add an arbitrary JsonElement object to the top level with the given key that is lazily evaluated only if the message is logged
   */
  JsonLogger json(String key, Supplier<JsonElement> jsonElement);

  /**
   * Log the formatted message
   */
  void log();
}
