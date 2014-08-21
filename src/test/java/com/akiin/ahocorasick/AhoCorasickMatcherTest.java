package com.akiin.ahocorasick;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class AhoCorasickMatcherTest extends TestCase {


    @Test
    public void testParseText() throws Exception {

        AhoCorasickMatcher acm = new AhoCorasickMatcher();
        HashMap<String,String> dict = new HashMap<String,String> (){{
            put("dragon warrior", "Po");
            put("Master", "Shifu");
            put("wise", "Oogway");
            put("evil", "Tai Lung");
        }};
        acm.setKeysFromCollection(dict.keySet());
        String text = "When the wise turtle selected the Dragon Warrior the master did not believe it" +
                " however after the evil fled from the heavily guarded prison the master had no recourse but to " +
                "believe in and train the Dragon Warrior";

        List<String> matches = acm.parseText(text);

        System.out.println(matches);
        assertEquals("matches should  be only lowercase", 6, matches.size());
        AhoCorasickMatcher acmCI = new AhoCorasickMatcher().caseSensitive(true);
        acmCI.setKeysFromCollection(dict.keySet());
        List<String> matchesCI = acmCI.parseText(text);
        System.out.println(matchesCI);
        assertEquals("matches should  be only lowercase", 2, matchesCI.size());
    }

    @Test
    public void testSetKeysFromCollection(){
        HashSet<String> keys = new HashSet<String>(){{
            add("Tech");
            add("Technology");
            add("Tweet");
            add("ReTweet");
        }};

        AhoCorasickMatcher acm = new AhoCorasickMatcher();
        acm.setKeysFromCollection(keys);
        String text = "The technology behind retweets";
        System.out.println(acm.parseText(text));


    }

    @Test
    public void testRemoveOverlaps1(){
        HashSet<String> keys = new HashSet<String>(){{
            add("ebook");
            add("Tech");
            add("game");
            add("spot");

        }};
        AhoCorasickMatcher acm = new AhoCorasickMatcher().removeOverlaps(true);
        acm.setKeysFromCollection(keys);
        String url = "http://www.gamespot.com/articles/facebook-will-pay-you-to-find-oculus-rift-bugs/1100-6421849/";
        String text = url.replaceFirst("http://", " ").replaceAll("-", " ");
        List<String> matches = acm.parseText(text);
        System.out.println(keys);
        System.out.println(text);
        System.out.println(matches);
        assertEquals("No key should match since matches are partial words", 0, matches.size());


    }

    @Test
    public void testRemoveOverlaps(){
        HashSet<String> keys = new HashSet<String>(){{
            add("Tech");
            add("The Technology");
            add("Tweets");
            add("ReTweets");
            add("an enigma");
            add("enigm");
        }};

        AhoCorasickMatcher acm = new AhoCorasickMatcher().removeOverlaps(false);
        acm.setKeysFromCollection(keys);
        String text = "The technology, behind retweets is an enigma is an enigm";
        List<String> overlappingMatches = acm.parseText(text);
        System.out.println("Overlapping: " + overlappingMatches);
        assertEquals("Overlapping matches should be counted", 4, overlappingMatches.size());

        AhoCorasickMatcher acmo = new AhoCorasickMatcher();
        acmo.setKeysFromCollection(keys);
        List<String> nonOverlappingMatches = acmo.parseText(text);
        System.out.println("Non Overlapping: " + nonOverlappingMatches);
        assertEquals("overlapping matches should not be counted", 4, nonOverlappingMatches.size());

    }


}