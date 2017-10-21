package WebCrawlPackage;

import java.io.FileWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Controller {
	 private static final Logger logger =
		        LoggerFactory.getLogger(Controller.class);
	public static void main(String[] args) throws Exception {
		 String crawlStorageFolder = "/data/crawl";
		 FileWriter fileWriter = null;
		 int numberOfCrawlers = 7;
		 CrawlConfig config = new CrawlConfig();
		 config.setCrawlStorageFolder(crawlStorageFolder);
		 config.setMaxPagesToFetch(20000);
		 config.setMaxDepthOfCrawling(16);
		 config.setPolitenessDelay(150); 
		 config.setUserAgentString("USC viterbi");
		 /*
		 * Instantiate the controller for this crawl.
		 */
		 PageFetcher pageFetcher = new PageFetcher(config);
		 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		 robotstxtConfig.setEnabled(false);
		 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		 /*
			 * Since images are binary content, we need to set this parameter to
			 * true to make sure they are included in the crawl.
			 */
			config.setIncludeBinaryContentInCrawling(true);
		 /*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		 controller.addSeed("http://www.nydailynews.com/");
		 /*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		 fileWriter = new FileWriter("fetch_nydailynews.csv",false);
		 fileWriter.append("URL,STATUS CODE\n"); 
		 fileWriter.close();
		 fileWriter = new FileWriter("visit_nydailynews.csv",false);
		 fileWriter.append("URL,SIZE,No of Outlinks,Content-Type\n");
		 fileWriter.close();
		 fileWriter = new FileWriter("urls_nydailynews", false);
		 fileWriter.append("URL,Indicator\n");
		 fileWriter.close();
		 controller.start(MyCrawler.class, numberOfCrawlers);
		  List<Object> crawlersLocalData = controller.getCrawlersLocalData();
	        long totalLinks = 0;
	        long totalTextSize = 0;
	        int totalProcessedPages = 0;
	        for (Object localData : crawlersLocalData) {
	            CrawlStat stat = (CrawlStat) localData;
	            totalLinks += stat.getTotalLinks();
	            totalTextSize += stat.getTotalTextSize();
	            totalProcessedPages += stat.getTotalProcessedPages();
	        }

	        logger.info("Aggregated Statistics:");
	        logger.info("\tProcessed Pages: {}", totalProcessedPages);
	        logger.info("\tTotal Links found: {}", totalLinks);
	        logger.info("\tTotal Text Size: {}", totalTextSize);
	    
		 }
}
