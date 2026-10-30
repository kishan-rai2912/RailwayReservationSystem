package com.railway.util;
import org.mindrot.jbcrypt.BCrypt;
public final class Passwords { private Passwords(){} public static String hash(String p){return BCrypt.hashpw(p,BCrypt.gensalt(12));} public static boolean verify(String p,String h){return h!=null && BCrypt.checkpw(p,h);} }
