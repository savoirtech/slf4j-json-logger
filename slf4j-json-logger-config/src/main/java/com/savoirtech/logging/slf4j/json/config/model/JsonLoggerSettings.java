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

package com.savoirtech.logging.slf4j.json.config.model;

import java.util.function.Consumer;

/**
 * Created by art on 6/28/17.
 */
public class JsonLoggerSettings {
    public Boolean includeClassName;
    public Boolean includeLoggerName;
    public Boolean includeThreadName;

    public Boolean plainTextOverrideMode;

    public String timestampFieldName;
    public String threadFieldName;
    public String loggerFieldName;
    public String classFieldName;
    public String mdcFieldName;

    public void setToDefaults() {
        this.includeClassName  = true;
        this.includeLoggerName = true;
        this.includeThreadName = true;

        this.plainTextOverrideMode = false;

        this.timestampFieldName = "@timestamp";
        this.threadFieldName    = "thread_name";
        this.loggerFieldName    = "logger_name";
        this.classFieldName     = "class";
        this.mdcFieldName       = "mdc";
    }

    /**
     * Apply the given, defined settings to this.
     *
     * @param other
     */
    public void apply(JsonLoggerSettings other) {
        this.helpApply(other.includeClassName, (value) -> this.includeClassName = value);
        this.helpApply(other.includeLoggerName, (value) -> this.includeLoggerName= value);
        this.helpApply(other.includeThreadName, (value) -> this.includeThreadName= value);

        this.helpApply(other.plainTextOverrideMode, (value) -> this.plainTextOverrideMode = value);
        this.helpApply(other.timestampFieldName, (value) -> this.timestampFieldName = value);
        this.helpApply(other.threadFieldName, (value) -> this.threadFieldName = value);
        this.helpApply(other.loggerFieldName, (value) -> this.loggerFieldName = value);
        this.helpApply(other.mdcFieldName, (value) -> this.mdcFieldName= value);
    }

    private <T> void  helpApply(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
