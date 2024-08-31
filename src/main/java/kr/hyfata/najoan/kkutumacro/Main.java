package kr.hyfata.najoan.kkutumacro;

import com.formdev.flatlaf.IntelliJTheme;
import kr.hyfata.najoan.kkutumacro.gui.Design;
import kr.hyfata.najoan.kkutumacro.utils.WordUtil;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;

public class Main {
    public final static Logger LOG = LoggerFactory.getLogger(Main.class);
    private static Driver driver;
    private static HashSet<String> words;

    public static void main(String[] args) throws IOException {
        IntelliJTheme.setup(Main.class.getClassLoader().getResourceAsStream("theme/arc_theme_dark.theme.json"));
        new Design();
        LOG.info("Loading words...");
        words = WordUtil.getCSVData();
        LOG.info("Words loaded");
        LOG.info("Data count: {}", words.size());
    }

    public static void setDriver(String string, String url) {
        driver = new Driver(string, url);
    }

    public static WebDriver getDriver() {
        return driver.getDriver();
    }

    public static HashSet<String> getWords() {
        return words;
    }
}