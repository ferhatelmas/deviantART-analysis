package utils;

import com.sun.javaws.exceptions.CacheAccessException;

import java.util.Calendar;

public class Date {

  private int year;
  private int month;
  private int day;

  public static long DAY = 1000*60*60*24;

  public Date() {
  }

  public Date(java.sql.Date date) {
    Calendar c = Calendar.getInstance();

    c.setTimeInMillis(date.getTime());

    this.year = c.get(Calendar.YEAR);
    this.month = c.get(Calendar.MONTH);
    this.day = c.get(Calendar.DATE);
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public java.sql.Date convert() {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(year, month, day);
    return new java.sql.Date(c.getTimeInMillis());
  }

  public static int getDifference(java.sql.Date d1, java.sql.Date d2) {
    return (int)((d2.getTime()-d1.getTime())/DAY);
  }
}