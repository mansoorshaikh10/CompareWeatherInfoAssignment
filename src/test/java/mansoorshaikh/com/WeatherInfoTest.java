package mansoorshaikh.com;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class WeatherInfoTest extends Base {

    @Test
    public void validateCityPin() throws IOException {

        navigateToWeatherPage();

        selectCityCheckBox();

        WebElement cityText = getCityPinInfo("cityText");
        WebElement cityTempInDegree = getCityPinInfo("cityTempInDegree");
        WebElement cityTempInFahren = getCityPinInfo("cityTempInFahren");

        Assert.assertTrue(cityText.isDisplayed() && cityTempInDegree.isDisplayed() && cityTempInFahren.isDisplayed());
//        int tempInDegree = Integer.parseInt(cityTempInDegree.getText().replace("\u2103", ""));
//        int tempInFaren = Integer.parseInt(cityTempInFahren.getText().replace("\u2109", ""));
//        System.out.println(tempInDegree);
//        System.out.println(tempInFaren);

        closeDriver();
    }

    @Test
    public void validateCityOnMap() throws IOException {

        navigateToWeatherPage();

        validatingCityOnMap();

        closeDriver();

    }

    @Test
    public void compareWeatherInfo() throws IOException {
        Weather ndtvSource = getWeatherInfoNDTV();
        Weather APISource = getWeatherInfoAPI();

        double tempDegreeDifference = Math.round((Math.abs(ndtvSource.getTempInDegrees() - APISource.getTempInDegrees())) * 100.0) / 100.0;
        System.out.println(tempDegreeDifference);
        Assert.assertTrue( tempDegreeDifference <= Double.parseDouble(getProperty("TempDegreeVariance")));

    }

}
