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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

  public AbstractJsonLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, GsonBuilder gsonBuilder) {
    this.slf4jLogger = slf4jLogger;
    this.formatter = formatter;
    this.gson = gsonBuilder.create();
    jsonObject = new JsonObject();
  }

  @Override
  public JsonLogger message(String message) {
    try {
      jsonObject.add("message", gson.toJsonTree(message));
    }
    catch (Exception e) {
      jsonObject.add("message", gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger message(Supplier<String> message) {
    try {
      jsonObject.add("message", gson.toJsonTree(message.get()));
    }
    catch (Exception e) {
      jsonObject.add("message", gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger map(String key, Map map) {
    try {
      jsonObject.add(key, gson.toJsonTree(map));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger map(String key, Supplier<Map> map) {
    try {
      jsonObject.add(key, gson.toJsonTree(map.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger list(String key, List list) {
    try {
      jsonObject.add(key, gson.toJsonTree(list));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger list(String key, Supplier<List> list) {
    try {
      jsonObject.add(key, gson.toJsonTree(list.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger field(String key, String value) {
    try {
      jsonObject.add(key, gson.toJsonTree(value));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger field(String key, Supplier<String> value) {
    try {
      jsonObject.add(key, gson.toJsonTree(value.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger json(String key, JsonElement jsonElement) {
    try {
      jsonObject.add(key, jsonElement);
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger json(String key, Supplier<JsonElement> jsonElement) {
    try {
      jsonObject.add(key, jsonElement.get());
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger jsonString(String key, String jsonString) {
    try {
      jsonObject.add(key, gson.toJsonTree(jsonString));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public JsonLogger jsonString(String key, Supplier<String> jsonString) {
    try {
      jsonObject.add(key, gson.toJsonTree(jsonString.get()));
    }
    catch (Exception e) {
      jsonObject.add(key, gson.toJsonTree(e.getStackTrace()));
    }
    return this;
  }

  @Override
  public abstract void log();

  protected String formatMessage(String level) {

    jsonObject.add("level", gson.toJsonTree(level));

    try {
      jsonObject.add("timestamp", gson.toJsonTree(getCurrentTimestamp(formatter)));
    }
    catch (Exception e) {
      jsonObject.add("timestamp", gson.toJsonTree(e.getStackTrace()));
    }

    Map mdc = MDC.getCopyOfContextMap();
    if (mdc != null && !mdc.isEmpty()) {
      try {
        jsonObject.add("MDC", gson.toJsonTree(MDC.getCopyOfContextMap()));
      }
      catch (Exception e) {
        jsonObject.add("MDC", gson.toJsonTree(e.getStackTrace()));
      }
    }

    return gson.toJson(jsonObject);
  }

  private String getCurrentTimestamp(Format formatter) {
    return formatter.format(System.currentTimeMillis());
  }
}
