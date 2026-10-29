package dev.quartz563.alchemy.configs;

import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.expiringmap.ExpirationPolicy;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AlchemistData extends YamlConfig {

    private static final ExpiringMap.ExpirationListener<UUID, AlchemistData> ON_EXPIRE =
            (uuid, data) -> data.save();

    private static final ExpiringMap<UUID, AlchemistData> CACHE = ExpiringMap.<UUID, AlchemistData>builder()
            .expiration(10, TimeUnit.MINUTES)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .expirationListener(ON_EXPIRE)
            .build();

    private Set<String> discoveredRecipes = new HashSet<>();

    @Override
    protected void onLoad() {
        final List<String> saved = this.getStringList("Discovered_Recipes");
        this.discoveredRecipes = new HashSet<>(saved);
    }

    @Override
    protected void onSave() {
        this.set("Discovered_Recipes", this.discoveredRecipes.stream().toList());
    }

    public boolean hasDiscovered(String recipeKey) {
        return discoveredRecipes.contains(recipeKey);
    }

    public Set<String> getDiscoveredRecipes() {
        return Collections.unmodifiableSet(discoveredRecipes);
    }

    public void discoverRecipe(String recipeKey) {
        if(discoveredRecipes.add(recipeKey))
            this.save();
    }

    public static AlchemistData get(Player player) {
        return get(player.getUniqueId());
    }

    public static AlchemistData get(final UUID uuid) {
        if(!CACHE.containsKey(uuid)) {
            AlchemistData data = new AlchemistData();
            data.loadConfiguration(NO_DEFAULT, "players/" + uuid + ".yml");
            CACHE.put(uuid, data);
        }
        return CACHE.get(uuid);
    }

    public static void remove(Player player) {
        final AlchemistData data = CACHE.remove(player.getUniqueId());
        if(data != null) {
            data.save();
        }
    }

    public static void removeAll() {
        CACHE.forEach((uuid, data) -> data.save());
        CACHE.clear();
    }
}
