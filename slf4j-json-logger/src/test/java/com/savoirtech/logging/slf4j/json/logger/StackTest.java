package com.savoirtech.logging.slf4j.json.logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StackTest {
  private AbstractJsonLogger logger;

  private org.slf4j.Logger slf4jLogger;

  private Gson gson;

  private String dateFormatString = "yyyy-MM-dd HH:mm:ss.SSSZ";
  private FastDateFormat formatter;

  private String logMessage;

  @Before
  public void before() {
    this.slf4jLogger = Mockito.mock(org.slf4j.Logger.class);
    this.gson = new GsonBuilder().disableHtmlEscaping().create();
    this.formatter = FastDateFormat.getInstance(dateFormatString);

    logger = new AbstractJsonLogger(slf4jLogger, formatter, gson, true) {
      @Override
      public void log() {
        logMessage = formatMessage("INFO");
      }
    };
  }

  private class Class1 {
    private Class2 class2;

    public Class1() {
      this.class2 = new Class2();
    }

    public void logMe() {
      class2.logMe();
    }
  }

  private class Class2 {
    private Class3 class3;

    public Class2() {
      this.class3 = new Class3();
    }

    public void logMe() {
      class3.logMe();
    }
  }

  private class Class3 {
    private Class4 class4;

    public Class3() {
      this.class4 = new Class4();
    }

    public void logMe() {
      class4.logMe();
    }
  }

  private class Class4 {

    public Class4() {

    }
    public void logMe() {
      logger.stack().message("Some message").log();
    }
  }

  @Test
  public void stack() {
    Class1 class1 = new Class1();
    class1.logMe();
    assert(logMessage.contains("\"stacktrace\":\"com.savoirtech.logging.slf4j.json.logger.StackTest$Class4.logMe(StackTest.java:79)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class3.logMe(StackTest.java:69)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class2.logMe(StackTest.java:57)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class1.logMe(StackTest.java:45)"));
  }

}
