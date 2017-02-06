package com.sdadas.spring2ts.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.sdadas.spring2ts.examples.utils.JsonUtils;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sławomir Dadas
 */
public class ClientTester implements Closeable {

    private int scriptsCount = 0;

    public void open(File file) {
        ChromeDriverManager.getInstance().setup();
        WebDriverRunner.setWebDriver(new ChromeDriver());
        Selenide.open(file.toURI().toString());
    }

    public void expect(String evalJs, String expectedResult) {
        String eval = String.format("expect(%s, %s)", evalJs, expectedResult);
        Selenide.executeJavaScript(eval);
        this.scriptsCount++;
    }

    public void expect(String evalJs, Object expectedObject) {
        String json = expectedObject != null ? JsonUtils.toJson(expectedObject) : "null";
        expect(evalJs, json);
    }

    public void assertValid() {
        if(Selenide.$$("h2").size() != scriptsCount) {
            List<String> logs = Selenide.getWebDriverLogs(LogType.BROWSER);
            Assert.fail("Not all scripts executed successfully:\n" + Joiner.on("\n").join(logs));
        }
        List<String> failed = Selenide.$$("h2.check-failed").stream()
                .map(SelenideElement::text)
                .collect(Collectors.toList());
        if(failed.size() > 0) {
            Assert.fail("Failed tests: " + Iterables.toString(failed));
        }
    }

    public void waitForReady() {
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), 30, 500);
        wait.until((WebDriver driver) -> {
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });
    }

    public void sleep(long milis) {
        Selenide.sleep(milis);
    }

    @Override
    public void close() throws IOException {
        Selenide.close();
    }
}
