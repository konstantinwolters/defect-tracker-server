package com.example.defecttrackerserver.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BucketService {

    // Token Bucket settings per user
    @Value("${BUCKET.TOKENS-PER-MINUTE}")
    private int TOKENS_PER_MINUTE = 10;

    @Value("${BUCKET.REFILL-TIME}")
    private long REFILL_TIME;

    private final ConcurrentHashMap<String, Long> lastRefillTime = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> tokenBucket = new ConcurrentHashMap<>();

    public boolean isTokenBucketEmpty(String username) {
        // Initialization if not present
        lastRefillTime.putIfAbsent(username, System.currentTimeMillis());
        tokenBucket.putIfAbsent(username, TOKENS_PER_MINUTE);

        // Refill logic
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRefill = currentTime - lastRefillTime.get(username);
        int tokensToAdd = (int) (timeSinceLastRefill / REFILL_TIME * TOKENS_PER_MINUTE);
        int newTokenBucketValue = Math.min(tokenBucket.get(username) + tokensToAdd, TOKENS_PER_MINUTE);

        // Update values
        lastRefillTime.put(username, currentTime);
        tokenBucket.put(username, newTokenBucketValue);

        // Check and deduct token
        if (newTokenBucketValue > 0) {
            tokenBucket.put(username, newTokenBucketValue - 1);
            return true;
        }

        return false;
    }
}
