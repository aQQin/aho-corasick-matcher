package com.akiin.ahocorasick;

import junit.framework.TestCase;
import org.junit.Test;
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
}