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

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;

import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractJsonLogger implements JsonLogger {
  protected org.slf4j.Logger slf4jLogger;
  private FastDateFormat formatter;
  private GsonBuilder gsonBuilder;

  private String category;
  private Supplier<String> categorySupplier;

  private Map<String, Map> maps;
  private Map<String, Supplier<Map>> mapSuppliers;

  private Map<String, List> lists;
  private Map<String, Supplier<List>> listSupplier;

  private Map<String, String> fields;
  private Map<String, Supplier<String>> fieldSupplier;

  private String message;
  private Supplier<String> messageSupplier;

  private boolean hasCategory = false;
  private boolean hasCategorySupplier = false;

  private boolean hasMaps = false;
  private boolean hasMapSuppliers = false;

  private boolean hasLists = false;
  private boolean hasListSuppliers = false;

  private boolean hasFields = false;
  private boolean hasFieldSuppliers = false;

  private boolean hasMessage = false;
  private boolean hasMessageSupplier = false;

  public AbstractJsonLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, GsonBuilder gsonBuilder) {
    this.slf4jLogger = slf4jLogger;
    this.formatter = formatter;
    this.gsonBuilder = gsonBuilder;
  }

  @Override
  public JsonLogger category(String category) {
    this.category = category;
    hasCategory = true;
    return this;
  }

  @Override
  public JsonLogger category(Supplier<String> category) {
    this.categorySupplier = category;
    hasCategorySupplier = true;
    return this;
  }

  @Override
  public JsonLogger message(String message) {
    this.message = message;
    hasMessage = true;
    return this;
  }

  @Override
  public JsonLogger message(Supplier<String> message) {
    this.messageSupplier = message;
    hasMessageSupplier = true;
    return this;
  }

  @Override
  public JsonLogger map(String key, Map map) {
    if (this.maps == null) {
      this.maps = new HashMap<>();
    }
    this.maps.put(key, map);
    hasMaps = true;
    return this;
  }

  @Override
  public JsonLogger map(String key, Supplier<Map> map) {
    if (this.mapSuppliers == null) {
      this.mapSuppliers = new HashMap<>();
    }
    this.mapSuppliers.put(key, map);
    hasMapSuppliers = true;
    return this;
  }

  @Override
  public JsonLogger list(String key, List list) {
    if (this.lists == null) {
      this.lists = new HashMap<>();
    }
    this.lists.put(key, list);
    hasLists = true;
    return this;
  }

  @Override
  public JsonLogger list(String key, Supplier<List> list) {
    if (this.listSupplier == null) {
      this.listSupplier = new HashMap<>();
    }
    this.listSupplier.put(key, list);
    hasListSuppliers = true;
    return this;
  }

  @Override
  public JsonLogger field(String key, String value) {
    if (this.fields == null) {
      this.fields = new HashMap<>();
    }
    this.fields.put(key, value);
    hasFields = true;
    return this;
  }

  @Override
  public JsonLogger field(String key, Supplier<String> value) {
    if (this.fieldSupplier == null) {
      this.fieldSupplier = new HashMap<>();
    }
    this.fieldSupplier.put(key, value);
    hasFieldSuppliers = true;
    return this;
  }

  @Override
  public abstract void log();

  protected String formatMessage(String level) {
    Map<String, Object> jsonMessage = new HashMap<>();

    jsonMessage.put("level", level);

    try {
      jsonMessage.put("timestamp", getCurrentTimestamp(formatter));
    }
    catch (Exception e) {
      jsonMessage.put("timestamp", e);
    }

    if (hasCategory) {
      jsonMessage.put("category", category);
    }
    else if (hasCategorySupplier) {
      try {
        jsonMessage.put("category", categorySupplier.get());
      }
      catch (Exception e) {
        jsonMessage.put("category", e);
      }
    }

    if (hasMessage) {
      jsonMessage.put("message", message);
    }
    else if (hasMessageSupplier) {
      try {
        jsonMessage.put("message", messageSupplier.get());
      }
      catch (Exception e) {
        jsonMessage.put("message", e);
      }
    }

    if (hasFields) {
      for (Map.Entry<String, String> entry : fields.entrySet()) {
        jsonMessage.put(entry.getKey(), entry.getValue());
      }
    }

    if (hasFieldSuppliers) {
      for (Map.Entry<String, Supplier<String>> entry : fieldSupplier.entrySet()) {
        try {
          jsonMessage.put(entry.getKey(), entry.getValue().get());
        }
        catch (Exception e) {
          jsonMessage.put(entry.getKey(), e);
        }
      }
    }

    if (hasMaps) {
      for (Map.Entry<String, Map> entry : maps.entrySet()) {
        jsonMessage.put(entry.getKey(), entry.getValue());
      }
    }

    if (hasMapSuppliers) {
      for (Map.Entry<String, Supplier<Map>> entry : mapSuppliers.entrySet()) {
        try {
          jsonMessage.put(entry.getKey(), entry.getValue().get());
        }
        catch (Exception e) {
          jsonMessage.put(entry.getKey(), e);
        }
      }
    }

    if (hasLists) {
      for (Map.Entry<String, List> entry : lists.entrySet()) {
        jsonMessage.put(entry.getKey(), entry.getValue());
      }
    }

    if (hasListSuppliers) {
      for (Map.Entry<String, Supplier<List>> entry : listSupplier.entrySet()) {
        try {
          jsonMessage.put(entry.getKey(), entry.getValue().get());
        }
        catch (Exception e) {
          jsonMessage.put(entry.getKey(), e);
        }
      }
    }

    Map mdc = MDC.getCopyOfContextMap();
    if (mdc != null && !mdc.isEmpty()) {
      jsonMessage.put("MDC", MDC.getCopyOfContextMap());
    }

    Gson gson = gsonBuilder.create();
    return gson.toJson(jsonMessage);
  }

  private String getCurrentTimestamp(Format formatter) {
    return formatter.format(System.currentTimeMillis());
  }
}
