package dev.marcgil.hexagon.bootstrap.health;

import com.codahale.metrics.health.HealthCheck;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
        return Result.healthy();
    }
}