package kr.hyfata.najoan.kkutumacro.handler;

import kr.hyfata.najoan.kkutumacro.Main;
import kr.hyfata.najoan.kkutumacro.gui.Design;
import kr.hyfata.najoan.kkutumacro.handler.dto.Count;
import kr.hyfata.najoan.kkutumacro.handler.dto.Round;
import kr.hyfata.najoan.kkutumacro.utils.WebUtil;
import kr.hyfata.najoan.kkutumacro.utils.WordUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Handler {
    private static ScheduledExecutorService executorService;
    private static String ID = "", temp = "", temp2 = "";
    private static boolean tempReady = false;

    public static void start(int delay) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(Handler::auto, 0, delay, TimeUnit.MILLISECONDS);
    }

    public static void stop() {
        executorService.shutdown();
        ID = "";
        temp = "";
        temp2 = "";
        tempReady = false;
    }

    private static void auto() {
        try {
            Design.count.setText(Count.getCount() + "");
            // getting player's id
            String id;
            if (ID.isEmpty() && (id = getID()) != null) {
                ID = id;
                Main.LOG.info("kkutuId found: \"{}\"", ID);
            }

            // get current round
            String round;
            if ((round = WebUtil.getCurrentRound()) != null) {
                Round.setCurrentRound(round);
                Design.round.setText(round);
            }

            // get history
            String history;
            if ((history = WebUtil.getLatestHistory()) != null) {
                WordUtil.addExcludedWord(history);
            }

            // attack
            List<WebElement> nowTurnPlayerID = getNowTurnPlayerID();
            if (nowTurnPlayerID != null && !nowTurnPlayerID.isEmpty()) {
                String playerName = nowTurnPlayerID.get(0).getText();
                attack(playerName);
            } else {
                CountHandler.turnEnd();
            }
        } catch (UnhandledAlertException ex) {
            Main.LOG.warn("Alert opened! it will pause macro for 5 seconds!");
            handleAlert();
        } catch (StaleElementReferenceException ex) {
            Main.LOG.error("DOM was changed! This is not bug!");
        } catch (Exception e) {
            Main.LOG.error("Exception occurred: ", e);
        }
    }

    private static String getID() {
        List<WebElement> playerId = Main.getDriver().findElements(By.className("my-stat-name"));
        if (!playerId.isEmpty() && !playerId.get(0).getText().isEmpty()) {
            return playerId.get(0).getText();
        }
        return null;
    }

    private static List<WebElement> getNowTurnPlayerID() {
        List<WebElement> nowPlayer = Main.getDriver().findElements(By.className("game-user-current"));
        if (nowPlayer.isEmpty()) {
            return null;
        }
        return nowPlayer.get(0).findElements(By.className("game-user-name"));
    }

    private static void attack(String playerName) {
        if (!playerName.isEmpty() && playerName.equals(ID)) {
            CountHandler.turnStart();
            String start = WebUtil.getStartWord();
            String start2 = null;
            String regex = "^.|.\\(.\\)$";
            if (start != null && start.matches(regex)) {
                if (start.contains("(")) {
                    start2 = start.split("\\(")[1].replace(")", "");
                    start = start.split("\\(")[0];
                }
                tempReady = true;
                temp = start;
                temp2 = start2;
                Attack.attack(temp, temp2);
            } else if (tempReady) {
                Attack.attack(temp, temp2);
            }
        } else {
            tempReady = false;
            CountHandler.turnEnd();
        }
    }

    private static void handleAlert() {
        new WebDriverWait(Main.getDriver(), Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
        Alert alert = Main.getDriver().switchTo().alert();
        String alertText = alert.getText();
        Main.LOG.info("Alert detected: {}", alertText);
        alert.accept();
        Main.LOG.info("Alert accepted");
    }
}
