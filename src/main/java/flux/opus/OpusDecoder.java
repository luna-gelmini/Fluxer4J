package flux.opus;

import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class OpusDecoder implements AutoCloseable {
    public static final int SAMPLE_RATE = 48000;
    public static final int CHANNELS = 2;
    public static final int FRAME_SIZE = 960;
    public static final int MAX_FRAME_SIZE = FRAME_SIZE * 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(OpusDecoder.class);
    private final Pointer decoder;
    private final ShortBuffer decodedBuffer;

    public OpusDecoder() {
        IntBuffer error = IntBuffer.allocate(1);
        this.decoder = Opus.INSTANCE.opus_decoder_create(SAMPLE_RATE, CHANNELS, error);
        if (error.get(0) != Opus.OPUS_OK || this.decoder == null) {
            throw new IllegalStateException("Failed to create Opus decoder: " + Opus.INSTANCE.opus_strerror(error.get(0)));
        }
        this.decodedBuffer = ShortBuffer.allocate(MAX_FRAME_SIZE * CHANNELS);
        LOGGER.debug("Opus decoder created successfully.");
    }

    public synchronized byte[] decode(byte[] opusData) {
        if (decoder == null) {
            throw new IllegalStateException("Decoder is already closed.");
        }
        int result = Opus.INSTANCE.opus_decode(decoder, opusData, opusData.length, decodedBuffer, MAX_FRAME_SIZE, 0);
        if (result < 0) {
            LOGGER.error("Opus native decode failed with error: {} ({})", result, Opus.INSTANCE.opus_strerror(result));
            return null;
        }

        byte[] pcmData = new byte[result * CHANNELS * 2];
        for (int i = 0; i < result * CHANNELS; i++) {
            short s = decodedBuffer.get(i);
            pcmData[i * 2] = (byte) s;
            pcmData[i * 2 + 1] = (byte) (s >> 8);
        }
        return pcmData;
    }

    @Override
    public synchronized void close() {
        if (decoder != null) {
            Opus.INSTANCE.opus_decoder_destroy(decoder);
            LOGGER.debug("Opus decoder destroyed.");
        }
    }
}
