package org.example.drivers;


public class DriverFactory {
    private DriverFactory(){
        super();
    }
    public static WebDriverInterface getDriver(String browserName) {
        return switch (browserName.toLowerCase()) {
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            case "edge" -> new EdgeDriver();
            case "android"->new AndroidDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }


}
