package test;


import com.okada.junit.Assert;
import com.okada.junit.TestCase;
import com.okada.junit.TestRunner;
import com.okada.junit.TestSuite;

public class MathTest extends TestCase {


    public MathTest(String testMethodName) {
        super(testMethodName);
    }

    public void add() {
        Assert.assertEquals(1, 2);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();

        testSuite.addTest(new MathTest("ok"));// 该方法并不存在
        testSuite.addTest(new MathTest("add"));

        return testSuite;
    }

    public static void main(String[] args) {
        TestRunner.main("test.MathTest");
    }
}
