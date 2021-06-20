package StepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import mansoorshaikh.com.WeatherPage;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;

@RunWith(Cucumber.class)
public class StepDefinitions extends WeatherPage {


    public StepDefinitions(WebDriver driver) {
        super(driver);
    }

    @Given("^User is on the NDTV Weather page$")
    public void user_is_on_the_ndtv_weather_page() throws IOException {
        //System.out.println("User on weather page");

    }

    @When("^User enters city name in the Pin Your City section$")
    public void user_enters_city_name_in_the_pin_your_city_section() throws IOException {
        //System.out.println("User pin the city");
        selectCityCheckBox();
    }

    @Then("^Temperature information is available for the city on the map$")
    public void temperature_information_is_available_for_the_city_on_the_map() throws IOException {
        //System.out.println("temp info available");
        WebElement cityText = getCityPinInfo("cityText");
        WebElement cityTempInDegree = getCityPinInfo("cityTempInDegree");
        WebElement cityTempInFahren = getCityPinInfo("cityTempInFahren");

        Assert.assertTrue(cityText.isDisplayed() && cityTempInDegree.isDisplayed() && cityTempInFahren.isDisplayed());
        int tempInDegree = Integer.parseInt(cityTempInDegree.getText().replace("\u2103", ""));
        int tempInFaren = Integer.parseInt(cityTempInFahren.getText().replace("\u2109", ""));
        System.out.println(tempInDegree);
        System.out.println(tempInFaren);

    }

    @When("User selects a city on the map")
    public void user_Selects_A_City_On_The_Map() throws IOException {

        driver.findElement(By.xpath("//div[@title='" + getLocatorsProperty("city") + "']")).click();

    }

    @Then("Weather details is available for the city on the map")
    public void weather_Details_Is_Available_For_The_City_On_The_Map() throws IOException {

        WebElement cityName = driver.findElement(By.xpath("//span[contains(text(),'" + getLocatorsProperty("city") + "')]"));
        WebElement cityCondition = getCityWeatherInfoNDTV("Condition");
        WebElement cityHumidity = getCityWeatherInfoNDTV("Humidity");
        WebElement cityTempInDegree = getCityWeatherInfoNDTV("Temp in Degrees");
        WebElement cityTempInFahren = getCityWeatherInfoNDTV("Temp in Fahrenheit");

        Assert.assertTrue(cityName.isDisplayed() && cityCondition.isDisplayed() && cityHumidity.isDisplayed() &&
                cityTempInDegree.isDisplayed() && cityTempInFahren.isDisplayed());

        System.out.println(getWeatherParaValue(cityHumidity));
        System.out.println(getWeatherParaValue(cityTempInDegree));
        System.out.println(getWeatherParaValue(cityTempInFahren));

    }
}
