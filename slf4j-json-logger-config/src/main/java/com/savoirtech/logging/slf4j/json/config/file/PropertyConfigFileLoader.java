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

package com.savoirtech.logging.slf4j.json.config.file;

import com.savoirtech.logging.slf4j.json.config.model.JsonLoggerSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by art on 6/28/17.
 */
public class PropertyConfigFileLoader {
    public static final String INCLUDE_CLASS_NAME_PROP = "includeClassName";
    public static final String INCLUDE_LOGGER_NAME_PROP = "includeLoggerName";
    public static final String INCLUDE_THREAD_NAME_PROP = "includeThreadName";
    public static final String PLAIN_TEXT_OVERRIDE_MODE_PROP = "plainTextOverrideMode";

    public static final String TIMESTAMP_FIELD_NAME_PROP = "timestampFieldName";
    public static final String THREAD_FIELD_NAME_PROP = "threadFieldName";
    public static final String LOGGER_FIELD_NAME_PROP = "loggerFieldName";

    public JsonLoggerSettings load(InputStream inputStream) throws IOException {
        JsonLoggerSettings result = new JsonLoggerSettings();

        Properties props = new Properties();
        props.load(inputStream);

        for (String key : props.stringPropertyNames()) {
            switch (key) {
                case INCLUDE_CLASS_NAME_PROP:
                    result.includeClassName = Boolean.parseBoolean(props.getProperty(key));
                    break;

                case INCLUDE_LOGGER_NAME_PROP:
                    result.includeLoggerName = Boolean.parseBoolean(props.getProperty(key));
                    break;

                case INCLUDE_THREAD_NAME_PROP:
                    result.includeThreadName = Boolean.parseBoolean(props.getProperty(key));
                    break;

                case PLAIN_TEXT_OVERRIDE_MODE_PROP:
                    result.plainTextOverrideMode = Boolean.parseBoolean(props.getProperty(key));
                    break;

                case TIMESTAMP_FIELD_NAME_PROP:
                    result.timestampFieldName = props.getProperty(key);
                    break;

                case THREAD_FIELD_NAME_PROP:
                    result.threadFieldName = props.getProperty(key);
                    break;

                case LOGGER_FIELD_NAME_PROP:
                    result.loggerFieldName = props.getProperty(key);
                    break;
            }
        }

        return result;
    }
}
