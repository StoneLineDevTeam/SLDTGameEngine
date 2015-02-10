package net.sldt_team.gameEngine.sound.decoders;

import net.sldt_team.gameEngine.GameApplication;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class WaveSoundDecoder implements ISoundDecoder {

    private AudioInputStream ais;
    private AudioFormat format;
    private EnumChanelFormat chanelFormat;

    public void initialize(InputStream stream) {
        try {
            ais = AudioSystem.getAudioInputStream(stream);
            format = ais.getFormat();
            if (format.getChannels() == 1) {
                if (format.getSampleSizeInBits() == 8) {
                    chanelFormat = EnumChanelFormat.MONO_CHANEL_8BITS;
                } else if (format.getSampleSizeInBits() == 16) {
                    chanelFormat = EnumChanelFormat.MONO_CHANEL_16BITS;
                }
            } else if (format.getChannels() == 2) {
                if (format.getSampleSizeInBits() == 8) {
                    chanelFormat = EnumChanelFormat.STEREO_CHANEL_8BITS;
                } else if (format.getSampleSizeInBits() == 16) {
                    chanelFormat = EnumChanelFormat.STEREO_CHANEL_16BITS;
                }
            }
        } catch (UnsupportedAudioFileException e) {
            GameApplication.engineLogger.severe("Audio file is unsupported");
        } catch (IOException e) {
            GameApplication.engineLogger.severe("IO exception occured while initializing WAVE decoder");
        }
    }

    public ByteBuffer getData() {
        ByteBuffer buffer = null;
        try {
            int available = ais.available();
            if(available <= 0) {
                available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
            }
            byte[] buf = new byte[ais.available()];
            int read;
            int total = 0;
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
            buffer = convertAudioBytes(buf, format.getSampleSizeInBits() == 16);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    public EnumChanelFormat getChanelFormat() {
        return chanelFormat;
    }

    public int getSampleRate() {
        return (int)format.getSampleRate();
    }

    public String getName() {
        return "WAVEDecoder";
    }

    public void clearBuffers() {
        //try {
            if (ais != null) {
                format = null;
                ais = null;
            }
        /*} catch (IOException e) {
            GameApplication.engineLogger.warning("Unable to clear buffers for WAVEDecoder");
        }*/
    }

    /**
     * Method from LWJGL OpenAL WaveData
     */
    private ByteBuffer convertAudioBytes(byte[] audio_bytes, boolean two_bytes_data) {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(ByteOrder.LITTLE_ENDIAN);
        if (two_bytes_data) {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        } else {
            while (src.hasRemaining()) {
                dest.put(src.get());
            }
        }
        dest.rewind();
        return dest;
    }
}
