package flux.api.entities.interaction;

import flux.api.payload.interaction.CommandOptionChoice;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AutocompleteInteraction extends Interaction {

    String getCommandName();

    String getSubcommandName();

    String getSubcommandGroup();

    SlashCommandInteraction.Option getFocusedOption();

    List<SlashCommandInteraction.Option> getOptions();

    CompletableFuture<Void> replyChoices(List<CommandOptionChoice> choices);
}
