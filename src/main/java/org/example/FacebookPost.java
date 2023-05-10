package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import org.slf4j.Logger;

public class FacebookPost {

    Logger logger = LoggerFactory.getLogger(FacebookPost.class);
    
    public void method() throws InterruptedException {
        ArrayList<String> listvid = new ArrayList<String>();
        String PostString = "This is a goofy ass post";



            // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver","C:\\Users\\vikto\\Downloads\\chromedriver_win32\\chromedriver.exe");

            // Configure the WebDriver to not accept announcements and popups
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

            // Create a new instance of the Chrome driver with the configured options
        WebDriver driver = new ChromeDriver(options);

            // Maximize the window
        driver.manage().window().maximize();

            // Navigate to Facebook
        driver.get("https://www.facebook.com");

            // Accept cookies
        try {
            WebElement acceptButton = (new WebDriverWait(driver, Duration.ofSeconds(2)))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-cookiebanner='accept_button']")));
            acceptButton.click();
        } catch (Exception e) {
            Thread.sleep(1000);
        }

        TimeUnit.SECONDS.sleep(1);
        try {
            WebElement acceptButton = (new WebDriverWait(driver, Duration.ofSeconds(1)))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-cookiebanner='accept_button']")));
            acceptButton.click();

        } catch (Exception e) {
            logger.error("The program did not find any cookies");
            Thread.sleep(1000);
        }

        logger.info("The has accepted cookies");

        String email = null;
        String password = null;

        try {
            File jsonFile = new File("C:\\Users\\vikto\\Documents\\Facebook.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            email = jsonNode.get("facebookCredentials").get("email").asText();
            password = jsonNode.get("facebookCredentials").get("password").asText();

        } catch (IOException e) {
            logger.error("You failed to log in");
            e.printStackTrace();
        }


        // Enter the username and password and click the login button
        WebElement usernameField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("pass"));
        WebElement loginButton = driver.findElement(By.name("login"));

        usernameField.sendKeys(email);
        passwordField.sendKeys(password);
        loginButton.click();


        TimeUnit.SECONDS.sleep(4);

            // Click on the "What's on your mind" element
        WebElement postButton = driver.findElement(By.xpath("//span[contains(text(), 'What')]"));
        postButton.click();

        TimeUnit.SECONDS.sleep(1);

            // Enter the text "hej" in the post textarea
        WebElement postTextArea = driver.findElement(By.xpath("//div[contains(@class, 'notranslate')][@role='textbox']"));
        postTextArea.sendKeys(PostString);

        TimeUnit.SECONDS.sleep(1);

            // Click on the "Post" button to make the post
        WebElement postButton2 = driver.findElement(By.xpath("//span[text()='Post']"));
        postButton2.click();

        TimeUnit.SECONDS.sleep(2);
        driver.get("https://www.facebook.com/profile.php");
        TimeUnit.SECONDS.sleep(3);
        WebElement post = driver.findElement(By.id("facebook"));
        TimeUnit.SECONDS.sleep(1);
        String latestPostText = post.getText();
        TimeUnit.SECONDS.sleep(1);

        String lines[] = latestPostText.split("\\r?\\n");
        int tot = 0;
        int place = 0;
        for (int i=0;i<lines.length;i++){
            if(lines[i].contains("Shared")){
                listvid.add(lines[i+1]);
                int eee = (listvid.size())-1;
            }
        }

        boolean equal = listvid.get(0).equals(PostString);

        if (equal) {
            logger.info("The strings are equal, both strings are: " + listvid.get(0));
        } else {
            logger.error("The strings are not equal: " + listvid.get(0) + " and " + PostString);
        }

        logger.info("The test is completed");

        }

    }

