package mansoorshaikh.com;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportsNG {

    static ExtentReports extent;

    public static ExtentReports getReportObject() {

        String path = System.getProperty("user.dir") + "\\Reports\\index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Automation Results");
        reporter.config().setDocumentTitle("Assignment Test Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Mansoor Shaikh");

        return extent;

    }
}
