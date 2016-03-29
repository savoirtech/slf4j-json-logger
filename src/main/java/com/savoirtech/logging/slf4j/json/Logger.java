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

package com.savoirtech.logging.slf4j.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Wrapper for slf4j Logger that enables a builder pattern and JSON layout
 */
public class Logger {
  private org.slf4j.Logger slf4jLogger;

  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
  private FastDateFormat formatter;

  private NoopLogger noopLogger = new NoopLogger();

  public Logger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter) {
    this.slf4jLogger = slf4jLogger;
    this.formatter = formatter;
  }

  public JsonLogger trace() {
    if (slf4jLogger.isTraceEnabled()) {
      return new TraceLogger(slf4jLogger, formatter, gson);
    }

    return noopLogger;
  }

  public JsonLogger debug() {
    if (slf4jLogger.isDebugEnabled()) {
      return new DebugLogger(slf4jLogger, formatter, gson);
    }

    return noopLogger;
  }

  public JsonLogger info() {
    if (slf4jLogger.isInfoEnabled()) {
      return new InfoLogger(slf4jLogger, formatter, gson);
    }

    return noopLogger;
  }

  public JsonLogger warn() {
    if (slf4jLogger.isWarnEnabled()) {
      return new WarnLogger(slf4jLogger, formatter, gson);
    }

    return noopLogger;
  }

  public JsonLogger error() {
    if (slf4jLogger.isErrorEnabled()) {
      return new ErrorLogger(slf4jLogger, formatter, gson);
    }

    return noopLogger;
  }
}
