package flux.api.entities.channel;

public interface TextChannel extends Channel {

    String getTopic();

    boolean isNsfw();

}
