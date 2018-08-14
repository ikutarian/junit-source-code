package com.okada.junit;


import java.lang.reflect.Method;

public class TestRunner {

    public static void main(String testCaseClassName) {
        Class testCaseClass;
        Method suiteMethod;
        TestSuite suite;

        try {
            testCaseClass = Class.forName(testCaseClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到该类：" + testCaseClassName);
            return;
        }

        try {
            suiteMethod = testCaseClass.getMethod("suite");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println(testCaseClassName + " 类应该提供一个 suite() 静态方法");
            return;
        }

        try {
            // 调用静态方法
            // 因为是调用静态方法，只需要传递一个null即可
            suite = (TestSuite) suiteMethod.invoke(null);
        } catch (Exception e) {
            System.out.println("无法调用 suite() 方法");
            return;
        }

        run(suite);
    }

    public static void run(TestSuite suite) {
        TestResult testResult = new TestResult();

        long startTime = System.currentTimeMillis();
        suite.run(testResult);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        System.out.println();
        System.out.println("Time: " + runTime / 1000 + "." + runTime % 1000);

        testResult.print();
    }
}
