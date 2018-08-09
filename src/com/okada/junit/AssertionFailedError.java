package com.okada.junit;

/**
 * Assert校验失败的异常
 */
public class AssertionFailedError extends Error {

    public AssertionFailedError(String message) {
        super(message);
    }
}
