package flux.opus;

import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class OpusEncoder implements AutoCloseable {
    public static final int SAMPLE_RATE = 48000;
    public static final int CHANNELS = 2;
    public static final int BITRATE = 128000;
    public static final int FRAME_SIZE = 960;
    private static final Logger LOGGER = LoggerFactory.getLogger(OpusEncoder.class);
    private final Pointer encoder;
    private final ShortBuffer directPcmBuffer;
    private boolean closed = false;

    public OpusEncoder() {
        IntBuffer error = IntBuffer.allocate(1);
        this.encoder = Opus.INSTANCE.opus_encoder_create(SAMPLE_RATE, CHANNELS, Opus.OPUS_APPLICATION_AUDIO, error);
        if (error.get(0) != Opus.OPUS_OK || this.encoder == null) {
            throw new IllegalStateException("Failed to create Opus encoder: " + Opus.INSTANCE.opus_strerror(error.get(0)));
        }

        Opus.INSTANCE.opus_encoder_ctl(this.encoder, Opus.OPUS_SET_BITRATE_REQUEST, BITRATE);

        this.directPcmBuffer = ByteBuffer.allocateDirect(FRAME_SIZE * CHANNELS * 2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asShortBuffer();

        LOGGER.debug("Opus encoder created successfully with bitrate {}.", BITRATE);
    }

    public synchronized byte[] encode(byte[] pcmData) {
        if (closed || pcmData.length != FRAME_SIZE * CHANNELS * 2) {
            return null;
        }

        ShortBuffer heapPcmBuffer = ByteBuffer.wrap(pcmData)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asShortBuffer();

        directPcmBuffer.clear();

        directPcmBuffer.put(heapPcmBuffer);

        directPcmBuffer.flip();

        ByteBuffer opusBuffer = ByteBuffer.allocateDirect(4096);

        int result = Opus.INSTANCE.opus_encode(encoder, directPcmBuffer, FRAME_SIZE, opusBuffer, opusBuffer.capacity());
        if (result < 0) {
            LOGGER.error("Opus native encode failed with error: {} ({})", result, Opus.INSTANCE.opus_strerror(result));
            return null;
        }

        byte[] opusData = new byte[result];
        opusBuffer.get(opusData);
        return opusData;
    }

    @Override
    public synchronized void close() {
        if (!closed) {
            Opus.INSTANCE.opus_encoder_destroy(encoder);
            closed = true;
            LOGGER.debug("Opus encoder destroyed.");
        }
    }
}
