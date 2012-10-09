package category;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CategoryCrawler {

  private static ArrayList<String> seen = new ArrayList<String>();
  private static ArrayList<String> toSeen = new ArrayList<String>();

  public static void main(String[] args) throws Exception {

    for(Element e : getSource("http://browse.deviantart.com")
      .getElementById("facetMenu-Category_list").getAllElements("li")) {
      toSeen.add(e.getFirstElement("a").getAttributeValue("href"));
    }

    crawl();

    System.out.println("+++++ Final List +++++");
    for(String path : seen) {
      System.out.println(path);
    }
  }

  public static void crawl() {

    if(toSeen.size() == 0) return;
    else {
      String url = toSeen.get(0);
      toSeen.remove(0);
      seen.add(url);
      System.out.println(url + " is seen!");
            
      for(Element e : getSource(url).getElementById("facetMenu-Category_list").getAllElements("li")) {
        String href = e.getFirstElement("a").getAttributeValue("href");
        if(!seen.contains(href)) toSeen.add(href);
      }
      crawl();
    }
  }

  private static Source getSource(String url){
    Source source = null;
    try {
      URLConnection conn = new URL(url).openConnection();
      conn.setAllowUserInteraction(false);
      conn.setDoOutput(true);
      conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
      source = new Source(conn.getInputStream());
    } catch (IOException ex) { }
    return source;
  }
}