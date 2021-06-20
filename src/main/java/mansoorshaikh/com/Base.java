package mansoorshaikh.com;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {

    public WebDriver driver;

    public String getLocatorsProperty(String s) throws IOException {
        Properties properties = new Properties();

        String fileName = "locator.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            }
        }
        return properties.getProperty(s);
    }

    public String getDataProperty(String s) throws IOException {
        Properties properties = new Properties();

        String fileName = "data.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            }
        }
        return properties.getProperty(s);
    }

    public WebDriver initializeDriver() throws IOException {


        String browserName = getDataProperty("browser");

        if (browserName.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();

        } else if (browserName.equalsIgnoreCase("Firefox")) {
            //System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver-v0.29.0-win64\\geckodriver.exe");
            driver = new FirefoxDriver();

        } else if (browserName.equalsIgnoreCase("Edge")) {
            //System.setProperty("webdriver.edge.driver", "C:\\Selenium\\edgedriver_win64\\msedgedriver.exe");
            driver = new EdgeDriver();

        } else if (browserName.equalsIgnoreCase("IE")) {
            //System.setProperty("webdriver.ie.driver", "C:\\Selenium\\IEDriverServer_x64_3.141.59\\IEDriverServer.exe");
            driver = new InternetExplorerDriver();

        } else {
            return null;
        }

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        return driver;

    }

    public String getScreenshotPath(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destinationFile = System.getProperty("user.dir") + "\\Reports\\" + testCaseName + ".png";
        FileUtils.copyFile(source, new File(destinationFile));

        return destinationFile;
    }

//    public void closeDriver() {
//        driver.quit();
//    }

}
