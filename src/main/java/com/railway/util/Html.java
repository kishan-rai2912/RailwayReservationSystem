package com.railway.util;
public final class Html {private Html(){} public static String escape(Object x){if(x==null)return "";return x.toString().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");}}
