package com.akiin.ahocorasick;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by umar on 19/8/14.
 */
public class AhoCorasickMatcher {
    private AhoCorasick tree;
    private boolean prepared = false;
    private boolean caseSensitive = false;
    private boolean removeOverlaps = true;

    public AhoCorasickMatcher setKeysFromCollection(Collection<String> dict){
        tree = new AhoCorasick();
        for (String key: dict) {
            byte[] bytes = (caseSensitive) ? key.getBytes() : key.toLowerCase().getBytes();
            tree.add(bytes, key);
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
     * whether to remove overlapping matches
     * @param remove
     * @return
     */
    public AhoCorasickMatcher removeOverlaps(boolean remove){
        if(prepared) throw new TreeException("Tree already built...");
        this.removeOverlaps = remove;
        return this;
    }

    /**
     * take input text and run matcher on pre built tree
     * @param text : input text
     * @return     : list of matched entries
     * throws TreeException if parse was run on an unprepared tree;
     */
    public List<String> parseText(String text) throws TreeException{
        if (!prepared) throw new TreeException("Tree has not been built yet...");
        List<String> termsThatHit = new ArrayList<String>();
        if (text == null || text.isEmpty()){
            return termsThatHit;
        }
        TreeMap<Integer,String> matches = new TreeMap<Integer, String>();
        byte[] textBytes = (caseSensitive) ? text.getBytes() : text.toLowerCase().getBytes();
        for (Iterator iter = tree.search(textBytes); iter.hasNext(); ) {
            SearchResult result = (SearchResult) iter.next();
//            System.out.println("O: " + result.getOutputs() + "OFS: " + result.getLastIndex());
            Object[] objects = result.getOutputs().toArray();
//            System.out.println(Arrays.asList(objects));
            Arrays.sort(objects, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    Integer length1 = o1.toString().length();
                    Integer length2 = o2.toString().length();
                    return length2.compareTo(length1);
                }
            });
            int lastIndex = result.getLastIndex();
            String fullMatch ;
            int leftBound = Math.max(0, (lastIndex - (objects[0].toString().length() + 1)));
            /*System.out.println(String.format("LI: [%d] LB:[%d] SS: %s OO: %s",
                    lastIndex, leftBound, text.substring(leftBound, lastIndex), objects[0].toString()));*/
            if(lastIndex < text.length()){
                fullMatch = text.substring(leftBound, lastIndex + 1);
            } else {
                fullMatch = text.substring(leftBound, lastIndex) + " ";
            }
            if (leftBound == 0) fullMatch = " " + fullMatch;
            // add only if the match ended in a word boundry
            Pattern compile = Pattern.compile("^\\W.*\\W$");
            if (compile.matcher(fullMatch).matches() ){
//                System.out.println("SELE: [" + fullMatch + "]");
                matches.put(lastIndex, objects[0].toString());
            } else {
//                System.out.println("DROP: [" + fullMatch + "]");
            }
        }

        if (removeOverlaps){
            int lastIndex = -1;
            int matchLen=0;
            String match = null;
            for (Integer offset: matches.descendingKeySet()){
                if (lastIndex == -1){
                    match = matches.get(offset);
                    lastIndex = offset - match.length();
                    termsThatHit.add(0,match);
                } else if(offset <= lastIndex) {
                    match = matches.get(offset);
                    lastIndex = offset - match.length();
                    termsThatHit.add(0,match);
                }
            }
        } else {
            termsThatHit.addAll(matches.values());
        }
//        System.out.println(matches);
        return  termsThatHit;
    }


    class TreeException extends RuntimeException {
        public TreeException(String message){
            super(message);
        }
    }

}
