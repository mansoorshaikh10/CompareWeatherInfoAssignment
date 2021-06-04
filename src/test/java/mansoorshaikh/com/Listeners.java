package mansoorshaikh.com;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listeners extends Base implements ITestListener {

    ExtentReports extent = ExtentReportsNG.getReportObject();
    ExtentTest extentTest;

    @Override
    public void onTestStart(ITestResult iTestResult) {
        extentTest = extent.createTest(iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

        extentTest.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        extentTest.fail(iTestResult.getThrowable());

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

        extent.flush();
    }
}
