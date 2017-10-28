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

import java.io.InputStream;

/**
 * Scan the classpath scanner.
 *
 * <p/>
 * Created by art on 6/28/17.
 */
public class ConfigFileClasspathScanner implements ConfigFileScanner {
    public static final String PROPERTY_CONFIG_FILE_NAME = "slf4j-json-logger.properties";

    @Override
    public JsonLoggerSettings scan() {
        JsonLoggerSettings settings = null;

        try (InputStream propertyConfigInputStream = this.openScannedConfigFile(PROPERTY_CONFIG_FILE_NAME)) {
            if (propertyConfigInputStream != null) {
                PropertyConfigFileLoader propertyConfigFileLoader = new PropertyConfigFileLoader();
                settings = propertyConfigFileLoader.load(propertyConfigInputStream);
            }
        } catch (Exception exc) {
            System.err.println("ERROR: failed to load slf4j-json-logger configuration: " + exc.getMessage());
            exc.printStackTrace();
        }

        return settings;
    }

    private InputStream openScannedConfigFile(String filename) {
        return ConfigFileClasspathScanner.class.getClassLoader().getResourceAsStream(filename);
    }
}
