package com.example.foodie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OrderIdGenerator {
    public static String generateOrderId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        // Generate a random string (you can customize the length)
        String randomString = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);

        // Combine the timestamp and random string to create the order ID

        return timestamp + randomString;
    }
}

