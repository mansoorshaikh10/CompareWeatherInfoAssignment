package mansoorshaikh.com;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class Listeners extends Base implements ITestListener {

    ExtentTest extentTest;
    ExtentReports extent = ExtentReportsNG.getReportObject();
    ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        extentTest = extent.createTest(iTestResult.getMethod().getMethodName());
        test.set(extentTest);

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

        test.get().log(Status.PASS, "Test Passed");
    }


    @Override
    public void onTestFailure(ITestResult iTestResult) {

        test.get().fail(iTestResult.getThrowable());
        WebDriver driver = null;
        String testMethodName = iTestResult.getMethod().getMethodName();

        try {
            driver = (WebDriver) iTestResult.getTestClass().getRealClass().getDeclaredField("driver").get(iTestResult.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            test.get().addScreenCaptureFromPath(getScreenshotPath(testMethodName,driver), testMethodName);

        } catch (IOException e) {
            e.printStackTrace();
        }


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
