package mvnPackage;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class AutomationExerciseTest {

    @Test
    public void youtubeTest() {

        // Driver Setup
        WebDriver driver = setWebDriver();

        // Set youtubePages
        YoutubePages youtubePages = new YoutubePages(); 
        youtubePages.setCurrentDriver(driver);

        testConsoleIntro();

        // Step 1 - Go to youtube.com
        youtubePages.goToYoutubeHomepage();

        // Step 2 - Open Trending videos
        youtubePages.openTrendingPage();

        // Step 3 -  Assert that there are exectly 50 videos in top section of the list
        youtubePages.checkNumberOfTrendingVideos();

        // Step 4 - Find the 5 most viewed videos and log them in the console (number of views - videos name - video ID)
        youtubePages.top5MostViewedVideos();

        // Step 5 - Find the 5 longest videos and log them in the console (lenght - video name - video ID)
        youtubePages.top5LongestVideos();

        // Step 6 - Sum the number of views for all 50 videos and log it in the console
        youtubePages.totalTrendingViews();

        testConsoleOutro();
        driver.close();
    }

    private WebDriver setWebDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }

    private static void testConsoleIntro() {
        System.out.println("\n----------- TEST RESULTS -----------");
    }

    private static void testConsoleOutro() {
        System.out.println("---------- TEST FINISHED -----------\n");
    }

}
