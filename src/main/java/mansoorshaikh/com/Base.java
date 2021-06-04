package mansoorshaikh.com;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {

    public WebDriver driver;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public String getProperty(String s) throws IOException{
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


        String browserName = getProperty("browser");

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

        WebDriverWait wait = new WebDriverWait(driver, 30);
        if (driver.findElement(By.linkText(getProperty("NoAlerts"))) != null) {
            WebElement alertNotifications = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText(getProperty("NoAlerts")))));
            if (alertNotifications.isEnabled()) {
                alertNotifications.click();
            }
        }
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("#" + getProperty("SubMenuHeaderID"))).click();
        driver.findElement(By.linkText(getProperty("WeatherLinkText"))).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#" + getProperty("LoadingID"))));

    }

    public WebElement getCityWeatherInfoNDTV(String parameter) throws IOException {

        return driver.findElement(By.xpath("//span[contains(text(),'" + getProperty("city") + "')]/parent::div/following-sibling::span/b[contains(text(),'" + parameter + "')]"));
    }

    public WebElement getCityPinInfo (String parameter) throws IOException {

        if (parameter.equalsIgnoreCase("cityText")) {
            return driver.findElement(By.xpath(String.format(getProperty("XcityText"), getProperty("city") , getProperty("city"))));
        } else if (parameter.equalsIgnoreCase("cityTempInDegree")) {
            return driver.findElement(By.xpath(String.format(getProperty("XcityTempInDegree"), getProperty("city"))));
        } else if (parameter.equalsIgnoreCase("cityTempInFahren")) {
            return driver.findElement(By.xpath(String.format(getProperty("XcityTempInFahren"), getProperty("city"))));
        } else {
            return null;
        }
    }

    public void validatingCityOnMap () throws IOException {

        driver.findElement(By.xpath("//div[@title='" + getProperty("city") + "']")).click();
        WebElement cityName = driver.findElement(By.xpath("//span[contains(text(),'" + getProperty("city") + "')]"));
        WebElement cityCondition = getCityWeatherInfoNDTV("Condition");
        WebElement cityHumidity = getCityWeatherInfoNDTV("Humidity");
        WebElement cityTempInDegree = getCityWeatherInfoNDTV("Temp in Degrees");
        WebElement cityTempInFahren = getCityWeatherInfoNDTV("Temp in Fahrenheit");

        Assert.assertTrue(cityName.isDisplayed() && cityCondition.isDisplayed() && cityHumidity.isDisplayed() &&
                cityTempInDegree.isDisplayed() && cityTempInFahren.isDisplayed());

//        System.out.println(humidity);
//        System.out.println(tempInDegree);
//        System.out.println(tempInFahren);
//        System.out.println(ndtvSource.getCondition());
    }

    public void selectCityCheckBox() throws IOException {

        driver.findElement(By.cssSelector("#" + getProperty("searchBoxID"))).clear();
        driver.findElement(By.cssSelector("#" + getProperty("searchBoxID"))).sendKeys(getProperty("city") + Keys.ENTER);
        WebElement checkBox = driver.findElement(By.xpath(String.format(getProperty("XcityCheckBox"), getProperty("city"))));
        if (!(checkBox.isSelected())) {
            checkBox.click();
        }
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
        driver.findElement(By.xpath(String.format(getProperty("XcityPin"), getProperty("city")))).click();
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
