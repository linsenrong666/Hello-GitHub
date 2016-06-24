package com.linsr.dumpling.utils;

/**
 * description
 *
 * @author Linsr
 */
public class ZhihuUtils {

    public static String processHtmlBody(String body) {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        return html;
    }
}
