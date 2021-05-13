package com.project.webcrawler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerBrowserTest {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate rest;

    @BeforeClass
    public static void setup() {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void testCrawling_HappyPath() {
        browser.get(homePageUrl());
        checkQueriesSelect();
        fillSeedAndTermsClickCrawl();
        ensureOutputNotEmpty();
    }

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private void checkQueriesSelect() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        WebDriverWait webDriverWait = new WebDriverWait(browser, 30);
        webDriverWait.until(ExpectedConditions.numberOfElementsToBe(By.name("queryOption"), 2));

    }
    private void fillSeedAndTermsClickCrawl() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementById("inputSeed").sendKeys("http://www.example.com");
        browser.findElementById("addTermButton").click();
        browser.findElementById("addTermButton").click();
        browser.findElementById("termInput-1").sendKeys("domain");
        browser.findElementById("termInput-2").sendKeys("permission");
        browser.findElementById("crawlButton").click();
    }

    private void ensureOutputNotEmpty() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            fail();
        }
        Pattern pattern = Pattern.compile(".+");
        Matcher matcher = pattern.matcher(browser.findElementById("output").getAttribute("value"));
        assertTrue(matcher.matches());
    }
}
