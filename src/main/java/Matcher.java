import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by umar on 19/8/14.
 */
public class Matcher {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        AhoCorasick tree = new AhoCorasick();
        /*Scanner scanner = new Scanner(new File("london_streets.txt")).useDelimiter("\\A");
        while(scanner.hasNext()){
            String text = scanner.next().trim();
            System.out.println(text);
            tree.add(text.getBytes("ASCII"), text);

        }
        tree.prepare();*/
        long endTime = System.currentTimeMillis();
        System.out.println("endTime - startTime = " +
                (endTime - startTime) +
                " milliseconds");
        String data = "abberley mews \n" +
                "abbervill road \n" +
                "abbess close \n" +
                "abbeville road \n" +
                "abbey cottages \n" +
                "abbey drive \n" +
                "abbeyfield estate \n" +
                "abbey field road \n" +
                "abbeyfield road \n" +
                "abbeyfield road rotherhithe \n";

        for(String term : data.split("\n")){
            tree.add(term.getBytes(), term);
        }
        tree.prepare();
        Set termsThatHit = new HashSet();
        for (Iterator iter = tree.search(data.getBytes("ASCII")); iter.hasNext(); ) {
            SearchResult result = (SearchResult) iter.next();
            System.out.println(result.getLastIndex() + " : " + result.getOutputs());
            termsThatHit.addAll(result.getOutputs());
        }
        System.out.println(termsThatHit);
        // main1(null);
    }

    static public void main1(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        AhoCorasick tree = new AhoCorasick();

        Properties props = new Properties();
        props.load(new FileInputStream("/home/umar/work/aQQin/repos/recapi/conf/etc/aqqin/categories.mapping.properties"));
        Enumeration<?> enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            tree.add(key.getBytes(), key);
        }
        tree.prepare();
        long endTime = System.currentTimeMillis();
        System.out.println("endTime - startTime = " +
                (endTime - startTime) +
                " milliseconds");

        String text = "KEY: New-York-Mets\n" +
                "KEY: Man-Booker-Prize\n" +
                "KEY: collar\n" +
                "KEY: Rob-Delaney\n" +
                "KEY: Champions-League\n" +
                "KEY: ios\n" +
                "KEY: Journal-of-Experimental-Medicine\n" +
                "KEY: Journal-of-the-ACM\n" +
                "KEY: Edmonton-Oilers\n" +
                "KEY: spacecraft\n" +
                "KEY: Picture\n" +
                "KEY: Rhino\n" +
                "KEY: Money\n";

        Set termsThatHit = new HashSet();
        for (Iterator iter = tree.search(text.getBytes()); iter.hasNext(); ) {
            SearchResult result = (SearchResult) iter.next();
            System.out.println(result.getLastIndex() + " : " + result.getOutputs());
            termsThatHit.addAll(result.getOutputs());
        }
        System.out.println(termsThatHit);
    }
}
