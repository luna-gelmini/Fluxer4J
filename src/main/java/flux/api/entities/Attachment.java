package flux.api.entities;

public interface Attachment extends FluxerEntity {
    String getFilename();
    String getUrl();
    String getProxyUrl();
    String getContentType();
    int getSize();
    int getHeight();
    int getWidth();
    boolean isEphemeral();
}
