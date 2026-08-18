package flux.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import flux.api.entities.Attachment;
import flux.model.AbstractFluxerEntity;

public class AttachmentImpl extends AbstractFluxerEntity implements Attachment {
    @JsonProperty("filename")
    private String filename;

    @JsonProperty("url")
    private String url;

    @JsonProperty("proxy_url")
    private String proxyUrl;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("size")
    private int size;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("ephemeral")
    private boolean ephemeral;

    public AttachmentImpl() {
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getProxyUrl() {
        return proxyUrl;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getHeight() {
        return height != null ? height : 0;
    }

    @Override
    public int getWidth() {
        return width != null ? width : 0;
    }

    @Override
    public boolean isEphemeral() {
        return ephemeral;
    }
}
