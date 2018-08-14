package com.okada.junit;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestCase implements Test {

    /**
     * 测试方法名
     */
    private String testMethodName;

    public TestCase(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    /**
     * 测试开始前的初始化操作
     */
    protected void setUp() {
    }

    /**
     * 执行初始化、测试、资源回收三个步骤
     */
    public void run(TestResult testResult) {
        setUp();

        try {
            runTest();
        } catch (AssertionFailedError e) {
            // 抛出Assert校验失败的异常
            testResult.addFailure(this, e);
        } catch (Throwable e) {
            // 其他异常，比如ArrayIndexOutOfBoundsException这样异常
            testResult.addError(this, e);
        }

        tearDown();
    }

    /**
     * 进行测试
     */
    protected void runTest() throws Throwable {
        Method runMethod;

        try {
            // 利用反射，获取名为testMethodName的方法
            runMethod = getClass().getMethod(testMethodName);
        } catch (NoSuchMethodException e) {
            throw e;
        }

        try {
            // 调用名为testMethodName的方法
            runMethod.invoke(this);
        } catch (InvocationTargetException e) {
            // 当被调用的方法的内部抛出了异常而没有被捕获时，将由InvocationTargetException接收
            // 调用InvocationTargetException.getTargetException()可以获取内部抛出的异常
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    /**
     * 测试完成后的资源回收操作
     */
    protected void tearDown() {
    }

    public String toString() {
        // 格式：类的完全限定名.测试方法名
        return getClass().getName() + "." + testMethodName;
    }
}
