package devparser;

import db.DbConnection;

import net.htmlparser.jericho.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import deviation.Deviation;
import deviant.Deviant;

import favorite.Favorite;
import utils.Date;

public class Parse {
  private DbConnection conn;

  public Parse() {
    conn = new DbConnection();
  }

  public void parse(String url, int offset) {
    putSpecificCategoryDeviations(url, offset);
    putSpecificCategoryFavoritesOfDeviations(0);
    conn.closeConn();
  }

  public void parse(int start) {
    putSpecificCategoryFavoritesOfDeviations(start);
    conn.closeConn();
  }

  private Source getSource(String url){
    Source source = null;

    try {
      URLConnection conn = new URL(url).openConnection();
      conn.setAllowUserInteraction(false);
      conn.setDoOutput(true);
      conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
      source = new Source(conn.getInputStream());
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    return source;
  }

  private void putSpecificCategoryFavoritesOfDeviations(int start) {

    for(Deviation d : conn.getAllDeviations(start)) {
      System.out.println(d.getDeviation_id());
      Source source = getSource(d.getDeviation_url());
      if(source == null) {
        System.out.println(d.getDeviation_url());
        continue;
      }
      for(Element e : source.getAllElementsByClass("a")) {
        String url = e.getAttributeValue("href");
        if(url.substring(url.length()-10).equals("favourites")) {
          int offset = 0;
          while(true) {
            int counter = 0;
            Element who = getSource(url + "?offset=" + offset).getElementById("theWho");
            List<Element> list = who.getAllElementsByClass("whoFaveRow even");
            list.addAll(who.getAllElementsByClass("whoFaveRow odd"));
            for(Element element : list) {
              counter++;
              TextExtractor text = element.getTextExtractor();
              for(StartTag st : element.getAllStartTags()) {
                text.excludeElement(st);
              }
              Matcher m = Pattern.compile("^(\\w+) (\\d+), (\\d+).*\\d\\. (.).*$")
                            .matcher(text.toString());

              Deviant deviant = new Deviant();
              utils.Date date;

              if(m.matches()) {
                date = new utils.Date();

                date.setMonth(getMonth(m.group(1)));
                date.setDay(Integer.parseInt(m.group(2)));
                date.setYear(Integer.parseInt(m.group(3)));
                deviant.setDeviant_type(getType(m.group(4).charAt(0)));
              } else {
                System.out.println("not match");
                continue;
              }
              String href = element.getFirstElementByClass("whoUserLink").getAttributeValue("href");
              deviant.setDeviant_name(href.substring(7, href.length()-15));
              conn.putDeviant(deviant);

              Favorite f = new Favorite();
              f.setDeviant_id(conn.getDeviant(deviant.getDeviant_name()).getDeviant_id());
              f.setDeviation_id(d.getDeviation_id());
              f.setFavorite_date(date);
              conn.putFavorite(f);
            }
            if(counter != 100) break;
            else offset += 100;
          }
        }
      }
    }
  }

  private void putSpecificCategoryDeviations(String categoryUrl, int offset) {
    while(true) {
      String url =  categoryUrl + "?order=5&offset=" + offset;
      Source source = getSource(url);
      while(source == null) {
        try {
          System.out.println("Sleep");
          Thread.sleep(30000);
          source = getSource(url);
        } catch (InterruptedException interEx) {}
      }
      System.out.println("URL: " + url);
      List<Element> elements = source.getFirstElementByClass("browse2-results")
        .getAllElementsByClass("tt-a");
      if(elements.size() == 0) break;
      else {
        processPage(elements);
        offset += elements.size();
      }
    }
  }

  private void processPage(List<Element> elements) {
    for(Element e : elements) {
      try {
        Deviant deviant = new Deviant();
        Deviation deviation = new Deviation();

        deviation.setDeviation_deviantart_id(Integer.parseInt(e
          .getAttributeValue("collect_rid").split(":")[1]));

        e = e.getFirstElementByClass("t");
        deviation.setDeviation_url(e.getAttributeValue("href"));
        deviant.setDeviant_name(new URL(deviation.getDeviation_url()).getHost()
          .substring(0, new URL(deviation.getDeviation_url()).getHost().length()-15));

        Matcher m =  Pattern.compile("^.* by (.*), (\\w+) (\\d+), (\\d+)")
          .matcher(e.getAttributeValue("title"));

        Date date = new Date();
        if(m.matches()) {
          deviant.setDeviant_type(getType(m.group(1).charAt(0)));

          date.setMonth(getMonth(m.group(2)));
          date.setDay(Integer.parseInt(m.group(3)));
          date.setYear(Integer.parseInt(m.group(4)));
        }            
        String[] dirs = new URL(deviation.getDeviation_url()).getPath().split("/");
        deviation.setDeviation_name(dirs[dirs.length-1]
          .substring(0, dirs[dirs.length-1].length()-Long.toString(deviation
            .getDeviation_deviantart_id()).length()-1));

        deviation.setDeviation_date(date);
        conn.putDeviant(deviant);

        deviation.setDeviation_deviant_id(conn.getDeviant(deviant
          .getDeviant_name()).getDeviant_id());
        conn.putDeviation(deviation);

      } catch (Exception ex) {
          System.out.println(ex.getMessage());
      }
    }
  }

  private short getType(char c) {
    switch(c) {
      case '`': return 1;
      case '~': return 2;
      case '=': return 3;
      case '*': return 4;
      default:
        System.out.println(c); return 0;
    }
  }

  private int getMonth(String s ) {

    if(s.equals("January") || s.equals("Jan")) return 0;
    else if(s.equals("February") || s.equals("Feb")) return 1;
    else if(s.equals("March") || s.equals("Mar")) return 2;
    else if(s.equals("April") || s.equals("Apr")) return 3;
    else if(s.equals("May") || s.equals("May")) return 4;
    else if(s.equals("June") || s.equals("Jun")) return 5;
    else if(s.equals("July") || s.equals("Jul")) return 6;
    else if(s.equals("August") || s.equals("Aug")) return 7;
    else if(s.equals("September") || s.equals("Sep")) return 8;
    else if(s.equals("October") || s.equals("Oct")) return 9;
    else if(s.equals("November") || s.equals("Nov")) return 10;
    else if(s.equals("December") || s.equals("Dec")) return 11;
    else {
      System.out.println(s);
      return 1;
    }
  }
}