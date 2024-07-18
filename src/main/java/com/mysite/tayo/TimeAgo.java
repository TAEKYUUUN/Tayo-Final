package com.mysite.tayo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeAgo {

	public static String toRelative(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + "초 전";
        }
        
        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }
        
        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "시간 전";
        }
        
        long days = duration.toDays();
        if (days < 7) {
            return days + "일 전";
        }
        
        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + "주 전";
        }
        
        long months = days / 30;
        if (months < 12) {
            return months + "개월 전";
        }
        
        long years = days / 365;
        return years + "년 전";
    }

    public static String toRelative(Date date) {
        return toRelative(date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }
	
}
