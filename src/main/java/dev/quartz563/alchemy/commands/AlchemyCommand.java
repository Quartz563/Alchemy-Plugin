package dev.quartz563.alchemy.commands;

import dev.quartz563.alchemy.commands.subcommands.AlmanacCommand;
import dev.quartz563.alchemy.commands.subcommands.RecipeBookCommand;
import dev.quartz563.alchemy.commands.subcommands.TransformCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class AlchemyCommand extends SimpleCommandGroup {

    public AlchemyCommand() {
        super("alchemy|a");
    }

    @Override
    protected void registerSubcommands() {
        //DEBUG COMMANDS
        registerSubcommand(new AlmanacCommand(this));
        registerSubcommand(new RecipeBookCommand(this));
        registerSubcommand(new TransformCommand(this));

        //ACTUAL COMMANDS
    }

}
