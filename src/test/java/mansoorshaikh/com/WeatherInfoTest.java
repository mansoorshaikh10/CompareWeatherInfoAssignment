package mansoorshaikh.com;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class WeatherInfoTest extends Base {

    @Test
    public void validateCityPin() throws IOException {

        navigateToWeatherPage();
        driver.findElement(By.cssSelector("#searchBox")).clear();
        driver.findElement(By.cssSelector("#searchBox")).sendKeys(getProperty("city") + Keys.ENTER);
        WebElement checkBox = driver.findElement(By.xpath("//input[@class='defaultChecked'][@id='" + getProperty("city") +"']"));
        if (!(checkBox.isSelected())) {
            checkBox.click();
        }
        WebElement cityText = driver.findElement(By.xpath("//div[@title='" + getProperty("city") + "']" + "/div[text()='" + getProperty("city") + "']"));
        WebElement cityTempInDegree = driver.findElement(By.xpath("//div[@title='" + getProperty("city") + "']" + "/div[@class='temperatureContainer']/span[contains(text(),'℃')]"));
        WebElement cityTempInFahren = driver.findElement(By.xpath("//div[@title='" + getProperty("city") + "']" + "/div[@class='temperatureContainer']/span[contains(text(),'℉')]"));

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
