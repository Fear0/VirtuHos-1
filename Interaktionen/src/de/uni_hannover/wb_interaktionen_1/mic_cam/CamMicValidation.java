package de.uni_hannover.wb_interaktionen_1.mic_cam;
import javax.sound.sampled.*;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class CamMicValidation {
    private boolean camConnected;
    private boolean micConnected;


    public CamMicValidation(){

        this.camConnected = true;
        this.micConnected = true;
        this.update();

    }

    //updates the states of webcam and microphone
    public void update(){

        //detect microphone
        AudioFormat format = new AudioFormat(44100,16,1, true,true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        //if mic wasn't found set variable on false
        if (AudioSystem.isLineSupported(info))
            micConnected = true;
        else
            micConnected = false;

        //detect Webcam
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture capture = new VideoCapture(1);
        //if cam wasn't found set variable on false
        if(capture.isOpened())
            camConnected = true;
        else
            camConnected = false;
    }

    public boolean getCam() {
        return camConnected;
    }

    public boolean getMic() {
        return micConnected;
    }

}