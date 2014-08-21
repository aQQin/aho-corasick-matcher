package com.akiin.ahocorasick;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class AhoCorasickMatcherTest extends TestCase {

    @Test
    public void testCaseSensitive() throws Exception {

        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
        AhoCorasickMatcher acm = new AhoCorasickMatcher().caseSensitive(true);
        acm.setKeysFromProperties(props);
        String text = "When the wise turtle selected the Dragon Warrior the master did not believe it" +
                " however after the evil fled from the heavily guarded prison the master had no recourse but to " +
                "believe in and train the dragon warrior";

        System.out.println(acm.parseText(text));
        assertEquals("matches should  be only lowercase", 5, acm.parseText(text).size() );
        AhoCorasickMatcher acm1 = new AhoCorasickMatcher().caseSensitive(false);
        acm1.setKeysFromProperties(props);
        assertEquals("matches should  be only lowercase", 6, acm1.parseText(text).size() );

    }

    @Test
    public void testParseText() throws Exception {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("test.properties"));
        AhoCorasickMatcher acm = new AhoCorasickMatcher();
        acm.setKeysFromProperties(props);
        String text = "When the wise turtle selected the dragon warrior the master did not believe it" +
                " however after the evil fled from the heavily guarded prison the master had no recourse but to " +
                "believe in and train the dragon warrior";

        System.out.println(acm.parseText(text));

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
    public void testRemoveOverlaps(){
        HashSet<String> keys = new HashSet<String>(){{
            add("Tech");
            add("Technology");
            add("Tweet");
            add("ReTweet");
            add("an enigma");
            add("enigm");
        }};

        AhoCorasickMatcher acm = new AhoCorasickMatcher().removeOverlaps(false);
        acm.setKeysFromCollection(keys);
        String text = "The technology behind retweets is an enigma";
        List<String> overlappingMatches = acm.parseText(text);
        System.out.println("Overlapping: " + overlappingMatches);
        assertEquals("Overlapping matches should be counted", 5, overlappingMatches.size());

        AhoCorasickMatcher acmo = new AhoCorasickMatcher();
        acmo.setKeysFromCollection(keys);
        List<String> nonOverlappingMatches = acmo.parseText(text);
        System.out.println("Non Overlapping: " + nonOverlappingMatches);
        assertEquals("overlapping matches should not be counted", 3, nonOverlappingMatches.size());


    }
}