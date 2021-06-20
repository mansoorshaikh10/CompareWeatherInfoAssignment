package mansoorshaikh.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class WeatherInfoTest extends Base {

    public WebDriver driver;

    @BeforeMethod
    public void initialize() throws IOException {

        driver = initializeDriver();
    }

    @Test
    public void validateCityPin() throws IOException {

        WeatherPage weatherPage = new WeatherPage(driver);
        weatherPage.navigateToWeatherPage();
        weatherPage.selectCityCheckBox();

        WebElement cityText = weatherPage.getCityPinInfo("cityText");
        WebElement cityTempInDegree = weatherPage.getCityPinInfo("cityTempInDegree");
        WebElement cityTempInFahren = weatherPage.getCityPinInfo("cityTempInFahren");

        Assert.assertTrue(!cityText.isDisplayed() && cityTempInDegree.isDisplayed() && cityTempInFahren.isDisplayed());
//        int tempInDegree = Integer.parseInt(cityTempInDegree.getText().replace("\u2103", ""));
//        int tempInFaren = Integer.parseInt(cityTempInFahren.getText().replace("\u2109", ""));
//        System.out.println(tempInDegree);
//        System.out.println(tempInFaren);

    }

    @Test
    public void validateCityOnMap() throws IOException {

        WeatherPage weatherPage = new WeatherPage(driver);
        weatherPage.navigateToWeatherPage();
        weatherPage.validatingCityOnMap();
    }

    @Test
    public void compareWeatherInfo() throws IOException {

        WeatherPage weatherPage = new WeatherPage(driver);
        Weather ndtvSource = weatherPage.getWeatherInfoNDTV();
        Weather APISource = weatherPage.getWeatherInfoAPI();

        double tempDegreeDifference = Math.round((Math.abs(ndtvSource.getTempInDegrees() - APISource.getTempInDegrees())) * 100.0) / 100.0;
        System.out.println(tempDegreeDifference);
        Assert.assertTrue( tempDegreeDifference <= Double.parseDouble(getDataProperty("TempDegreeVariance")));

    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

}
