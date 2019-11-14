package edu.acc.jee.hubbub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentDecorator {
    private static final Pattern mention = Pattern.compile("@\\w{6,20}");
    
    public static String decorate(String content) {
        StringBuilder result = new StringBuilder(content.length() * 2);
        int copyIdx = 0;
        Matcher m = mention.matcher(content);
        while (m.find()) {
            result.append(content.substring(copyIdx, m.start()));
            result.append("<a href=\"main?action=mentions&subject=");
            result.append(content.substring(m.start()+1, m.end()));
            result.append("\">");
            result.append(content.substring(m.start(), m.end()));
            result.append("</a>");
            copyIdx = m.end();
        }
        result.append(content.substring(copyIdx));
        return result.toString();
    }
    
}
