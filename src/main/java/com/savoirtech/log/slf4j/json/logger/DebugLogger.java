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

import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;

public class DebugLogger extends AbstractJsonLogger {

  public static final String LOG_LEVEL = "DEBUG";

  public DebugLogger(org.slf4j.Logger slf4jLogger, FastDateFormat formatter, GsonBuilder gsonBuilder) {
    super(slf4jLogger, formatter, gsonBuilder);
  }

  @Override
  public void log() {
    slf4jLogger.debug(formatMessage(LOG_LEVEL));
  }

  public String toString() {
    return formatMessage(LOG_LEVEL);
  }
}
