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
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verify the operation of the logger settings.
 */
public class ToggleTest {

    private Gson gson;

    String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
    FastDateFormat formatter = FastDateFormat.getInstance(dateFormatString);

    @Test
    public void doesNotIncludeThreadName() {
        String expectedThreadNameKey = "\"thread_name\":";
        String expectedThreadNameValue = "\"main\"";

        org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
        when(slf4jLogger.isInfoEnabled()).thenReturn(true);
        when(slf4jLogger.getName()).thenReturn(this.getClass().getName());

        Logger logger = new Logger(slf4jLogger, formatter);
        logger.setIncludeClassName(false);
        logger.setIncludeThreadName(false);

        JsonLogger infoLogger = logger.info();

        infoLogger
                .message("something")
                .log();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(slf4jLogger).info(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();
        assert (!actualMessage.contains(expectedThreadNameKey));
        assert (!actualMessage.contains(expectedThreadNameValue));
    }

    @Test
    public void doesIncludeThreadName() {
        String expectedThreadNameKey = "\"thread_name\":";
        String expectedThreadNameValue = "\"main\"";

        org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
        when(slf4jLogger.isInfoEnabled()).thenReturn(true);
        when(slf4jLogger.getName()).thenReturn(this.getClass().getName());

        Logger logger = new Logger(slf4jLogger, formatter);

        JsonLogger infoLogger = logger.info();
        logger.setIncludeClassName(false);
        logger.setIncludeThreadName(true);

        infoLogger
                .message("something")
                .log();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(slf4jLogger).info(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();
        assert (actualMessage.contains(expectedThreadNameKey));
        assert (actualMessage.contains(expectedThreadNameValue));
    }

    @Test
    public void doesNotIncludeClassName() {
        String expectedClassNameKey = "\"class\":";
        String expectedClassNameValue = "\"com.savoirtech.logging.slf4j.json.logger.BasicLoggingTests\"";

        org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
        when(slf4jLogger.isInfoEnabled()).thenReturn(true);
        when(slf4jLogger.getName()).thenReturn(this.getClass().getName());

        Logger logger = new Logger(slf4jLogger, formatter);
        logger.setIncludeClassName(false);
        logger.setIncludeThreadName(false);

        JsonLogger infoLogger = logger.info();

        infoLogger
                .message("something")
                .log();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(slf4jLogger).info(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();

        assert (!actualMessage.contains(expectedClassNameKey));
        assert (!actualMessage.contains(expectedClassNameValue));
    }

    @Test
    public void doesIncludeClassName() {
        String expectedClassNameKey = "\"class\":";
        String expectedClassNameValue = "\"com.savoirtech.logging.slf4j.json.logger.ToggleTest\"";

        org.slf4j.Logger slf4jLogger = mock(org.slf4j.Logger.class);
        when(slf4jLogger.isInfoEnabled()).thenReturn(true);
        when(slf4jLogger.getName()).thenReturn(this.getClass().getName());

        Logger logger = new Logger(slf4jLogger, formatter);
        JsonLogger infoLogger = logger.info();
        logger.setIncludeClassName(true);
        logger.setIncludeThreadName(false);

        infoLogger
                .message("something")
                .log();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(slf4jLogger).info(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();

        assert (actualMessage.contains(expectedClassNameKey));
        assert (actualMessage.contains(expectedClassNameValue));
    }
}
