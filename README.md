## 源码获取

现在很难找到 junit 1.0 的代码了，官网和 Github 上的代码都是 4.0 起步的。经过 Google 搜索，发现一个大学的网站上还有 junit 1.0 的代码。地址是 http://www.eg.bucknell.edu/~cs475/F2000-S2001/hyde/JUnit/

## 文件夹结构

|文件夹名称|内容|
|:--|:--|
|doc|文档|
|javadoc|api文档|
|samples|例子|
|test|junit 源码|
|test.jar|junit 的 jar 包|

## 使用方法

假设要测试三个测试

- 1 + 1 是否等于 2 
- 1 + 2 是否等于 4
- 一个数字除以 0

### 1. 继承 test.framework.TestCase

首先创建一个类 `SimpleTest`，继承 `test.framework.TestCase`

```java
package samples;

import test.framework.*;

public class SimpleTest extends TestCase {

    public SimpleTest(String name) {
        super(name);
    }
}
```

### 2. 编写测试方法

然后是编写测试方法

```java
/**
 * 1 + 1 是否等于 2 的测试
 * 
 */
public void testAdd() {
    assertEquals(1 + 1, 2);
}

/**
 * 1 + 2 是否等于 4 的测试
 */
public void testAdd2() {
    assertEquals(1 + 2, 4);
}

/**
 * 数字除 0
 */
public void testDivideByZero() {
    int zero= 0;
    int result= 8 / zero;
}
```

### 3. 提供一个名为 `suite()` 的静态方法

提供一个名为 `suite()` 的静态方法，在方法体里面加入要测试的方法的方法名即可

```java
public static Test suite() {
    TestSuite suite= new TestSuite();

    suite.addTest(new SimpleTest("testAdd"));
    suite.addTest(new SimpleTest("testAdd2"));
    suite.addTest(new SimpleTest("testDivideByZero"));

    return suite;
}
```
### 4. 启动测试：

添加一个 `main` 方法，然后调用 `TestRunner` 把类的完全限定名传入即可

```java
public static void main(String[] args) {
    test.textui.TestRunner.main(new String[]{"samples.SimpleTest"});
}
```

### 5. 测试报告

运行 `main` 方法，即可得到测试报告

```
..F.E

Time: 0.3

!!!FAILURES!!!
Test Results:
Run: 3 Failures: 1 Errors: 1
There was 1 error:
1) samples.SimpleTest.testDivideByZero
java.lang.ArithmeticException: / by zero
	at samples.SimpleTest.testDivideByZero(SimpleTest.java:41)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at test.framework.TestCase.runTest(TestCase.java:218)
	at test.framework.TestCase.run(TestCase.java:184)
	at test.framework.TestSuite.run(TestSuite.java:30)
	at test.textui.TestRunner.run(TestRunner.java:81)
	at test.textui.TestRunner.main(TestRunner.java:61)
	at samples.SimpleTest.main(SimpleTest.java:84)
There was 1 failure:
1) samples.SimpleTest.testAdd2 "expected:"3"but was:"4""
```

如果使用 `test.tui.TestRunner` 可以得到一个图形化的测试报告

```java
public static void main(String[] args) {
    test.tui.TestRunner.main(new String[]{"samples.SimpleTest"});
}
```

![](./img/ui.png)

## 源码结构

源码总共分为 3 个 package

- framework
- textui
- ui

framework 用来放 junit 的核心源码。textui 是放基于控制台输出的测试报告的报告输出代码。ui 是放基于图形化界面的测试报告的报告输出代码

## 主要的类

|类名|作用|
|:--|:--|
|TestRunner|测试启动类|
|Test|测试用例接口定义类|
|TestCase|测试用例接口实现类|
|TestSuite|测试用例容器|
|TestResult|测试结果|
|TestFailure|测试错误结果的包装类|
|AssertionFailedError|测试异常|

## 继承关系

class TestRunner extends Object
interface Test
abstract class TestCase implements Test
class TestSuite implements Test
class TestResult extends Object
class AssertionFailedError extends Error

## 执行流程

以 `SimpleTest` 为例

调用 `test.textui.TestRunner.main(new String[]{"samples.SimpleTest"})`方法，在方法内部，做了 3 个操作：

1. 根据传入的完整类名 `"samples.SimpleTest"` 找到该类
2. 再找到该类的名为 `suite` 的静态方法
3. 调用 `suite` 的静态方法启动测试

`TestRunner.main()` 代码如下（稍微精简了一下）

```java
public static void main(String argv[]) {
    try {
        Class testClass= null;
        Method suiteMethod= null;
        Test suite= null;

        try {
            // 找到 SimpleTest 类
            testClass= Class.forName(testCase);
        } catch(Exception e) {
            System.out.println("Suite class \""+testCase+"\" not found");
            return;
        }

        try {
            // 找到 suite 方法
            suiteMethod= testClass.getMethod("suite", new Class[0]);
        } catch(Exception e) {
            System.out.println("The suite class should have a method named \"suite()\"");
            return;
        }

        try {
            // 调用 suite 方法，获取测试用例
            suite= (Test)suiteMethod.invoke(null, new Class[0]); // static method
        } catch(Exception e) {
            System.out.println("Could not invoke the suite() method");
            return;
        }
        // 执行测试用例
        run(suite);
    } catch(Exception e) {
        System.out.println("Could not create and run test suite");
    }
}
```

核心代码就是

```java
// 执行测试用例
run(suite);
```

方法体如下

```java
public static void run(Test suite) {
    TestResult result= new TestResult();
    long startTime= System.currentTimeMillis();

    // 运行测试用例
    suite.run(result);

    // ---- 打印测试报告 ---
    long endTime= System.currentTimeMillis();
    long runTime= endTime-startTime;
    System.out.println();
    System.out.println("Time: "+runTime/1000+"."+runTime%1000);
    result.print();
    // ---- 打印测试报告 ---
}
```

`suite.run(result)` 就是遍历所有的测试用例

```java
public class TestSuite implements Test {

    public void run(TestResult result) {
        for (Enumeration e= fTests.elements(); e.hasMoreElements(); ) {
            Test test= (Test)e.nextElement();
            test.run(result);
        }
    }
}
```

执行测试用例 `TestCase` 的 `run()` 方法

```java
public abstract class TestCase implements Test {

    public void run(TestResult result) {
        // 测试执行前的初始化操作
        setUp();

        try {
            // 执行测试
            runTest();
        } catch (AssertionFailedError e) {
            // 测试不通过
            result.addFailure(this, e);
        } catch (Throwable e) {
            // 测试时抛出异常
            result.addError(this, e);
        }

        // 测试完成后的资源回收操作
        tearDown();
    }
}
```

重头戏是在

```java
// 执行测试
runTest();
```

要明白这个方法就要回到之前在 `SimpleTest` 提供一个名为 `suite()` 的静态方法

```java
public static Test suite() {
    TestSuite suite= new TestSuite();

    suite.addTest(new SimpleTest("testAdd"));
    suite.addTest(new SimpleTest("testAdd2"));
    suite.addTest(new SimpleTest("testDivideByZero"));

    return suite;
}
```

其中通过指定方法名传入到 `TestSuit` 测试用例容器中

```java
suite.addTest(new SimpleTest("testAdd"));
suite.addTest(new SimpleTest("testAdd2"));
suite.addTest(new SimpleTest("testDivideByZero"));
```

这里的方法名就会在 `TestCase` 的 `runTest()` 方法中用到。现在来看一下 `runTest()` 方法的方法体

```java
public abstract class TestCase implements Test {

    // 要测试的方法的方法名
    private final String fName;

    /**
     * 通过构造方法传入方法名
     */
    public TestCase(String name) {
		fName= name;
	}

    protected void runTest() throws Throwable {
        // 利用反射，根据 fName 获取到测试方法
        Method runMethod = getClass().getMethod(fName, new Class[0]);
        // 执行测试方法
        runMethod.invoke(this, new Class[0]);
    }
}
```

可以看到这个方法就做了两件事：

- 利用反射获取测试方法
- 调用测试方法

## 代码执行流程总结

通过上面的分析，现在总结一下：

- 用户自定义类继承 `TestCase`
- 用户编写测试方法
- 在静态方法中 `suite()` 将测试用例让入测试用例容器 `TestSuite` 中
- `TestRunner` 调用自定义类的 `suite()` 方法获取测试用例
- 遍历测试用例，执行测试用例指定的测试方法
- 打印测试报告

## 自己实现一个 junit 1.0

为了学习，我对官方的代码进行精简，抽出重要的部分，自己实现了一个 junit。

源码在 `src` 文件夹中。核心代码在 `com.okada.junit` 包中，测试代码在 `test` 包中。运行 `test` 包中的 `MathTest` 即可得到一份测试报告

核心代码有以下文件：

|类名|作用|
|:--|:--|
|Assert|验证测试结果工具类|
|AssertionFailedError|测试异常|
|Test|接口定义，提供 `run(TestResult result)` 方法|
|TestCase|测试用例，实现 `Test` 接口的 `run(TestResult result)` 方法|
|TestFailure|测试异常封装类|
|TestResult|测试报告封装类|
|TestRunner|测试启动器|
|TestSuite|测试用例集合封装类|

现在从零开始照着 junit 1.0 的官方代码进行模仿。

### 使用方法

在看源码之前，先看一下使用方法

```java
package test;


import com.okada.junit.Assert;
import com.okada.junit.TestCase;
import com.okada.junit.TestRunner;
import com.okada.junit.TestSuite;

public class MathTest extends TestCase {

    /**
     * 传入要测试的方法的方法名
     */
    public MathTest(String testMethodName) {
        super(testMethodName);
    }

    /**
     * 测试方法
     */
    public void add() {
        Assert.assertEquals(1, 2);
    }

    /**
     * 把测试方法加入测试用例集合封装类
     */
    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();

        testSuite.addTest(new MathTest("ok"));// 该方法并不存在
        testSuite.addTest(new MathTest("add"));

        return testSuite;
    }

    public static void main(String[] args) {
        // 测试启动器启动测试
        TestRunner.main("test.MathTest");
    }
}
```

`MathTest` 继承 `TestCase`。提供一个 `add()` 测试方法，方法内部就是测试的具体内容，这里是判断 1 是否等于 2。然后再提供一个名为 `suite()` 的静态方法，把 `add()` 方法名加入 `TestSuite` 中。最后是在 `main()` 方法中调用 `TestRunner.main()` 启动测试

### TestRunner

测试的启动类，有了这个类才能调用测试用例代码。这个类提供了两个方法

- public static void main(String testCaseClassName)
- public static void run(TestSuite suite)

这两个方法都能运行测试用例，喜欢用哪个就用哪个

`main()` 方法传入测试用例类的完全限定名，利用反射，调用类的 `suite()` 静态方法获取 `TestSuite` 对象。`suite()` 方法会调用 `run()` 方法进行测试

```java
package com.okada.junit;


import java.lang.reflect.Method;

public class TestRunner {

    public static void main(String testCaseClassName) {
        Class testCaseClass;
        Method suiteMethod;
        TestSuite suite;

        try {
            // 根据类名找到该类
            testCaseClass = Class.forName(testCaseClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到该类：" + testCaseClassName);
            return;
        }

        try {
            // 获取名为suite()的方法
            suiteMethod = testCaseClass.getMethod("suite");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println(testCaseClassName + " 类应该提供一个 suite() 静态方法");
            return;
        }

        try {
            // 调用suite()静态方法，获取TestSuite返回值
            // 因为是调用静态方法，只需要传递一个null即可
            suite = (TestSuite) suiteMethod.invoke(null);
        } catch (Exception e) {
            System.out.println("无法调用 suite() 方法");
            return;
        }

        // 调用run(TestSuite suite)进行测试
        run(suite);
    }

    public static void run(TestSuite suite) {
        // 测试报告封装类
        TestResult testResult = new TestResult();

        // 测试开始时间
        long startTime = System.currentTimeMillis();
        // 开始测试
        suite.run(testResult);
        // 测试结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        // 打印测试使用的时间
        System.out.println();
        System.out.println("Time: " + runTime / 1000 + "." + runTime % 1000);

        // 打印测试报告
        testResult.print();
    }
}
```

### TestCase

每个测试类都会继承 `TestCase`，而且 `TestCase` 实现了 `Test` 接口

`Test` 接口

```java
package com.okada.junit;

public interface Test {

    /**
     * 运行测试
     *
     * 传入TestResult用来保存测试结果
     */
    void run(TestResult result);
}
```

`TestCase` 类，主要的方法是

- `setUp()`
- `run(TestResult testResult)`
- `tearDown()`

```java
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
```

`TestCase` 的核心代码就是利用反射调用测试方法

### Assert

测试的时候，需要判断测试是否通过，就需要一个工具类用来判断

```java
package com.okada.junit;

public class Assert {

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            assertEquals(notEqualsMessage("", expected, actual), false);
        }
    }

    public static void assertEquals(String message, boolean condition) {
        if (!condition) {
            throw new AssertionFailedError(message);
        }
    }

    private static String notEqualsMessage(String message, Object expected, Object actual) {
        return message + " expected:\"" + expected + "\" but was:\"" + actual + "\"";
    }
}
```

### AssertionFailedError

如果测试不通过的话，就要抛出异常

```java
package com.okada.junit;

/**
 * Assert校验失败的异常
 */
public class AssertionFailedError extends Error {

    public AssertionFailedError(String message) {
        super(message);
    }
}
```

### TestFailure

有抛出异常那么就要记录起来。`TestFailure` 类就是用来记录异常的

```java
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
```

### TestResult

测试完了，需要一份测试报告。`TestResult` 就是用来封装测试报告的类

```java
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
```

### 自定义 junit 总结

以上就是自定义 junit 的实现代码。总的流程就是：

`TestRunner` 启动 `TestCase`，调用 `Assert` 对测试结果进行验证，有错就抛出 `AssertionFailedError` 异常。捕获异常，把异常信息记录到 `TestFailure` 类。最后由 `TestResult` 打印测试报告