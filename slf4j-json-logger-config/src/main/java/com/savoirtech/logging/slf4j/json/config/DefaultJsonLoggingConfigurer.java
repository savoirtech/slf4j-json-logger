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

package com.savoirtech.logging.slf4j.json.config;

import com.savoirtech.logging.slf4j.json.config.file.ConfigFileClasspathScanner;
import com.savoirtech.logging.slf4j.json.config.file.ConfigFileScanner;
import com.savoirtech.logging.slf4j.json.config.model.JsonLoggerSettings;

/**
 * Created by art on 6/28/17.
 */
public class DefaultJsonLoggingConfigurer implements JsonLoggingConfigurer {
    private JsonLoggerSettings settings = new JsonLoggerSettings();
    private ConfigFileScanner scanner = new ConfigFileClasspathScanner();

    public void setScanner(ConfigFileScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void init() {
        this.settings.setToDefaults();

        JsonLoggerSettings updateSettings = this.scanner.scan();

        if (updateSettings != null) {
            this.settings.apply(updateSettings);
        } else {
            if (Boolean.parseBoolean(System.getProperty("com.savoirtech.logging.slf4j.debug"))) {
                System.out.println("DEBUG: slf4j-json-logger did not locate a configuration file; using defaults");
            }
        }
    }

    @Override
    public JsonLoggerSettings getSettings() {
        return this.settings;
    }
}
