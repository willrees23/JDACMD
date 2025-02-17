package dev.wand.jdacmd;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class JDACMD extends ListenerAdapter {

    private final ArrayList<Command> commands = new ArrayList<>();

    public JDACMD(Command... command) {
        register(command);
    }

    public void push(Guild guild) {
        List<CommandData> cmds = new ArrayList<>();
        for (Command command : getCommands()) {
            cmds.add(command.data());
        }
        guild.updateCommands().addCommands(cmds).queue();
    }

    public void register(Command... command) {
        commands.addAll(List.of(command));
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            if (!event.getName().equals(command.getName()))
                continue;

            if (command.getSubCommands() == null) {
                command.execute(event);
                return;
            }

            for (SubCommand subCommand : command.getSubCommands()) {
                if (event.getSubcommandGroup() == null) {
                    if (Objects.equals(event.getSubcommandName(), subCommand.getName())) {
                        subCommand.execute(event);
                        return;
                    }
                } else {
                    if (event.getSubcommandGroup().equals(subCommand.getGroupName()) && Objects.equals(event.getSubcommandName(), subCommand.getName())) {
                        subCommand.execute(event);
                        return;
                    }
                }
            }
        }
    }
}

