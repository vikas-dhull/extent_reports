package com.test.sample;

import java.lang.reflect.Method;

import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


public class BaseTest extends TestListenerAdapter {
	private ThreadLocal<ExtentTest> parentTest;
	private ThreadLocal<ExtentTest> test;

	@BeforeClass
	public synchronized void beforeClass() {
		ExtentTest parent = ExtentTestManager.createTest(getClass().getName());
		parentTest.set(parent);
	}

	@BeforeMethod
	public synchronized void beforeMethod(Method method) {
		ExtentTest child = parentTest.get().createNode(method.getName());
		test.set(child);
	}

	@AfterMethod
	public synchronized void afterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE)
			test.get().fail(result.getThrowable());
		else if (result.getStatus() == ITestResult.SKIP)
			test.get().skip(result.getThrowable());
		else
			test.get().pass("Test passed");

		ExtentManager.getExtent().flush();
	}
}