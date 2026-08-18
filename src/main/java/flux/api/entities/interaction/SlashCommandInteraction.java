package flux.api.entities.interaction;

import java.util.List;
import java.util.Optional;

public interface SlashCommandInteraction extends Interaction {

    String getCommandName();

    String getSubcommandName();

    String getSubcommandGroup();

    List<Option> getOptions();

    Optional<Option> getOption(String name);

    interface Option {
        String getName();
        int getType();
        String getValueAsString();
        long getValueAsLong();
        boolean getValueAsBoolean();
    }

    String getTargetId();
}
