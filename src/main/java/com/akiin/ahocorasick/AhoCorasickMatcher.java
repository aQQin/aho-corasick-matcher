package com.akiin.ahocorasick;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by umar on 19/8/14.
 */
public class AhoCorasickMatcher {
    private AhoCorasick tree;
    private boolean prepared = false;
    private boolean caseSensitive = false;

    /**
     * add keys from a property file
     * @param dict : input property file
     * @return : updated AhoCorasickMatcher object
     */
    public AhoCorasickMatcher setKeysFromProperties(Properties dict){
        tree = new AhoCorasick();
        Enumeration<?> enumeration = dict.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            byte[] bytes = (caseSensitive) ? key.getBytes() : key.toLowerCase().getBytes();
            tree.add(bytes, dict.getProperty(key));
        }
        tree.prepare();
        prepared = true;
        return this;
    }


    /**
     * set whether match has to be case sensitive
     * @param sensitive : whether to be case sesitive or not
     * @return : new the object with case sensitive flag updated
     * throws TreeException
     */
    public AhoCorasickMatcher caseSensitive(boolean sensitive){
        if(prepared) throw new TreeException("Tree already built...");
        this.caseSensitive = sensitive;
        return this;
    }

    /**
     * take input text and run matcher on pre built tree
     * @param text : input text
     * @return  : list of matched entries
     * throws TreeException if parse was run on an unprepared tree;
     */
    public List<String> parseText(String text) throws TreeException{
        //if (!prepared) throw new TreeException("Tree has not been built yet...");
        List<String> termsThatHit = new ArrayList<String>();
        byte[] textBytes = (caseSensitive) ? text.getBytes() : text.toLowerCase().getBytes();
        for (Iterator iter = tree.search(textBytes); iter.hasNext(); ) {
            SearchResult result = (SearchResult) iter.next();
            termsThatHit.addAll(result.getOutputs());
        }
        return  termsThatHit;
    }

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("/home/umar/work/aQQin/repos/recapi/conf/etc/aqqin/categories.mapping.properties"));
        AhoCorasickMatcher acm = new AhoCorasickMatcher();
        acm.setKeysFromProperties(props);
        String text = "KEY: New-York-Mets\n" +
                "KEY: Man-Booker-Prize\n" +
                "KEY: collar\n" +
                "KEY: Rob-Delaney\n" +
                "KEY: Champions-League\n" +
                "KEY: ios\n" +
                "KEY: ios\n" +
                "KEY: ios\n" +
                "KEY: Journal-of-Experimental-Medicine\n" +
                "KEY: Journal-of-the-ACM\n" +
                "KEY: Edmonton-Oilers\n" +
                "KEY: spacecraft\n" +
                "KEY: Picture\n" +
                "KEY: Rhino\n" +
                "KEY: Money\n";

        System.out.println(acm.parseText(text));


    }

    class TreeException extends RuntimeException {
        public TreeException(String message){
            super(message);
        }
    }

}
