package mansoorshaikh.com;

public class Weather {

    private double tempInFahren;
    private double humidity;
    private double tempInDegrees;


    public Weather(double humidity, double tempInDegrees, double tempInFahren) {
        this.humidity = humidity;
        this.tempInDegrees = tempInDegrees;
        this.tempInFahren = tempInFahren;
    }

    public double getTempInFahren() {
        return tempInFahren;
    }

    public void setTempInFahren(double tempInFahren) {
        this.tempInFahren = tempInFahren;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTempInDegrees() {
        return tempInDegrees;
    }

    public void setTempInDegrees(double tempInDegrees) {
        this.tempInDegrees = tempInDegrees;
    }
}
