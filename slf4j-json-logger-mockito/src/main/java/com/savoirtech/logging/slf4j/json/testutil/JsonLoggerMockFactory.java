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

package com.savoirtech.logging.slf4j.json.testutil;

import com.google.gson.JsonElement;

import com.savoirtech.logging.slf4j.json.logger.JsonLogger;

import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory for simple creation of JsonLogger mocks that properly return the mock on all of the
 * chained method calls.
 *
 * Created by art on 3/31/16.
 */
public class JsonLoggerMockFactory {

  public JsonLogger createJsonLoggerMock() {
    JsonLogger result = Mockito.mock(JsonLogger.class);

    Mockito.when(result.exception(Mockito.anyString(), Mockito.any(Exception.class)))
        .thenReturn(result);
    Mockito.when(result.field(Mockito.anyString(), Mockito.anyString())).thenReturn(result);
    Mockito.when(result.field(Mockito.anyString(), Mockito.any(Supplier.class))).thenReturn(result);
    Mockito.when(result.json(Mockito.anyString(), Mockito.any(JsonElement.class)))
        .thenReturn(result);
    Mockito.when(result.json(Mockito.anyString(), Mockito.any(Supplier.class))).thenReturn(result);
    Mockito.when(result.list(Mockito.anyString(), Mockito.any(List.class))).thenReturn(result);
    Mockito.when(result.list(Mockito.anyString(), Mockito.any(Supplier.class))).thenReturn(result);
    Mockito.when(result.map(Mockito.anyString(), Mockito.any(Map.class))).thenReturn(result);
    Mockito.when(result.map(Mockito.anyString(), Mockito.any(Supplier.class))).thenReturn(result);
    Mockito.when(result.message(Mockito.anyString())).thenReturn(result);
    Mockito.when(result.message(Mockito.any(Supplier.class))).thenReturn(result);

    return result;
  }
}
