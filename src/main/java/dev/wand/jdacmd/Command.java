package dev.wand.jdacmd;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Getter
public abstract class Command {

    private final String name;
    private final String description;
    @Setter
    private SubCommand[] subCommands;
    @Setter
    private OptionData[] options;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    public SlashCommandData data() {
        SlashCommandData data = Commands.slash(name, description);
        if (options != null) {
            data.addOptions(options);
        }
        if (subCommands != null) {
            for (SubCommand sub : subCommands) {
                if (sub.getGroupName() != null) {
                    data.addSubcommandGroups(
                            new SubcommandGroupData(sub.getGroupName(), sub.getGroupDescription())
                                    .addSubcommands(sub.data()));
                } else {
                    data.addSubcommands(sub.data());
                }
            }
        }
        return data;
    }

    protected void addSubCommands(SubCommand... subCommands) {
        // add onto subCommands array
        if (this.subCommands == null) {
            this.subCommands = subCommands;
        } else {
            SubCommand[] newSubCommands = new SubCommand[this.subCommands.length + subCommands.length];
            System.arraycopy(this.subCommands, 0, newSubCommands, 0, this.subCommands.length);
            System.arraycopy(subCommands, 0, newSubCommands, this.subCommands.length, subCommands.length);
            this.subCommands = newSubCommands;
        }
    }

    protected void addOptions(OptionData... options) {
        // add onto options array
        if (this.options == null) {
            this.options = options;
        } else {
            OptionData[] newOptions = new OptionData[this.options.length + options.length];
            System.arraycopy(this.options, 0, newOptions, 0, this.options.length);
            System.arraycopy(options, 0, newOptions, this.options.length, options.length);
            this.options = newOptions;
        }
    }
}

