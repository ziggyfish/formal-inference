package com.beprogramming.demo.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.ExecutableElement;
import java.util.Map;
import java.util.regex.Matcher;

public class Utils {
    public static String concat(Object... values) {
        if (values.length == 0)
            return "";

        StringBuilder buffer = new StringBuilder();
        for (Object value : values) {
            if (value == null)
                buffer.append("NULL");
            else {
                buffer.append(value.toString());
            }
        }
        return buffer.toString();
    }

    public static String escapeDollar(String value) {
        return value.replaceAll("\\$", Matcher.quoteReplacement("$$"));
    }

    public static String wrapString(String value) {
        return Utils.concat("\"", escapeDollar(value), "\"");
    }

    public static void printHashMap(Map<String, ?> map) {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            printHashMap(map, entry.getKey());
        }
    }

    public static void printHashMap(Map<String, ?> map, String key) {

        System.out.println(concat(key, " = \n", map.get(key), "\n"));
    }

    public static String getMethodName(ExecutableElement el){
        return Utils.concat(getClass(el), "::", el.getSimpleName().toString());
    }
    public static String getClass(ExecutableElement el){
        return el.getEnclosingElement().toString();
    }
}
