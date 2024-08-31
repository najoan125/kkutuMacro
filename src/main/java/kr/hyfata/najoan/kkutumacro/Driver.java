package kr.hyfata.najoan.kkutumacro;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.URL;
import java.nio.file.Paths;

public class Driver {
    private static WebDriver driver;

    // gecko or chrome
    public Driver(String driver, String url) {
        URL resource = this.getClass().getClassLoader().getResource("driver/" + driver + "driver.exe");
        if (resource == null) {
            System.out.println("Driver not found");
            throw new RuntimeException("Driver not found");
        }

        if (driver.equals("gecko")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setBinary(Paths.get(resource.getPath().substring(6)));
            Driver.driver = new FirefoxDriver();
        } else if (driver.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setBinary(Paths.get(resource.getPath().substring(6)).toFile());
            Driver.driver = new ChromeDriver();
        }
        Driver.driver.get(url);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
