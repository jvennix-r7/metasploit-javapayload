package com.metasploit.meterpreter.android;

import com.metasploit.meterpreter.Meterpreter;
import com.metasploit.meterpreter.TLVPacket;
import com.metasploit.meterpreter.command.Command;

import android.speech.tts.TextToSpeech;

public class speak implements Command {

    private static final int TLV_EXTENSIONS = 20000;
    private static final int TLV_TYPE_SPEAK_TEXT = TLVPacket.TLV_META_TYPE_STRING | (TLV_EXTENSIONS + 9022);

    @Override
    public int execute(Meterpreter meterpreter, TLVPacket request, TLVPacket response) throws Exception {
        String text = request.getStringValue(TLV_TYPE_SPEAK_TEXT);

        try {
            TextToSpeech tts = new TextToSpeech(this, this);
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        } catch (Exception e) {
            return ERROR_FAILURE;
        }

        return ERROR_SUCCESS;
    }

}
