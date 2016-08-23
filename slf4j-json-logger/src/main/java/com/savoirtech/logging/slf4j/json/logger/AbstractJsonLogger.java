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

package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;

import java.text.Format;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractJsonLogger implements JsonLogger {
  protected org.slf4j.Logger slf4jLogger;
  private FastDateFormat formatter;
  private Gson gson;
  private JsonObject jsonObject;
  private boolean includeLoggerName;

  public AbstractJsonLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, Gson gson, boolean includeLoggerName) {
    this.slf4jLogger = slf4jLogger;
    this.formatter = formatter;
    this.gson = gson;
    this.includeLoggerName = includeLoggerName;

    jsonObject = new JsonObject();
  }

  @Override
  public JsonLogger message(String message) {
    try {
      jsonObject.add("message", gson.toJsonTree(message));
    }
    catch (Exception e) {
      jsonObject.add("message", gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger message(Supplier<String> message) {
    try {
      jsonObject.add("message", gson.toJsonTree(message.get()));
    }
    catch (Exception e) {
      jsonObject.add("message", gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger map(String key, Map map) {
    try {
      jsonObject.add(key, gson.toJsonTree(map));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger map(String key, Supplier<Map> map) {
    try {
      jsonObject.add(key, gson.toJsonTree(map.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger list(String key, List list) {
    try {
      jsonObject.add(key, gson.toJsonTree(list));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger list(String key, Supplier<List> list) {
    try {
      jsonObject.add(key, gson.toJsonTree(list.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger field(String key, Object value) {
    try {
      jsonObject.add(key, gson.toJsonTree(value));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger field(String key, Supplier value) {
    try {
      // in the rare case that the value passed is null, this method will be selected as more specific than the Object
      // method.  Have to handle it here or the value.get() will NullPointer
      if (value == null) {
        jsonObject.add(key, null);
      } else {
        jsonObject.add(key, gson.toJsonTree(value.get()));
      }
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger json(String key, JsonElement jsonElement) {
    try {
      jsonObject.add(key, jsonElement);
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger json(String key, Supplier<JsonElement> jsonElement) {
    try {
      jsonObject.add(key, jsonElement.get());
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger exception(String key, Exception exception) {
    try {
      jsonObject.add(key, gson.toJsonTree(formatException(exception)));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public JsonLogger stack() {
    try {
      jsonObject.add("stacktrace", gson.toJsonTree(formatStack()));
    }
    catch (Exception e) {
      jsonObject.add("stacktrace", gson.toJsonTree(formatException(e)));
    }
    return this;
  }

  @Override
  public abstract void log();

  protected String formatMessage(String level) {

    jsonObject.add("level", gson.toJsonTree(level));
    jsonObject.add("thread_name", gson.toJsonTree(Thread.currentThread().getName()));

    try {
      jsonObject.add("class", gson.toJsonTree(getCallingClass()));
    }
    catch (Exception e) {
      jsonObject.add("class", gson.toJsonTree(formatException(e)));
    }

    if (includeLoggerName) {
      jsonObject.add("logger_name", gson.toJsonTree(slf4jLogger.getName()));
    }

    try {
      jsonObject.add("@timestamp", gson.toJsonTree(getCurrentTimestamp(formatter)));
    }
    catch (Exception e) {
      jsonObject.add("@timestamp", gson.toJsonTree(formatException(e)));
    }

    Map mdc = MDC.getCopyOfContextMap();
    if (mdc != null && !mdc.isEmpty()) {
      try {
        jsonObject.add("mdc", gson.toJsonTree(mdc));
      }
      catch (Exception e) {
        jsonObject.add("mdc", gson.toJsonTree(formatException(e)));
      }
    }

    return gson.toJson(jsonObject);
  }

  private String getCallingClass() {
    StackTraceElement[] stackTraceElements = (new Exception()).getStackTrace();
    return stackTraceElements[3].getClassName();
  }

  private String getCurrentTimestamp(Format formatter) {
    return formatter.format(System.currentTimeMillis());
  }

  private String formatException(Exception e) {
    return ExceptionUtils.getStackTrace(e);
  }

  /**
   * Some contention over performance of Thread.currentThread.getStackTrace() vs (new Exception()).getStackTrace()
   * Code in Thread.java actually uses the latter if 'this' is the current thread so we do the same
   *
   * Remove the top two elements as those are the elements from this logging class
   */
  private String formatStack() {
    StringBuilder output = new StringBuilder();
    StackTraceElement[] stackTraceElements = (new Exception()).getStackTrace();
    output.append(stackTraceElements[2]);
    for (int index = 3; index < stackTraceElements.length; index++) {
      output.append("\n\tat ")
          .append(stackTraceElements[index]);
    }
    return output.toString();
  }
}
