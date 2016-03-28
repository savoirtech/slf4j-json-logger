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

package com.savoirtech.log.slf4j.json.logger;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

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
    this.logger.map("x-map-key-x", new HashMap());
  }

  @Test
  public void testMapSupplier() throws Exception {
    this.logger.map("x-map-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    })
        .log();
  }

  @Test
  public void testList() throws Exception {
    this.logger.list("x-list-key-x", new LinkedList());
  }

  @Test
  public void testListSupplier() throws Exception {
    this.logger.list("x-list-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    })
        .log();
  }

  @Test
  public void testMessage() throws Exception {
    this.logger.message("x-message-x").log();
  }

  @Test
  public void testMessageSupplier() throws Exception {
    this.logger.message(() -> { throw new RuntimeException("unexpected execution"); }).log();

  }

  @Test
  public void testField() throws Exception {
    this.logger.field("x-field-key-x", "x-field-value-x").log();
  }

  @Test
  public void testFieldSupplier() throws Exception {
    this.logger.field("x-field-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    }).log();
  }

  @Test
  public void testJson() {
    this.logger.json("x-field-key-x", new JsonObject()).log();
  }

  @Test
  public void testJsonSupplier() {
    this.logger.json("x-field-key-x", () -> {
      throw new RuntimeException("unexpected execution");
    });
  }

  @Test
  public void testLog() throws Exception {
    this.logger.log();
  }
}