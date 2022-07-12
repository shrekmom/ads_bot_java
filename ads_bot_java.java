import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.StandardCopyOption;
import java.io.*;
import java.util.Objects;

class ads_bot_java {
  public static void main(String[] args) throws Exception {
    cleardir();
    downloadFiles();
    combineFiles();
    cleardir();
  }

  public static void cleardir() {
    File directory = new File("text_files");

    for (File file : Objects.requireNonNull(directory.listFiles())) {
      if (!file.isDirectory()) {
        file.delete();
      }
    }
  }

  public static void combineFiles() throws IOException {
    File dir = new File("text_files");

    try (PrintWriter pw = new PrintWriter("ads_final_list.txt")) {
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

  public static void downloadFiles() throws Exception {
    FileInputStream fis = null;
    BufferedReader reader = null;

    try {
      fis = new FileInputStream("links.txt");
      reader = new BufferedReader(new InputStreamReader(fis));

      int linenum = 1;

      String line = reader.readLine();
      while (line != null) {
        String file = "C:\\Users\\Student\\Documents\\GitHub\\ads_bot_java\\text_files\\" + linenum + ".txt";
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