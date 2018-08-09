package com.okada.junit;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试结果
 */
public class TestResult {

    /**
     * Assert校验失败的Test的容器
     */
    protected List<TestFailure> failures;
    /**
     * 抛出异常的Test的容器
     */
    protected List<TestFailure> errors;

    public TestResult() {
        failures = new ArrayList<>(10);
        errors = new ArrayList<>(10);
    }

    public void addError(Test test, Throwable t) {
        errors.add(new TestFailure(test, t));
    }

    public void addFailure(Test test, Throwable t) {
        failures.add(new TestFailure(test, t));
    }

    /**
     * 打印测试报告
     */
    public void print() {
        printHeader();
        printErrors();
        printFailures();
    }

    /**
     * 打印测试报告的failure信息
     */
    private void printFailures() {
        if (failures.isEmpty()) {
            return;
        }

        if (failures.size() == 1) {
            System.out.println("There was 1 failure:");
        } else {
            System.out.println("There were " + failures.size() + " failures:");
        }

        for (int i = 0; i < failures.size(); i++) {
            TestFailure failure = failures.get(i);
            System.out.print(i + ") " + failure.getFailedTest());
            Throwable thrownException = failure.getThrownException();
            if (thrownException.getMessage() != null) {
                System.out.println(thrownException.getMessage());
            } else {
                // 因为System.out和System.err的输出顺序不同
                // 为了防止日志顺序错乱，使用System.out进行异常信息输出
                thrownException.printStackTrace(System.out);
            }
        }
    }

    /**
     * 打印测试报告的error信息
     */
    private void printErrors() {
        if (errors.isEmpty()) {
            return;
        }

        if (errors.size() == 1) {
            System.out.println("There was 1 error:");
        } else {
            System.out.println("There were " + errors.size() + " errors:");
        }

        for (int i = 0; i < errors.size(); i++) {
            TestFailure failure = errors.get(i);
            System.out.println(i + ") " + failure.getFailedTest());
            // 因为System.out和System.err的输出顺序不同
            // 为了防止日志顺序错乱，使用System.out进行异常信息输出
            failure.getThrownException().printStackTrace(System.out);
        }
    }


    /**
     * 打印测试报告的头部信息
     */
    public void printHeader() {
        System.out.println("------------------------");
        if (isSuccessful()) {
            System.out.print("OK");
        } else {
            System.out.println("!!!FAILURES!!!");
            System.out.println("Test Results:");
            System.out.println(" Failures: " + failures.size() + " Errors: " + errors.size());
        }
    }

    /**
     * 测试是否通过
     * <p>
     * 既没有failure也没有error就算测试通过
     */
    private boolean isSuccessful() {
        return failures.isEmpty() && errors.isEmpty();
    }
}
