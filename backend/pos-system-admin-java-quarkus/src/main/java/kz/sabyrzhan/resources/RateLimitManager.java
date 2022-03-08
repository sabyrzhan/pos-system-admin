package kz.sabyrzhan.resources;

import com.google.common.util.concurrent.RateLimiter;
import io.smallrye.mutiny.Uni;
import kz.sabyrzhan.exceptions.TooManyRequestsException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RateLimitManager {
    private RateLimiter rateLimiter;
    private int timeout;

    public RateLimitManager(@ConfigProperty(name = "app.rate.limit") int rateLimit,
                            @ConfigProperty(name = "app.rate.timeout") int timeout) {
        rateLimiter = RateLimiter.create(rateLimit);
        this.timeout = timeout;
    }

    public Uni<Void> acquire() {
        boolean isSuccess;
        try {
            isSuccess = rateLimiter.tryAcquire(timeout, TimeUnit.SECONDS);
        } catch (IllegalArgumentException e) {
            isSuccess = false;
        }

        if (!isSuccess) {
            return Uni.createFrom().failure(new TooManyRequestsException());
        } else {
            return Uni.createFrom().voidItem();
        }
    }
}
