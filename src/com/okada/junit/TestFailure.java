package com.okada.junit;

/**
 * 测试失败信息的包装类
 */
public class TestFailure {

    /**
     * 失败的Test
     */
    private Test failedTest;

    /**
     * 失败时，抛出的失败异常
     */
    private Throwable thrownException;

    public TestFailure(Test failedTest, Throwable thrownException) {
        this.failedTest = failedTest;
        this.thrownException = thrownException;
    }

    public Test getFailedTest() {
        return failedTest;
    }

    public Throwable getThrownException() {
        return thrownException;
    }
}
