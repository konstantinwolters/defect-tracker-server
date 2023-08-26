package com.example.defecttrackerserver.security.rateLimiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BucketService {

    // Token Bucket settings per user
    @Value("${BUCKET.TOKENS-PER-MINUTE}")
    private int TOKENS_PER_MINUTE;

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String username) {
        return buckets.computeIfAbsent(username, this::newBucket);
    }

    private Bucket newBucket(String username) {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(TOKENS_PER_MINUTE, Duration.ofMinutes(1)))
                .build();
    }
}
