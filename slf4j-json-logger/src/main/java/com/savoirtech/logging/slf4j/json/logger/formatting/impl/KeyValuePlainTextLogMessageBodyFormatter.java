/*
 * Copyright (c) 2017 Savoir Technologies
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

package com.savoirtech.logging.slf4j.json.logger.formatting.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.savoirtech.logging.slf4j.json.logger.formatting.LogMessageBodyFormatter;

import java.util.Map;

/**
 * Log message body formatter that formats the body in "plain text" (as opposed to a structured text
 * format such as json), using key="value" format for each of the fields included in the output.
 *
 * Ouptut Format:
 *    MESSAGE: field="value"; field="value" ...
 *
 * Created by art on 7/19/17.
 */
public class KeyValuePlainTextLogMessageBodyFormatter implements LogMessageBodyFormatter {

  public static final String MESSAGE_FIELD_NAME = "message";

  private String keyValueSeparator = "=";

  public String getKeyValueSeparator() {
    return keyValueSeparator;
  }

  public void setKeyValueSeparator(String keyValueSeparator) {
    this.keyValueSeparator = keyValueSeparator;
  }

  @Override
  public String format(JsonObject logData) {
    StringBuilder result = new StringBuilder();

    //
    // Start with the core message, if one was given.
    //
    boolean needMessageSeparator = false;
    if (logData.has(MESSAGE_FIELD_NAME)) {
      String msg = this.formatJsonElement(logData.get(MESSAGE_FIELD_NAME));

      if (!msg.isEmpty()) {
        result.append(msg);
        needMessageSeparator = true;
      }
    }

    boolean first = true;
    for (Map.Entry<String, JsonElement> oneEntry : logData.entrySet()) {
      // Format the key= part.
      String key = oneEntry.getKey();

      // Ignore the message field as that was handled earlier
      if (!key.equals(MESSAGE_FIELD_NAME)) {
        if (first) {
          if (needMessageSeparator) {
            result.append(": ");
          }
          first = false;
        } else {
          result.append("; ");
        }

        this.formatKey(result, key);

        // And now the value (which may be a complex and/or nested structure)
        JsonElement value = oneEntry.getValue();
        String formattedValue = formatJsonElement(value);
        result.append(formattedValue);
      }
    }

    return result.toString();
  }

  private String formatJsonElement(JsonElement value) {
    String formattedValue;
    if (value instanceof JsonPrimitive) {
      return this.formatPrimitive(value.getAsJsonPrimitive());
    } else if (value.isJsonObject()) {
      return "{" + this.format(value.getAsJsonObject()) + "}";
    } else if (value.isJsonArray()) {
      return this.formatArray(value.getAsJsonArray());
    } else {
      return "null";
    }
  }

  private String formatArray(JsonArray array) {
    StringBuilder formattedArray = new StringBuilder();
    formattedArray.append("[");
    boolean first = true;

    for (JsonElement oneEle : array) {
      if (first) {
        first = false;
      } else {
        formattedArray.append(", ");
      }

      String formattedEle = this.formatJsonElement(oneEle);
      formattedArray.append(formattedEle);
    }

    formattedArray.append("]");

    return formattedArray.toString();
  }

  private void formatKey(StringBuilder stringBuilder, String key) {
    if (needsQuotes(key)) {
      stringBuilder.append("\"");
      stringBuilder.append(this.escapeQuotes(key));
      stringBuilder.append("\"");
    } else {
      stringBuilder.append(key);
    }

    stringBuilder.append("=");
  }

  private String formatPrimitive(JsonPrimitive primitive) {
    if (primitive.isString()) {
      String stringValue = "\"" + this.escapeQuotes(primitive.getAsString()) + "\"";
      return stringValue;
    }

    return primitive.getAsString();
  }

  private boolean needsQuotes(String value) {
    if (value.isEmpty()) {
      return true;
    }

    return value.matches(".*[ \r\n\t\"]");
  }

  private String escapeQuotes(String value) {
    return value.replaceAll("\"", "\\\"");
  }
}
