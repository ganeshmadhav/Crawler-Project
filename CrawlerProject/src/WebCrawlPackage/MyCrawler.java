package WebCrawlPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
public class MyCrawler extends WebCrawler {
/*	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			 + "|png|mp3|mp3|zip|gz))$");
	*/
/*    private static final Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");*/
	//private	final static Pattern FILTERS=Pattern.compile(".*(\\.(css|js|bmp|gif|jpg|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"+"|rm|smil|wmv|swf|wma|zip|rar|gz|php|iso|ico))$");
	private	final static Pattern FILTERS=Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"+"|rm|smil|wmv|swf|wma|zip|rar|gz|php|iso|ico))$");
	 private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
				CrawlStat myCrawlStat;
				static int total_count = 0;
				static int unexpectedstatus_count = 0;
				static int bigprocess_count = 0;
				static int cf_count = 0;
				static int a_value = 0;
				static int b_value = 0;
				static int total_urls=0;
				static int s1 =0, s2=0,s3=0,s4=0,s5=0;
				static int img_count =0;
			    public MyCrawler() {
			        myCrawlStat = new CrawlStat();
			    }

			    static Map<String,Integer> unique_url = new HashMap<String,Integer>();
				static Map<String,Integer> in_url = new HashMap<String,Integer>();
				static Map<String,Integer> out_url = new HashMap<String,Integer>();
			 /**
			 * This method receives two parameters. The first parameter is the page
			 * in which we have discovered this new url and the second parameter is
			 * the new url. You should implement this function to specify whether
			 * the given url should be crawled or not (based on your crawling logic).
			 * In this example, we are instructing the crawler to ignore urls that
			 * have css, js, git, ... extensions and to only accept urls that start
			 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
			 * referringPage parameter to make the decision.
			 */
			 @Override
			
			 public boolean shouldVisit(Page referringPage, WebURL url) {
			 String href = url.getURL().toLowerCase();
			 String url_file = "urls_nydailynews.csv";
			 List<String> url_data = new ArrayList<String>();
			 String url_ref = href.replaceAll(",", "-");
			 url_data.add(url_ref);
			 total_urls+=1;
			 if(unique_url.containsKey(url_ref))
					unique_url.put(url_ref, unique_url.get(url_ref)+1);
				else 
					unique_url.put(url_ref, 1);
			 if(href.startsWith("http://www.nydailynews.com/") || href.startsWith("https://www.nydailynews.com/")){
					url_data.add("OK");
					if(in_url.containsKey(url_ref))
						in_url.put(url_ref, in_url.get(url_ref)+1);
					else 
						in_url.put(url_ref, 1);
				}else{
					url_data.add("N_OK");
					if(out_url.containsKey(url_ref))
						out_url.put(url_ref, out_url.get(url_ref)+1);
					else 
						out_url.put(url_ref, 1);
					}
			 writeCSV(url_file, url_data);
			 System.out.println("unique count: "+ unique_url.size());
			 System.out.println("in count: "+ in_url.size());
			 System.out.println("out count: "+ out_url.size());
			 System.out.println("total url count: "+ total_urls);
			 boolean visit = false;
			 if(!FILTERS.matcher(href).matches() && (href.startsWith("http://www.nydailynews.com/") || href.startsWith("https://www.nydailynews.com/"))){
				 try{
					 URL url_url = new URL(url.getURL());
				        HttpURLConnection connection = (HttpURLConnection)  url_url.openConnection();
				        connection.setRequestMethod("HEAD");
				        connection.connect();
				        String contentType = connection.getContentType();
				        
				        Pattern filter = Pattern.compile("text/html.*|image/.*|application/pdf.*");
						 
						visit = filter.matcher(contentType).matches();
				 } catch(IOException e){
					 System.out.println("Error in CsvFileWriter!");
			            e.printStackTrace();
				 }
			 }
			 return visit && (href.startsWith("http://www.nydailynews.com/") || href.startsWith("https://www.nydailynews.com/"));
			/* return !FILTERS.matcher(href).matches()
			 && (href.startsWith("http://www.nydailynews.com/") || href.startsWith("https://www.nydailynews.com/"));*/
			 }
			 
			 

			 /**
		     * This function is called once the header of a page is fetched. It can be
		     * overridden by sub-classes to perform custom logic for different status
		     * codes. For example, 404 pages can be logged, etc.
		     *
		     * @param webUrl WebUrl containing the statusCode
		     * @param statusCode Html Status Code number
		     * @param statusDescription Html Status COde description
		     */
			 @Override
			protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
				// TODO Auto-generated method stub
				String	href=webUrl.getURL().toLowerCase();
				String fetch_file = "fetch_nydailynews.csv";
				total_count++;
				System.out.println("Page Status Error PROCESS: "+href+"::"+statusCode+ " :: "+total_count);
				List<String> data = new ArrayList<String>();
				data.add(href.replaceAll(",", "-"));
				data.add(String.valueOf(statusCode));
				writeCSV(fetch_file,data);
				super.handlePageStatusCode(webUrl, statusCode, statusDescription);
			}

			 /**
		     * This function is called if the crawler encountered an unexpected http status code ( a
		     * status code other than 3xx)
		     *
		     * @param urlStr URL in which an unexpected error was encountered while crawling
		     * @param statusCode Html StatusCode
		     * @param contentType Type of Content
		     * @param description Error Description
		     */
			@Override
			protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType,
					String description) {
				// TODO Auto-generated method stub
				 	System.out.println("Unexpected PROCESS: "+urlStr+" :: "+statusCode+ " :: "+total_count);
				 	System.out.println("unexpected count:: "+ ++unexpectedstatus_count);
					List<String> data = new ArrayList<String>();
					/*String fetch_file = "fetch_nydailynews.csv";
					data.add(urlStr.replaceAll(",", "-"));
					data.add(String.valueOf(statusCode));
					writeCSV(fetch_file,data);*/
				super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
			}
			

			@Override
			protected WebURL handleUrlBeforeProcess(WebURL curURL) {
				// TODO Auto-generated method stub
				String href=curURL.getURL().toLowerCase();
				System.out.println("Before PROCESS: "+href);
				return super.handleUrlBeforeProcess(curURL);
			}


			@Override
			protected void onContentFetchError(WebURL webUrl) {
				// TODO Auto-generated method stub
				System.out.println("content fetch error count:: "+ ++cf_count);
				List<String> fetchData = new ArrayList<String>();
				String	href=webUrl.getURL().toLowerCase();
				
				String fetch_file = "fetch_nydailynews.csv";
//				fetchData.add(href.replaceAll(",", "-"));
//				fetchData.add(String.valueOf(0));
//				writeCSV(fetch_file,fetchData);
				System.out.println("Fetch Error PROCESS----> "+href);
				super.onContentFetchError(webUrl);
			}


			@Override
			protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
				// TODO Auto-generated method stub
				System.out.println("Big process count:: "+ ++bigprocess_count);
				System.out.println("Big PROCESS URL: "+urlStr);
				super.onPageBiggerThanMaxSize(urlStr, pageSize);
			}


			@Override
			protected void onParseError(WebURL webUrl) {
				// TODO Auto-generated method stub
				String	href=webUrl.getURL().toLowerCase();
				System.out.println("Parse Error PROCESS: "+href);
				super.onParseError(webUrl);
			}


			/**
			  * This function is called when a page is fetched and ready
			  * to be processed by your program.
			  */
			  @Override
			  public void visit(Page page) {
				  List<String> visitData = new ArrayList<String>();
				  String visit_file = "visit_nydailynews.csv";
				  logger.info("Visited: {}", page.getWebURL().getURL());
				  myCrawlStat.incProcessedPages();
				  String url = page.getWebURL().getURL();
				  System.out.println("URL: " + url);
				  a_value++;
				  visitData.add(url);
				  visitData.add(String.valueOf(page.getContentData().length));
				  int thispg = page.getContentData().length;
				  thispg/=1024;
				  if(thispg < 1){
					  s1+=1;
				  } else if(thispg >1 && thispg < 10){
					  s2+=1;
				  } else if(thispg >10 && thispg < 100){
					  s3+=1;
				  } else if(thispg > 100 && thispg < 1024){
					  s4+=1;
				  } else {
					  s5+=1;
				  }
				  if (imgPatterns.matcher(url).matches() ||
				            (page.getParseData() instanceof BinaryParseData)){
				            	img_count+=1;
				            	BinaryParseData parseData = (BinaryParseData) page.getParseData();
								Set<WebURL> links = parseData.getOutgoingUrls();
					            myCrawlStat.incTotalLinks(links.size());
					            b_value++;
					            visitData.add(String.valueOf(links.size()));
				            }
				  if (page.getParseData() instanceof HtmlParseData) {
						HtmlParseData parseData = (HtmlParseData) page.getParseData();
						Set<WebURL> links = parseData.getOutgoingUrls();
			            myCrawlStat.incTotalLinks(links.size());
			            b_value++;
			   		 	visitData.add(String.valueOf(links.size()));
			            try {
			                myCrawlStat.incTotalTextSize(parseData.getText().getBytes("UTF-8").length);
			            } catch (UnsupportedEncodingException ignored) {
			                // Do nothing
			            }
			        }
				  if(!(page.getParseData() instanceof HtmlParseData) && !(page.getParseData() instanceof BinaryParseData)){
					  ParseData  parseData = page.getParseData();
					  Set<WebURL> links = parseData.getOutgoingUrls();
			           myCrawlStat.incTotalLinks(links.size());
			           visitData.add(String.valueOf(links.size()));
				  }
				  	String contentType = page.getContentType();
				  	String[] w_charset=contentType.split(";");
		   		 	visitData.add(w_charset[0]);
		   		 	writeCSV(visit_file,visitData);
					System.out.println("unexpected count:: "+unexpectedstatus_count);
					System.out.println("big file count:: "+ bigprocess_count);
					System.out.println("cf count:: "+ cf_count);
					System.out.println("total count:: "+ total_count);
					System.out.println("a value: "+a_value+"b value: "+b_value);
					System.out.println("s1: "+s1+", s2: "+s2+", s3: "+s3+", s4: "+s4+", s5: "+s5+", img count: "+img_count);
			        // We dump this crawler statistics after processing every 50 pages
			        if ((myCrawlStat.getTotalProcessedPages() % 50) == 0) {
			            dumpMyData();
			        }

			  }
			  
			  /**
			     * This function is called by controller to get the local data of this crawler when job is
			     * finished
			     */
			    @Override
			    public Object getMyLocalData() {
			        return myCrawlStat;
			    }
			    
			    /**
			     * This function is called by controller before finishing the job.
			     * You can put whatever stuff you need here.
			     */
			    @Override
			    public void onBeforeExit() {
			        dumpMyData();
			    }
			    
			    public void dumpMyData() {
			        int id = getMyId();
			        // You can configure the log to output to file
			        logger.info("Crawler {} > Processed Pages: {}", id, myCrawlStat.getTotalProcessedPages());
			        logger.info("Crawler {} > Total Links Found: {}", id, myCrawlStat.getTotalLinks());
			        logger.info("Crawler {} > Total Text Size: {}", id, myCrawlStat.getTotalTextSize());
			    }
			    
			    public void writeCSV(String fileName, List<String> data){
			    	FileWriter fileWriter = null;
			    	
			    	final String COMMA_DELIMITER = ",";
			    	final String NEWLINE_DELIMITER = "\n";
			    	int Listsize = data.size();
			    	
			    	try{
			    		fileWriter = new FileWriter(fileName, true);
				    	for(int i = 0; i< Listsize; i++){
				    		fileWriter.append(data.get(i));
				    		if(i != Listsize-1){
				    			fileWriter.append(COMMA_DELIMITER);
				    		} else {
				    			fileWriter.append(NEWLINE_DELIMITER);
				    		}
				    	}
			    	} catch(Exception e){
			            System.out.println("Error in CsvFileWriter!");
			            e.printStackTrace();
			    	} finally{
			    		try {
							fileWriter.flush();
							fileWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("Error while flushing/closing!");
							e.printStackTrace();
						}
			    	}
			    	
			    }
}