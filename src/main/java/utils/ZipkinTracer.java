package utils;

import config.PropsConfig;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for tracing transaction times in a format compatible with Zipkin.
 * This is a lightweight implementation that uses the existing logging framework.
 */
@Slf4j
public class ZipkinTracer {

    private static final ConcurrentHashMap<String, Long> activeSpans = new ConcurrentHashMap<>();
    private static final ThreadLocal<String> currentTraceId = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, String>> currentTags = ThreadLocal.withInitial(HashMap::new);

    /**
     * Check if tracing is enabled in the configuration.
     * @return true if tracing is enabled, false otherwise
     */
    public static boolean isEnabled() {
        return PropsConfig.getProps().zipkinEnabled();
    }

    /**
     * Start a new trace with the given name.
     * @param name The name of the trace
     * @return The trace ID, or null if tracing is disabled
     */
    public static String startTrace(@NotNull String name) {
        if (!isEnabled()) {
            return null;
        }

        String traceId = generateTraceId();
        currentTraceId.set(traceId);
        currentTags.get().clear();
        return startSpan(name);
    }

    /**
     * Start a new span with the given name.
     * @param name The name of the span
     * @return The span ID, or null if tracing is disabled
     */
    public static String startSpan(@NotNull String name) {
        if (!isEnabled()) {
            return null;
        }

        String spanId = generateSpanId();
        String traceId = currentTraceId.get();
        if (traceId == null) {
            traceId = startTrace(name);
            if (traceId == null) {
                return null;
            }
        }

        activeSpans.put(spanId, System.currentTimeMillis());
        log.info("Zipkin: Started span {} for trace {} with name {}", spanId, traceId, name);
        return spanId;
    }

    /**
     * End the span with the given ID.
     * @param spanId The span ID
     */
    public static void endSpan(@NotNull String spanId) {
        if (!isEnabled()) {
            return;
        }

        Long startTime = activeSpans.remove(spanId);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            String traceId = currentTraceId.get();
            Map<String, String> tags = new HashMap<>(currentTags.get());

            log.info("Zipkin: Ended span {} for trace {} with duration {}ms and tags {}", 
                    spanId, traceId, duration, tags);
        }
    }

    /**
     * Add a tag to the current span.
     * @param key The tag key
     * @param value The tag value
     */
    public static void addTag(@NotNull String key, @NotNull String value) {
        if (!isEnabled()) {
            return;
        }

        currentTags.get().put(key, value);
    }

    /**
     * Generate a unique trace ID.
     * @return A unique trace ID
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate a unique span ID.
     * @return A unique span ID
     */
    private static String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
