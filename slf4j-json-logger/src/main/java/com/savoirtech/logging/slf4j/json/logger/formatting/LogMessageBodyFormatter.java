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

package com.savoirtech.logging.slf4j.json.logger.formatting;

import com.google.gson.JsonObject;

/**
 * Formatter of log message bodies.
 *
 * Created by art on 7/19/17.
 */
public interface LogMessageBodyFormatter {

  /**
   * Format the log message body given the log data (fields and values) to include in the output.
   *
   * @param logData
   * @return
   */
  String format(JsonObject logData);
}
