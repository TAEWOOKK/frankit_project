package com.app.constant;

public class DateTimeFormat {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // private 생성자로 인스턴스화 방지
    private DateTimeFormat() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}