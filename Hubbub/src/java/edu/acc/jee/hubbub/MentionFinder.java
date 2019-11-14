package edu.acc.jee.hubbub;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionFinder {
    private static Pattern pattern = Pattern.compile("@\\w{6,20}");
    
    public static Set<String> parse(String content) {
        Set<String> result = new HashSet<>();
        Matcher m = pattern.matcher(content);
        while (m.find())
            result.add(m.group().substring(1));
        return result;
    }
}
