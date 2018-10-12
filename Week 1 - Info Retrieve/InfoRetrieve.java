import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InfoRetrieve {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String userID = sc.nextLine();
        try {
            String source = getURLSource("https://www.ecs.soton.ac.uk/people/" + userID);
            String name = source.split("property=\"name\">")[1].split("</h1><h2")[0];
            System.out.println(name);
        } catch (IOException ex) {
            Logger.getLogger(InfoRetrieve.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getURLSource(String url) throws IOException {
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }

}
