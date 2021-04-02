package mansoorshaikh.com;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {

    public WebDriver driver;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public String getProperty(String s) throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\shaikm\\IdeaProjects\\CompareWeatherReporting\\src\\main\\java\\mansoorshaikh\\com\\data.properties");
        properties.load(fileInputStream);
        fileInputStream.close();
        return properties.getProperty(s);
    }

    public WebDriver initializeDriver() throws IOException {


        String browserName = getProperty("browser");

        if (browserName.equalsIgnoreCase("Chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32\\chromedriver.exe");
            driver = new ChromeDriver();

        } else if (browserName.equalsIgnoreCase("Firefox")) {
            System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver-v0.29.0-win64\\geckodriver.exe");
            driver = new FirefoxDriver();

        } else if (browserName.equalsIgnoreCase("Edge")) {
            System.setProperty("webdriver.edge.driver", "C:\\Selenium\\edgedriver_win64\\msedgedriver.exe");
            driver = new EdgeDriver();

        } else if (browserName.equalsIgnoreCase("IE")) {
            System.setProperty("webdriver.ie.driver", "C:\\Selenium\\IEDriverServer_x64_3.141.59\\IEDriverServer.exe");
            driver = new InternetExplorerDriver();

        } else {
            return null;
        }

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        return driver;

    }

    public void closeDriver() {
        driver.quit();
    }

    public void navigateToWeatherPage() throws IOException {
        driver = initializeDriver();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        try {
            driver.get(getProperty("url"));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        WebDriverWait wait = new WebDriverWait(driver, 20);
        if (driver.findElement(By.linkText("No Thanks")) != null) {
            WebElement adNotification = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText("No Thanks"))));
            if (adNotification.isEnabled()) {
                adNotification.click();
            }
        }
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("#h_sub_menu")).click();
        driver.findElement(By.linkText("WEATHER")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loading']")));

    }

    public WebElement getCityWeatherInfoNDTV(String parameter) throws IOException {

        return driver.findElement(By.xpath("//span[contains(text(),'" + getProperty("city") +"')]/parent::div/following-sibling::span/b[contains(text(),'" + parameter + "')]"));
    }

    public double getWeatherParaValue(WebElement webElement) {

        if (webElement.getText().contains("Humidity")) {
            String humidity = (webElement.getText()).substring((webElement.getText()).lastIndexOf(":") + 2);
            humidity = humidity.replaceAll("%", "");
            return Double.parseDouble(humidity);
        } else if (webElement.getText().contains("Degrees")) {
            return Double.parseDouble((webElement.getText()).substring((webElement.getText().lastIndexOf(":") + 2)));
        } else if (webElement.getText().contains("Fahrenheit")) {
            return Double.parseDouble((webElement.getText()).substring((webElement.getText().lastIndexOf(":") + 2)));
        }

        return -1.0;
    }

    public Weather getWeatherInfoNDTV() throws IOException {

        navigateToWeatherPage();
        driver.findElement(By.xpath("//div[@title='" + getProperty("city") + "']")).click();
        WebElement cityHumidity = getCityWeatherInfoNDTV("Humidity");
        WebElement cityTempInDegree = getCityWeatherInfoNDTV("Temp in Degrees");
        WebElement cityTempInFahren = getCityWeatherInfoNDTV("Temp in Fahrenheit");

        double humidity = getWeatherParaValue(cityHumidity);
        double tempInDegree = getWeatherParaValue(cityTempInDegree);
        double tempInFahren = getWeatherParaValue(cityTempInFahren);

        System.out.println(tempInDegree);
        closeDriver();
        return new Weather(humidity, tempInDegree, tempInFahren);

    }

    public Weather getWeatherInfoAPI() throws IOException {

        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5/weather";
        RequestSpecification request = RestAssured.given();

        Response response = request.queryParam("q", getProperty("city")).
                queryParam("appid", "7fe67bf08c80ded756e598d6f8fedaea").queryParam("units", "metric").get();
        JsonPath jsonPath = response.jsonPath();

        df.setRoundingMode(RoundingMode.UP);

        int humidityAPI = jsonPath.get("main.humidity");
        float tempAPI =  jsonPath.get("main.temp");
        double tempInFahrenAPI = Double.parseDouble(df.format((( tempAPI * 9)/5)+32));

        System.out.println(tempAPI);
        System.out.println(tempInFahrenAPI);
        return new Weather(humidityAPI, tempAPI, tempInFahrenAPI);

    }
}
