package com.drol.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class QTool {

   public static boolean isRecordSetNull(List obj) {
      return obj == null || obj.size() == 0;
   }

   public static boolean isStrNull(String str) {
      return str == null || str.trim().length() == 0;
   }

   public static boolean isStrNull(StringBuffer str) {
      return str == null || str.length() == 0;
   }

   public static boolean isInStr(String str, String in) {
      if (str == null || in == null) return false;

      if (str.indexOf(',') != 0) {
         str = "," + str;
      }

      if (str.lastIndexOf(',') != str.length() - 1) {
         str = str + ",";
      }

      return str.indexOf("," + in + ",") >= 0;
   }

   public static boolean isInStrIgnoreCase(String str, String in) {
      if (str == null || in == null) return false;

      str = str.toLowerCase();
      in = in.toLowerCase();

      if (str.indexOf(',') != 0) {
         str = "," + str;
      }

      if (str.lastIndexOf(',') != str.length() - 1) {
         str = str + ",";
      }

      return str.indexOf("," + in + ",") >= 0;
   }

   public static void PutParamsTogether(StringBuffer sb, String sql, String param) {
      if (!QTool.isStrNull(param)) {
         if (!QTool.isStrNull(sb)) sb.append(" and ");

         if ("null".equalsIgnoreCase(param)) {
            sql = sql.replaceAll("[=]", " is ");
            sql = sql.replaceAll("[?]", "null");

         }
         else if ("notNull".equalsIgnoreCase(param)) {
            sql = sql.replaceAll("[=]", " is ");
            sql = sql.replaceAll("[?]", "not null");

         }
         else {
            sql = sql.replaceAll("[?]", param);
         }

         sb.append(sql);
      }
   }

   public static void PutParamsTogether(StringBuffer sb, String sql) {
      if (!QTool.isStrNull(sql)) {
         if (!QTool.isStrNull(sb)) sb.append(" and ");

         sb.append(sql);
      }
   }

   public static java.io.Serializable[] parseId(String[] ids) throws Exception {
      if (ids == null || ids.length == 0) { throw new Exception("非法操作！"); }

      java.io.Serializable id[] = null;
      try {
         id = new Integer[ids.length];
         for (int i = 0; i < ids.length; i++) {
            id[i] = new Integer(ids[i]);
         }
      }
      catch (Exception e) {
         id = ids;
      }
      return id;
   }

   public static String toStrDate(Object obj) {
      return toStrDate(obj, null);
   }

   public static String toStrDate(Object obj, String form) {
      String f = "yyyy-MM-dd";
      if (form != null && form.length() > 0) f = form;
      SimpleDateFormat sdf = new SimpleDateFormat(f);
      return obj != null ? sdf.format(obj) : "";
   }

   public static String toStr(Object obj) {
      return obj == null ? "" : obj.toString();
   }

   public static String toStr(Object obj, String encodeType) {
      String r = toStr(obj);
      encodeType = encodeType.trim();

      if ("xml".equalsIgnoreCase(encodeType)) r = encodeString4Xml(r);

      return r;
   }

   private static String encodeString4Xml(String s) {
      return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
   }

   public static Boolean isFirstDateGreater(String date1, String Date2) {
      try {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         Date dt1 = df.parse(date1);
         Date dt2 = df.parse(Date2);

         if (dt1.getTime() > dt2.getTime()) {
            return true;

         }
         else {
            return false;
         }

      }
      catch (Exception exception) {
         exception.printStackTrace();
      }
      return null;
   }

   public static String getRandomColorCode() {
      String color = "";
      String[] codeArray = new String[16];
      String[] AtoF = new String[] { "A", "B", "C", "D", "E", "F" };

      for (int i = 0; i < codeArray.length; i++)
         if (i <= 9) codeArray[i] = String.valueOf(i);
         else codeArray[i] = AtoF[i - 10];

      for (int i = 0; i < 6; i++)
         color += codeArray[getRandomNum(0, 16)];

      return color;
   }

   public static int getRandomNum(int left, int right) {
      int r = 0;
      Random random = new Random();

      if (left != right && left < right) {
         if (left >= 0 && right > 0) {
            r = random.nextInt(right - left) + left;
         }
         else if (left < 0 && right <= 0) {
            left = Math.abs(left);
            right = Math.abs(right);
            r = -1 * (random.nextInt(left - right) + right);
         }
         else {
            left = Math.abs(left);
            r = random.nextInt(right + left) - left;
         }
      }
      else {
         r = random.nextInt();
      }

      return r;
   }

   public static String newLine(String v, String... signs) {
      String r = v;
      for (String s : signs) {
         r = newLine(new StringBuffer(r), s, v.indexOf(s));
      }
      return r;
   }

   private static String newLine(StringBuffer sb, String sign, int i) {
      if (i != -1 && i != sb.length() - 1) return newLine(sb.insert(i + 1, "\n"), sign, sb.indexOf(sign, i + 1));
      else return sb.toString();
   }

   public static String newLine(String s, int n) {
      return newLine(new StringBuffer(s), n, 1);
   }

   private static String newLine(StringBuffer sb, int n, int lv) {
      int pos = n + lv - 1;
      if (pos < sb.length()) return newLine(sb.insert(pos, "\n"), n + n / lv, ++lv);
      else return sb.toString();
   }

   public static boolean isNumeric(String str) {
      if (str.trim().length() == 0) return false;
      if (str.indexOf("0") == 0 && str.indexOf(".") == -1 && str.trim().length() != 1) return false;
      if (str.indexOf(".") != -1 && str.indexOf(".") != str.lastIndexOf(".")) return false;
      if (str.lastIndexOf("-") != -1 && str.lastIndexOf("-") != 0) return false;
      if (str.indexOf(".") == 0) return false;

      String t = str.replace("-", "").replace(".", "");

      for (int i = t.length(); --i >= 0;) {
         if (!Character.isDigit(t.charAt(i))) { return false; }
      }
      return true;
   }

   public static void printDateTime(String info) {
      System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " -" + info);
   }

   public static void printDateTime() {
      System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
   }

   /**
    * 生成编号
    * 
    * @param prefix 前缀
    * @return
    */
   public static String getCodeNo(String prefix) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String nowdate = sdf.format(new Date());
      StringBuffer codeNo = new StringBuffer(prefix + nowdate);
      UUID uuid = UUID.randomUUID();
      codeNo.append(uuid.toString().split("-")[0]);
      return codeNo.toString().toUpperCase();
   }

   public static void openBrowse(String url) {
	   System.out.println(url);
	   System.out.println("");
		if (java.awt.Desktop.isDesktopSupported()) {  
            try {  
                // 创建一个URI实例  
                java.net.URI uri = java.net.URI.create(url);  
                // 获取当前系统桌面扩展  
                java.awt.Desktop dp = java.awt.Desktop.getDesktop() ;   
                // 判断系统桌面是否支持要执行的功能  
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {  
                    // 获取系统默认浏览器打开链接   
                    dp.browse( uri ) ;  
                }  
                  
                  
            } catch (Exception e) {   
                e.printStackTrace() ;  
            }  
        } 
   }
}
