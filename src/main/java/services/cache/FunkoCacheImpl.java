package services.cache;

import models.Funkos;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FunkoCacheImpl implements FunkoCache{
    private int maxSize;
    private Map<Integer, Funkos> cache;
    private ScheduledExecutorService cleaner;

    public FunkoCacheImpl(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<Integer, Funkos>(maxSize, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Funkos> eldest) {
                return size() > maxSize;
            }
        };
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::clear, 2, 2, TimeUnit.MINUTES);
    }
    @Override
    public CompletableFuture<Void> put(Integer key, Funkos value) {
        return CompletableFuture.runAsync(() -> {
            cache.put(key, value);
        });
    }

    @Override
    public CompletableFuture<Funkos> get(Integer key) {
        return CompletableFuture.supplyAsync(() -> cache.get(key));
    }

    @Override
    public CompletableFuture<Void> remove(Integer key) {
        return CompletableFuture.runAsync(() -> {
            cache.remove(key);
        });
    }

    @Override
    public CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() -> {
            cache.entrySet().removeIf(entry -> {
                return entry.getValue().getUpdateTime().plusMinutes(2).isBefore(LocalDateTime.now());
            });
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            cleaner.shutdown();
        });
    }
}
