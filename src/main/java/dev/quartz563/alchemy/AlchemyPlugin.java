package dev.quartz563.alchemy;

import dev.quartz563.alchemy.commands.AlchemyCommand;
import dev.quartz563.alchemy.configs.AlchemistData;
import dev.quartz563.alchemy.configs.AlchemySettings;
import dev.quartz563.alchemy.configs.CustomRepresentationConfig;
import dev.quartz563.alchemy.data.AlchemyDataStorage;
import dev.quartz563.alchemy.listeners.*;
import dev.quartz563.alchemy.registeries.RepresentationRegistry;
import dev.quartz563.alchemy.registeries.RecipeRegistry;
import dev.quartz563.alchemy.registeries.StructureRegistry;
import dev.quartz563.alchemy.structures.AlchemyStructures;
import org.mineacademy.fo.plugin.SimplePlugin;

public class AlchemyPlugin extends SimplePlugin {

    private AlchemyDataStorage dataStore;

    @Override
    protected void onPluginStart() {
        //database loading
        this.dataStore = new AlchemyDataStorage();
        this.dataStore.initialise(getDataFolder());
        CustomRepresentationConfig customRepresentationConfig = new CustomRepresentationConfig(getDataFolder());
        customRepresentationConfig.load();
        new RepresentationRegistry(dataStore, customRepresentationConfig).load();

        //file loading
        if(getResource("recipes.yml") != null)
            saveResource("recipes.yml", false);
        RecipeRegistry.load();
        //AlchemySettings.load(AlchemySettings.class); why does this line hate me so much!

        //registration loading
        registerEvents();
        registerCommandGroups();

        //structure registration
        AlchemyStructures.registerAll();
    }

    @Override
    protected void onPluginStop() {
        this.dataStore.close();
        AlchemistData.removeAll();
        StructureRegistry.clear();
    }

    private void registerEvents() {
        registerEvents(new PlayerInteractionListener());
        registerEvents(new ProcessorListener()); //I actually think this one can be folded into Structures!
        registerEvents(new AlchemistListener());
        registerEvents(new RecipeBookListeners());
        registerEvents(new StructureListener());
    }

    private void registerCommandGroups(){
        registerCommands(new AlchemyCommand());
    }

    public static AlchemyPlugin getInstance() {
        return (AlchemyPlugin) SimplePlugin.getInstance();
    }
}
