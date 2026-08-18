package flux.model.interaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import flux.api.FluxClient;
import flux.api.entities.Guild;
import flux.api.entities.Member;
import flux.api.entities.User;
import flux.api.entities.channel.Channel;
import flux.api.entities.interaction.SlashCommandInteraction;
import flux.model.AbstractFluxerEntity;
import flux.model.member.MemberImpl;
import flux.model.user.UserImpl;
import flux.model.message.MessageImpl;
import flux.api.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlashCommandInteractionImpl extends AbstractFluxerEntity implements SlashCommandInteraction {

    @JsonProperty("type")
    private int type;

    @JsonProperty("token")
    private String token;

    @JsonProperty("application_id")
    private String applicationId;

    @JsonProperty("guild_id")
    private String guildId;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("member")
    private MemberImpl member;

    @JsonProperty("user")
    private UserImpl user;

    @JsonProperty("data")
    private InteractionData data;

    @JsonProperty("message")
    private MessageImpl message;

    private FluxClient client;

    public void setClient(FluxClient client) {
        this.client = client;
        if (member != null)
            member.setFluxClient(client);
        if (message != null)
            message.setFluxClient(client);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public User getUser() {
        return user != null ? user : (member != null ? member.getUser() : null);
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public Guild getGuild() {
        if (guildId == null)
            return null;
        return client.getGuildById(guildId).join();
    }

    @Override
    public Channel getChannel() {
        if (channelId == null)
            return null;
        return client.getChannelById(channelId).join();
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public CompletableFuture<Void> deferReply(boolean ephemeral) {
        String json = String.format("{\"type\": 5, \"data\": {\"flags\": %d}}", ephemeral ? 64 : 0);
        return client.createInteractionResponse(getId(), token, json);
    }

    @Override
    public CompletableFuture<Void> reply(String content) {
        return reply(content, false);
    }

    static String buildReplyPayload(String content, boolean ephemeral) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("type", 4);
            ObjectNode data = root.putObject("data");
            data.put("content", content);
            data.put("flags", ephemeral ? 64 : 0);
            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build interaction reply payload", e);
        }
    }

    @Override
    public CompletableFuture<Void> reply(String content, boolean ephemeral) {
        return client.createInteractionResponse(getId(), token, buildReplyPayload(content, ephemeral));
    }

    @Override
    public CompletableFuture<Void> showModal(flux.api.payload.interaction.ModalPayload modal) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
            String modalJson = mapper.writeValueAsString(modal);
            String json = String.format("{\"type\": 9, \"data\": %s}", modalJson);
            return client.createInteractionResponse(getId(), token, json);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public String getCommandName() {
        return data != null ? data.name : null;
    }

    @Override
    public String getSubcommandName() {
        if (data == null || data.options == null)
            return null;
        return data.options.stream()
                .filter(o -> o.type == 1)
                .map(o -> o.name)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getSubcommandGroup() {
        if (data == null || data.options == null)
            return null;
        return data.options.stream()
                .filter(o -> o.type == 2)
                .map(o -> o.name)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Option> getOptions() {
        if (data == null || data.options == null)
            return new ArrayList<>();
        return new ArrayList<>(data.options);
    }

    @Override
    public Optional<Option> getOption(String name) {
        return getOptions().stream().filter(o -> o.getName().equals(name)).findFirst();
    }

    @Override
    public String getCustomId() {
        return data != null ? data.customId : null;
    }

    @Override
    public Integer getComponentType() {
        return data != null ? data.componentType : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InteractionData {
        @JsonProperty("id")
        public String id;
        @JsonProperty("name")
        public String name;
        @JsonProperty("type")
        public int type;
        @JsonProperty("options")
        public List<OptionImpl> options;
        @JsonProperty("custom_id")
        public String customId;
        @JsonProperty("component_type")
        public Integer componentType;
        @JsonProperty("target_id")
        public String targetId;
    }

    @Override
    public String getTargetId() {
        return data != null ? data.targetId : null;
    }

    public FluxClient getClient() {
        return client;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OptionImpl implements Option {
        @JsonProperty("name")
        public String name;
        @JsonProperty("type")
        public int type;
        @JsonProperty("value")
        public JsonNode value;
        @JsonProperty("focused")
        public boolean focused;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public String getValueAsString() {
            return value != null ? value.asText() : null;
        }

        @Override
        public long getValueAsLong() {
            return value != null ? value.asLong() : 0;
        }

        @Override
        public boolean getValueAsBoolean() {
            return value != null && value.asBoolean();
        }
    }
}
