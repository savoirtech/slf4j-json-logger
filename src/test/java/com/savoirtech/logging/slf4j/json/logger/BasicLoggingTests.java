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

import com.savoirtech.logging.slf4j.json.Logger;
import com.savoirtech.logging.slf4j.json.LoggerFactory;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BasicLoggingTests {

  String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  FastDateFormat formatter = FastDateFormat.getInstance(dateFormatString);

  @Test
  public void itWorks() {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.trace().message("It works!").log();
    logger.debug().message("It works!").log();
    logger.info().message("It works!").log();
    logger.warn().message("It works!").log();
    logger.error().message("It works!").log();
  }

  @Test
  public void messageEnabled() {
    String expectedLevelElement = "\"level\":\"INFO\"";
    String expectedMessageElement = "\"message\":\"My message\"";

    org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
    when(slf4jLogger.isInfoEnabled()).thenReturn(true);

    Logger logger = new Logger(slf4jLogger, formatter);

    logger.info().message("My message").log();

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    verify(slf4jLogger).info(messageCaptor.capture());

    String actualMessage = messageCaptor.getValue();

    assert(actualMessage.contains(expectedLevelElement));
    assert(actualMessage.contains(expectedMessageElement));
    assert(actualMessage.startsWith("{"));
    assert(actualMessage.endsWith("}"));
  }

  @Test
  public void messageDisabled() {
    org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
    when(slf4jLogger.isDebugEnabled()).thenReturn(false);

    Logger logger = new Logger(slf4jLogger, formatter);

    logger.info().message("My message").log();

    verify(slf4jLogger, times(0)).debug(anyString());
  }

  @Test
  public void allCollections() {
    String expectedLevelElement = "\"level\":\"TRACE\"";
    String expectedMessageElement = "\"message\":\"Report executed\"";
    String expectedMapElement = "\"someStats\":{\"numberSold\":\"0\"}";
    String expectedListElement = "\"customers\":[\"Acme\",\"Sun\"]";
    String expectedFieldElement = "\"year\":\"2016\"";

    org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
    when(slf4jLogger.isTraceEnabled()).thenReturn(true);

    Logger logger = new Logger(slf4jLogger, formatter);

    Map<String, String> map = new HashMap<>();
    map.put("numberSold", "0");

    List<String> list = new ArrayList<>();
    list.add("Acme");
    list.add("Sun");

    logger.trace()
        .message("Report executed")
        .map("someStats", map)
        .list("customers", list)
        .field("year", "2016")
        .log();

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    verify(slf4jLogger).trace(messageCaptor.capture());

    String actualMessage = messageCaptor.getValue();

    assert(actualMessage.contains(expectedLevelElement));
    assert(actualMessage.contains(expectedMessageElement));
    assert(actualMessage.contains(expectedMapElement));
    assert(actualMessage.contains(expectedListElement));
    assert(actualMessage.contains(expectedFieldElement));
    assert(actualMessage.startsWith("{"));
    assert(actualMessage.endsWith("}"));
  }

  @Test
  public void fieldOverwritesCategory() {
    String unexpectedMessageElement = "\"message\":\"This gets overwritten\"";
    String expectedMessageElement = "\"message\":\"This wins\"";

    org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
    when(slf4jLogger.isWarnEnabled()).thenReturn(true);

    Logger logger = new Logger(slf4jLogger, formatter);

    logger.warn()
        .message("This gets overwritten")
        .field("message", "This wins")
        .log();

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    verify(slf4jLogger).warn(messageCaptor.capture());

    String actualMessage = messageCaptor.getValue();

    assert(!actualMessage.contains(unexpectedMessageElement));
    assert(actualMessage.contains(expectedMessageElement));
  }

  @Test
  public void noExceptionThrown() {

  }

  @Test
  public void lambdas() {
    String expectedCategoryElement = "\"message\":\"Something expensive\"";

    org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
    when(slf4jLogger.isErrorEnabled()).thenReturn(true);

    Logger logger = new Logger(slf4jLogger, formatter);

    logger.error()
        .message(() -> "Something expensive")
        .log();

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

    verify(slf4jLogger).error(messageCaptor.capture());

    String actualMessage = messageCaptor.getValue();

    assert(actualMessage.contains(expectedCategoryElement));
  }
}
