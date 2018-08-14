package com.okada.junit;

public interface Test {

    /**
     * 运行测试
     *
     * 传入TestResult用来保存测试结果
     */
    void run(TestResult result);
}
