package flux.model.interaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flux.api.entities.interaction.AutocompleteInteraction;
import flux.api.entities.interaction.SlashCommandInteraction;
import flux.api.payload.interaction.CommandOptionChoice;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutocompleteInteractionImpl extends SlashCommandInteractionImpl implements AutocompleteInteraction {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SlashCommandInteraction.Option getFocusedOption() {
        return getOptions().stream()
                .filter(o -> {
                    if (o instanceof OptionImpl impl) {
                        return impl.focused;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public CompletableFuture<Void> replyChoices(List<CommandOptionChoice> choices) {
        try {
            String choicesJson = mapper.writeValueAsString(choices);
            String json = String.format("{\"type\": 8, \"data\": {\"choices\": %s}}", choicesJson);
            return getClient().createInteractionResponse(getId(), getToken(), json);
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}
