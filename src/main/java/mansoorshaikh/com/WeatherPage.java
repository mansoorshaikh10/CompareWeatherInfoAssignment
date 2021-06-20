package mansoorshaikh.com;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class WeatherPage extends Base{

    public WebDriver driver;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public WeatherPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToWeatherPage() throws IOException {

//      driver = initializeDriver();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        try {
            driver.get(getDataProperty("url"));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        WebDriverWait wait = new WebDriverWait(driver, 30);
        if ((driver.findElements(By.linkText(getLocatorsProperty("NoAlerts")))).size() > 0) {
            WebElement alertNotifications = driver.findElement(By.linkText(getLocatorsProperty("NoAlerts")));
            alertNotifications.click();
        }
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("#" + getLocatorsProperty("SubMenuHeaderID"))).click();
        driver.findElement(By.linkText(getLocatorsProperty("WeatherLinkText"))).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#" + getLocatorsProperty("LoadingID"))));

    }

    public WebElement getCityWeatherInfoNDTV(String parameter) throws IOException {

        return driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityWeatherParameters"), getDataProperty("city"), parameter)));
    }

    public WebElement getCityPinInfo (String parameter) throws IOException {

        if (parameter.equalsIgnoreCase("cityText")) {
            return driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityText"), getDataProperty("city") , getDataProperty("city"))));
        } else if (parameter.equalsIgnoreCase("cityTempInDegree")) {
            return driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityTempInDegree"), getDataProperty("city"))));
        } else if (parameter.equalsIgnoreCase("cityTempInFahren")) {
            return driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityTempInFahren"), getDataProperty("city"))));
        } else {
            return null;
        }
    }

    public void validatingCityOnMap () throws IOException {

        driver.findElement(By.xpath("//div[@title='" + getDataProperty("city") + "']")).click();
        WebElement cityName = driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityName"), getDataProperty("city"))));
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

        driver.findElement(By.cssSelector("#" + getLocatorsProperty("searchBoxID"))).clear();
        driver.findElement(By.cssSelector("#" + getLocatorsProperty("searchBoxID"))).sendKeys(getDataProperty("city") + Keys.ENTER);
        WebElement checkBox = driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityCheckBox"), getDataProperty("city"))));
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
        driver.findElement(By.xpath(String.format(getLocatorsProperty("XcityPin"), getDataProperty("city")))).click();
        WebElement cityHumidity = getCityWeatherInfoNDTV("Humidity");
        WebElement cityTempInDegree = getCityWeatherInfoNDTV("Temp in Degrees");
        WebElement cityTempInFahren = getCityWeatherInfoNDTV("Temp in Fahrenheit");

        double humidity = getWeatherParaValue(cityHumidity);
        double tempInDegree = getWeatherParaValue(cityTempInDegree);
        double tempInFahren = getWeatherParaValue(cityTempInFahren);

        System.out.println(tempInDegree);
        return new Weather(humidity, tempInDegree, tempInFahren);

    }

    public Weather getWeatherInfoAPI() throws IOException {

        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5/weather";
        RequestSpecification request = RestAssured.given();

        Response response = request.queryParam("q", getDataProperty("city")).
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
