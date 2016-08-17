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
    assert(logMessage.contains("\"stackTrace\":\"com.savoirtech.logging.slf4j.json.logger.StackTest$Class4.logMe(StackTest.java:79)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class3.logMe(StackTest.java:69)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class2.logMe(StackTest.java:57)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest$Class1.logMe(StackTest.java:45)\\n\\tat com.savoirtech.logging.slf4j.json.logger.StackTest.stackDump(StackTest.java:86)\\n\\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\\n\\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\\n\\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\n\\tat java.lang.reflect.Method.invoke(Method.java:497)\\n\\tat org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)\\n\\tat org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)\\n\\tat org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)\\n\\tat org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)\\n\\tat org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)\\n\\tat org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)\\n\\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)\\n\\tat org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)\\n\\tat org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)\\n\\tat org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)\\n\\tat org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)\\n\\tat org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)\\n\\tat org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)\\n\\tat org.junit.runners.ParentRunner.run(ParentRunner.java:363)\\n\\tat org.junit.runner.JUnitCore.run(JUnitCore.java:137)\\n\\tat com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)\\n\\tat com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:234)\\n\\tat com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:74)\\n\\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\\n\\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\\n\\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\n\\tat java.lang.reflect.Method.invoke(Method.java:497)\\n\\tat com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)\""));
  }

}
