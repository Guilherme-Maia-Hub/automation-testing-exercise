package mvnPackage;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class YoutubePages {

    private List<WebElement> listTrendingVideos;
    WebDriver driver;

    public void setCurrentDriver(WebDriver currentDriver) {
        this.driver = currentDriver;
    }

    public void setListOfTrendingVideos() {
        this.listTrendingVideos = driver.findElements(By.xpath("(//ytd-item-section-renderer)[1]//a[@id='video-title']"));
    }

    public void totalTrendingViews() {
        int[] arrayTEMP = getAllViewsFromList(listTrendingVideos);
        int totalNumberOfViews = 0;
        for (int i = 0 ; i < arrayTEMP.length; i++) {
            totalNumberOfViews += arrayTEMP[i];
        }
        System.out.println("\nTRENDING VIDEOS SUM OF VIEWS: " + totalNumberOfViews + "\n");
    }

    public void top5LongestVideos() {
        int[] arrayVideoDuration = new int[listTrendingVideos.size()];
        for (int i = 0 ; i < arrayVideoDuration.length; i++) {
            arrayVideoDuration[i] = getVideoDuration(listTrendingVideos.get(i));
        }

        int[] arrayVideoDurationSorted = (int[]) arrayVideoDuration.clone();
        Arrays.sort(arrayVideoDurationSorted);

        System.out.println("\nTOP 5 LONGEST VIDEOS:");
        for(int i=0;i<5;i++) {
            int index = getIndexFromIntArray( 
                            arrayVideoDuration,
                            arrayVideoDurationSorted[arrayVideoDurationSorted.length - (i+1)]);

            String videoDuration = getVideoDurationString(listTrendingVideos.get(index));

            System.out.println(
                "VIDEO DURATION: " 
               + videoDuration
               + " - VIDEO NAME: " 
               + listTrendingVideos.get(index).getAttribute("title")
               + " - VIDEO ID: "
               + listTrendingVideos.get(index).getAttribute("href").substring(32));
        }
    }


    private String getVideoDurationString(WebElement webElement) {
        String ariaLabelValue = webElement.getAttribute("aria-label");
        String[] splitAriaLabel = ariaLabelValue.split(" ");
   
        String videoDuration = "";
        for( int j = 0; j < splitAriaLabel.length ; j++){
            if(splitAriaLabel[j].contains("ago")) {
                for (int i = j+1; i < splitAriaLabel.length-2; i++) {
                    if (i == j+1) {
                        videoDuration += splitAriaLabel[i];
                    } else{
                        videoDuration += " " + splitAriaLabel[i];
                    }
                }
                break;
            }
        }
        return videoDuration;
    }

    public void top5MostViewedVideos() {
        int[] arrayVideoViews = getAllViewsFromList(listTrendingVideos);

        int[] arrayVideoViewsSorted = (int[]) arrayVideoViews.clone();
        Arrays.sort(arrayVideoViewsSorted);        

        System.out.println("\nTOP 5 MOST VIEWED VIDEOS:");
        for(int i=0;i<5;i++) {
            int index = getIndexFromIntArray( arrayVideoViews ,arrayVideoViewsSorted[arrayVideoViewsSorted.length - (i+1)]);
            System.out.println(
               "NUMBER OF VIEWS: " 
               + arrayVideoViewsSorted[arrayVideoViewsSorted.length - (i+1)]
               + " - VIDEO NAME: " 
               + listTrendingVideos.get(index).getAttribute("title")
               + " - VIDEO ID: "
               + listTrendingVideos.get(index).getAttribute("href").substring(32));
        }
    }

    private int[] getAllViewsFromList(List<WebElement> listTrendingVideos) {
        int[] arrayVideoViews = new int[listTrendingVideos.size()];
        for (int i = 0 ; i < arrayVideoViews.length; i++) {
            arrayVideoViews[i] = (int) getVideoViews(listTrendingVideos.get(i));
        }
        return arrayVideoViews;
    }

    public void checkNumberOfTrendingVideos() {
        Assert.assertEquals(listTrendingVideos.size(),50);
        System.out.println("\nTRENDING VIDEOS COUNT: " + listTrendingVideos.size());
    }

    public void openTrendingPage() {
        driver.findElement(By.xpath("//span[text()='Trending']")).click();
        String trendingPageURL = driver.getCurrentUrl();
        Assert.assertEquals(trendingPageURL, "https://www.youtube.com/feed/trending");
        setListOfTrendingVideos();
    }

    public void goToYoutubeHomepage() {
        driver.get("https://www.youtube.com/");
        String homepageURL = driver.getCurrentUrl();
        Assert.assertEquals(homepageURL, "https://www.youtube.com/");
        driver.findElement(By.xpath("//*[@id='text' and text()='No thanks']")).click(); // No Login
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='iframe']"))); // Enter cookies ifrmae
        driver.findElement(By.xpath("//span[text()='I agree']")).click(); // Accept cookies
        driver.switchTo().parentFrame(); // Leave to main iframe
    }

    private int getIndexFromIntArray(int[] valuesList,int value){
        int index=0;
         for(int i=0;i<valuesList.length;i++)
         {
             if(valuesList[i]==value)
             {
                 index=i;
                 break;
             }
         } 
         return index;
    }

    private int getVideoDuration(WebElement webElement) {
        String ariaLabelValue = webElement.getAttribute("aria-label");
        String[] splitAriaLabel = ariaLabelValue.split(" ");

        int videoDuration = 0;
        for( int i = splitAriaLabel.length-1; i > 0 ; i--){

            if(splitAriaLabel[i].contains("second")) {
                i--;
                int time = Integer.parseInt(splitAriaLabel[i]);
                videoDuration += time;

            } else if(splitAriaLabel[i].contains("minute")){
                i--;
                int time = Integer.parseInt(splitAriaLabel[i]);
                videoDuration += (time*60);
            } else if(splitAriaLabel[i].contains("hour")){
                i--;
                int time = Integer.parseInt(splitAriaLabel[i]);
                videoDuration += (time*60*60);
            } else if (splitAriaLabel[i].contains("ago")) {
                break;
            }
        
        }
        return videoDuration;
    }

    private int getVideoViews(WebElement webElement) {
        String ariaLabelValue = webElement.getAttribute("aria-label");
        String[] splitAriaLabel = ariaLabelValue.split(" ");
        String stringNumberOfViews = splitAriaLabel[splitAriaLabel.length-2];
        String numericVideoViews = stringNumberOfViews.replace(",","");
        int videoViews = Integer.parseInt(numericVideoViews);
        return videoViews;
    }
}
