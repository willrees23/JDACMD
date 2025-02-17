package dev.wand.jdacmd;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Getter
public abstract class SubCommand {

    private final String name;
    private final String description;
    @Setter
    private String groupName;
    @Setter
    private String groupDescription;
    @Setter
    private OptionData[] options;

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubCommand(String name, String description, String groupName, String groupDescription) {
        this.name = name;
        this.description = description;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    public SubcommandData data() {
        SubcommandData data = new SubcommandData(name, description);
        if (options != null) {
            data.addOptions(options);
        }
        return data;
    }
}
