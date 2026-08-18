package flux.opus;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface Opus extends Library {

    Opus INSTANCE = Native.load("opus", Opus.class);

    int OPUS_OK = 0;
    int OPUS_BAD_ARG = -1;
    int OPUS_BUFFER_TOO_SMALL = -2;
    int OPUS_INTERNAL_ERROR = -3;
    int OPUS_INVALID_PACKET = -4;
    int OPUS_UNIMPLEMENTED = -5;
    int OPUS_INVALID_STATE = -6;
    int OPUS_ALLOC_FAIL = -7;
    int OPUS_APPLICATION_AUDIO = 2049;
    int OPUS_SET_BITRATE_REQUEST = 4002;

    Pointer opus_decoder_create(int Fs, int channels, IntBuffer error);

    int opus_decode(Pointer st, byte[] in, int len, ShortBuffer out, int frame_size, int decode_fec);

    void opus_decoder_destroy(Pointer st);

    String opus_strerror(int error);

    Pointer opus_encoder_create(int Fs, int channels, int application, IntBuffer error);

    int opus_encode(Pointer st, ShortBuffer pcm, int frame_size, ByteBuffer data, int max_data_bytes);

    int opus_encoder_ctl(Pointer st, int request, Object... args);

    void opus_encoder_destroy(Pointer st);
}
