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

package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.savoirtech.logging.slf4j.json.LoggerFactory;
import org.junit.Test;

/**
 * Created by art on 6/29/17.
 */
public class ConfigurationFileTestIT {

    private Logger logger = LoggerFactory.getLogger(ConfigurationFileTestIT.class);

    // TBD999: use a real test, capturing the log output and verifying it
    @Test
    public void testConfigurationFile() {
        JsonObject complexField = new JsonObject();
        complexField.add("count", new JsonPrimitive(3));
        complexField.add("name", new JsonPrimitive("Joe"));

        JsonArray arrayField = new JsonArray();
        arrayField.add(12);
        arrayField.add(10);
        arrayField.add(3);
        arrayField.add(22);

        // Nested
        complexField.add("ages", arrayField);

        logger.info()
            .message("Hello World")
            .field("requestedBy", "Tom")
            .field("userStats", complexField)
            .field("ages", arrayField)
            .log();
    }
}
