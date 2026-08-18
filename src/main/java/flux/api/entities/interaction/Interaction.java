package flux.api.entities.interaction;

import flux.api.entities.FluxerEntity;
import flux.api.entities.Guild;
import flux.api.entities.Member;
import flux.api.entities.User;
import flux.api.entities.channel.Channel;
import flux.api.payload.interaction.ModalPayload;

import java.util.concurrent.CompletableFuture;

public interface Interaction extends FluxerEntity {

    int getType();

    String getToken();

    String getApplicationId();

    User getUser();

    Member getMember();

    Guild getGuild();

    Channel getChannel();

    CompletableFuture<Void> deferReply(boolean ephemeral);

    CompletableFuture<Void> reply(String content);

    CompletableFuture<Void> reply(String content, boolean ephemeral);

    CompletableFuture<Void> showModal(ModalPayload modal);

    String getCustomId();

    String getGuildId();

    Integer getComponentType();

    default flux.api.entities.Message getMessage() {
        return null;
    }
}
