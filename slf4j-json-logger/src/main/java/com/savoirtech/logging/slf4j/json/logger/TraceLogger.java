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

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Marker;

public class TraceLogger extends AbstractJsonLogger {

  public static final String LOG_LEVEL = "TRACE";

  public TraceLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, Gson gson, boolean includeLoggerName) {
    super(slf4jLogger, formatter, gson, includeLoggerName);
  }

  @Override
  public void log() {
    slf4jLogger.trace(formatMessage(LOG_LEVEL));
  }

  @Override
  public void log(Marker marker) {
    slf4jLogger.trace(marker, formatMessage(marker.getName(), LOG_LEVEL));
  }

  public String toString() {
    return formatMessage(LOG_LEVEL);
  }
}
