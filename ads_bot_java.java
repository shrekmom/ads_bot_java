import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.StandardCopyOption;
import java.io.*;
import java.util.*;
import java.util.HashSet;

class ads_bot_java {
  public static void main(String[] args) throws Exception {
    String ip1 = "127.0.0.1";
    String ip2 = "0.0.0.0";

    String local = "localhost";
    String thing = "::1";

    String smodge = "ads_list.txt";

    cleardir();
    downloadFiles();
    combineFiles();
    cleardir();
    String ad_str = convertString();
    String ad_str_imrpv = removeunwanted(ad_str, ip1, ip2, local, thing);
    finallist(ad_str_imrpv);
    removeduplicate();
    deletefile(smodge);
  }

  public static void deletefile(String x){
    File myObj = new File(x); 
    myObj.delete();
  }

  public static void cleardir() {
    File directory = new File("text_files");

    for (File file : Objects.requireNonNull(directory.listFiles())) {
      if (!file.isDirectory()) {
        file.delete();
      }
    }
  }

  public static void finallist(String x) throws Exception {
    try (PrintWriter out = new PrintWriter("ads_list.txt")) {
      out.println(x);
    }
  }

  public static void combineFiles() throws IOException {
    File dir = new File("text_files");

    try (PrintWriter pw = new PrintWriter("ads_list.txt")) {
      String[] fileNames = dir.list();

      for (String fileName : fileNames) {

        File f = new File(dir, fileName);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
          String line = br.readLine();
          while (line != null) {

            pw.println(line);
            line = br.readLine();
          }
        }

        pw.flush();
      }
    }
  }

  public static String removeunwanted(String x, String y, String z, String a, String b) {

    x = x.replaceAll("(?m)^#.*", "");
    x = x.replaceAll(y, "");
    x = x.replaceAll(z, "");
    x = x.replaceAll(a, "");
    x = x.replaceAll(b, "");
    String t = x.replaceAll("(?m)^[ \t]*\r?\n", "");

    return t;
  }

  public static String convertString() {
    Path path = Paths.get("ads_list.txt");

    byte[] bytes = null;
    try {
      bytes = Files.readAllBytes(path);
    } catch (IOException ex) {
    }
    String varlist = new String(bytes, StandardCharsets.UTF_8);
    return varlist;
  }

  public static void removeduplicate() throws IOException {

    PrintWriter pw = new PrintWriter("ads_final_list.txt");

    BufferedReader br = new BufferedReader(new FileReader("ads_list.txt"));

    String line = br.readLine();

    HashSet<String> hs = new HashSet<String>();

    while (line != null) {

      if (hs.add(line))
        pw.println(line);

      line = br.readLine();

    }

    pw.flush();
    br.close();
    pw.close();
  }

  public static void downloadFiles() throws Exception {
    FileInputStream fis = null;
    BufferedReader reader = null;

    try {
      fis = new FileInputStream("links.txt");
      reader = new BufferedReader(new InputStreamReader(fis));

      int linenum = 1;

      String line = reader.readLine();
      while (line != null) {
        String file = "text_files\\" + linenum + ".txt";
        InputStream in = new URL(line).openStream();
        Files.copy(in, Paths.get(file), StandardCopyOption.REPLACE_EXISTING);
        line = reader.readLine();
        linenum++;
      }

    } catch (FileNotFoundException ex) {
      Logger.getLogger(BufferedReader.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(BufferedReader.class.getName()).log(Level.SEVERE, null, ex);

    } finally {
      try {
        reader.close();
        fis.close();
      } catch (IOException ex) {
        Logger.getLogger(BufferedReader.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }
}