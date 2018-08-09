package com.okada.junit;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试的容器，所有的TestCase都会放到这里类里面存起来
 * 然后循环容器里的TestCase执行测试方法
 */
public class TestSuite {

    private List<Test> tests = new ArrayList<>(10);

    public void addTest(Test test) {
        tests.add(test);
    }

    /**
     * 执行测试，并把测试结果放入TestResult中
     */
    public void run(TestResult result) {
        for (Test test : tests) {
            test.run(result);
        }
    }
}
