package edu.acc.jee.hubbub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentDecorator {
    private static final Pattern mention = Pattern.compile("@\\w{6,20}");
    private static final Pattern tag = Pattern.compile("#\\w{1,30}");
    
    public static String decorate(String content) {
        return decorateTag(decorateMention(content));
    }
    
    public static String decorateMention(String content) {
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
    
    public static String decorateTag(String content) {
         StringBuilder result = new StringBuilder(content.length() * 2);
        int copyIdx = 0;
        Matcher m = tag.matcher(content);
        while (m.find()) {
            result.append(content.substring(copyIdx, m.start()));
            result.append("<a href=\"main?action=tags&tagName=");
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
