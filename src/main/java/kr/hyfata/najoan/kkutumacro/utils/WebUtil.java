package kr.hyfata.najoan.kkutumacro.utils;

import kr.hyfata.najoan.kkutumacro.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    public static String getNowTurnPlayerID() {
        List<WebElement> nowPlayer = Main.getDriver().findElements(By.className("game-user-current"));
        if (!nowPlayer.isEmpty()) {
            List<WebElement> playerID = nowPlayer.get(0).findElements(By.className("game-user-name"));
            if (!playerID.isEmpty() && !playerID.get(0).getText().isEmpty()) {
                return playerID.get(0).getText();
            }
        }
        return null;
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
        List<WebElement> webElement = Main.getDriver().findElements(By.className(className));
        if (!webElement.isEmpty() && !webElement.get(0).getText().isEmpty()) {
            return webElement.get(0).getText();
        }
        return null;
    }

    public static void send(String message) {
        Main.getDriver().findElement(By.xpath("//*[@id=\"Talk\"]")).sendKeys(message);
        WebElement element = Main.getDriver().findElement(By.xpath("//*[@id=\"ChatBtn\"]"));
        JavascriptExecutor js = (JavascriptExecutor) Main.getDriver();
        js.executeScript("arguments[0].click();", element);
    }
}
