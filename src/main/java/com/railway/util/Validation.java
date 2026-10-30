package com.railway.util;
import java.time.LocalDate;import java.util.regex.Pattern;
public final class Validation {private Validation(){} private static final Pattern EMAIL=Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
 public static boolean email(String s){return s!=null&&EMAIL.matcher(s.trim()).matches()&&s.length()<=190;}
 public static boolean phone(String s){return s!=null&&s.matches("[+0-9() -]{7,20}");}
 public static boolean name(String s){return s!=null&&s.trim().matches("[\\p{L}][\\p{L} .'-]{1,98}");}
 public static boolean journeyDate(String s){try{return !LocalDate.parse(s).isBefore(LocalDate.now());}catch(Exception e){return false;}}
 public static boolean age(String s){try{int a=Integer.parseInt(s);return a>=0&&a<=120;}catch(Exception e){return false;}}
}
