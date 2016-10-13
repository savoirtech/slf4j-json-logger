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

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MarkerFactory;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertSame;

/**
 * Verify operation of the NoopLogger; note that there is very little validation here since all of
 * the methods operate as no-ops.  This test still serves to show how it operates, verify that it
 * will operate without an underlying SLF4J logger, and to confirm that the lambda expressions are
 * not evaluated.
 *
 * Created by art on 3/28/16.
 */
public class NoopLoggerTest {

  private NoopLogger logger;

  @Before
  public void setupTest() throws Exception {
    this.logger = new NoopLogger();
  }

  @Test
  public void testMap() throws Exception {
    JsonLogger result = this.logger.map("x-map-key-x", new HashMap());
    assertSame(result, this.logger);
  }

  @Test
  public void testMapSupplier() throws Exception {
    JsonLogger result = this.logger.map("x-map-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    });
    assertSame(result, this.logger);
  }

  @Test
  public void testList() throws Exception {
    JsonLogger result = this.logger.list("x-list-key-x", new LinkedList());
    assertSame(result, this.logger);
  }

  @Test
  public void testListSupplier() throws Exception {
    JsonLogger result = this.logger.list("x-list-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    });
    assertSame(result, this.logger);
  }

  @Test
  public void testMessage() throws Exception {
    JsonLogger result = this.logger.message("x-message-x");
    assertSame(result, this.logger);
  }

  @Test
  public void testMessageSupplier() throws Exception {
    JsonLogger result = this.logger.message(() -> {
      throw new RuntimeException("unexpected execution");
    });
    assertSame(result, this.logger);
  }

  @Test
  public void testField() throws Exception {
    JsonLogger result = this.logger.field("x-field-key-x", "x-field-value-x");
    assertSame(result, this.logger);
  }

  @Test
  public void testFieldSupplier() throws Exception {
    JsonLogger result = this.logger.field("x-field-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    });
    assertSame(result, this.logger);
  }

  @Test
  public void testJson() {
    JsonLogger result = this.logger.json("x-field-key-x", new JsonObject());
    assertSame(result, this.logger);
  }

  @Test
  public void testJsonSupplier() {
    JsonLogger result = this.logger.json("x-field-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    });
    assertSame(result, this.logger);
  }

  @Test
  public void testException() {
    JsonLogger result = this.logger.exception("x-map-key-x", new RuntimeException("Not logged"));
    assertSame(result, this.logger);
  }

  @Test
  public void testStack() {
    JsonLogger result = this.logger.stack();
    assertSame(result, this.logger);
  }

  @Test
  public void testLog() throws Exception {
    this.logger.log();
  }

  @Test
  public void testMarker() throws Exception {
    JsonLogger result = this.logger.marker(MarkerFactory.getMarker("x-test-marker-x"));
    assertSame(result, this.logger);
  }
}
