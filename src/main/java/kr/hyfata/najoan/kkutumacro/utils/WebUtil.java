package kr.hyfata.najoan.kkutumacro.utils;

import kr.hyfata.najoan.kkutumacro.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebUtil {
    public static String getStartWord() {
        return findTextByClassName("jjo-display");
    }

    public static String getCurrentRound() {
        return findTextByClassName("rounds-current");
    }

    public static String getPlayerID() {
        return findTextByClassName("my-stat-name");
    }

    public static String getMissionWord() {
        return findTextByClassName("items");
    }

    public static String getNowTurnPlayerID() {
        List<WebElement> nowPlayer = Main.getDriver().findElements(By.className("game-user-current"));
        if (nowPlayer.isEmpty()) {
            return null;
        }

        List<WebElement> playerID = nowPlayer.get(0).findElements(By.className("game-user-name"));
        if (playerID.isEmpty()) {
            return null;
        }

        String result = playerID.get(0).getText();
        return result.isEmpty() ? null : result;
    }

    public static String getLatestHistory() {
        List<WebElement> webElement = Main.getDriver().findElements(By.className("history-item"));
        if (!webElement.isEmpty()) {
            WebElement element = webElement.get(0);

            String parentText = (String) ((JavascriptExecutor) Main.getDriver()).executeScript(
                    "var parent = arguments[0];" +
                            "var childNodes = parent.childNodes;" +
                            "var text = '';" +
                            "for (var i = 0; i < childNodes.length; i++) {" +
                            "    if (childNodes[i].nodeType === Node.TEXT_NODE) {" +
                            "        text += childNodes[i].textContent.trim() + ' ';" +
                            "    }" +
                            "}" +
                            "return text.trim();", element);

            return parentText.trim();
        }
        return null;
    }

    private static String findTextByClassName(String className) {
        List<WebElement> webElements = Main.getDriver().findElements(By.className(className));

        if (webElements.isEmpty()) {
            return null;
        }

        String result = webElements.get(0).getText();
        return result.isEmpty() ? null : result;
    }

    public static void send(String message) {
        WebDriver driver = Main.getDriver();

        driver.findElement(By.cssSelector("#Talk")).sendKeys(message);
        WebElement chatButton = driver.findElement(By.cssSelector("#ChatBtn"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", chatButton);
    }
}
