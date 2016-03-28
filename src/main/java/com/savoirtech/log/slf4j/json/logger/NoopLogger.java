package com.savoirtech.log.slf4j.json.logger;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class NoopLogger implements JsonLogger {

  @Override
  public JsonLogger category(String category) {
    return this;
  }

  @Override
  public JsonLogger category(Supplier<String> category) {
    return this;
  }

  @Override
  public JsonLogger map(String key, Map map) {
    return this;
  }

  @Override
  public JsonLogger map(String key, Supplier<Map> map) {
    return this;
  }

  @Override
  public JsonLogger list(String key, List list) {
    return this;
  }

  @Override
  public JsonLogger list(String key, Supplier<List> list) {
    return this;
  }

  @Override
  public JsonLogger message(String message) {
    return this;
  }

  @Override
  public JsonLogger message(Supplier<String> message) {
    return this;
  }

  @Override
  public JsonLogger field(String key, String value) {
    return this;
  }

  @Override
  public JsonLogger field(String key, Supplier<String> value) {
    return this;
  }

  @Override
  public void log() {

  }
}
