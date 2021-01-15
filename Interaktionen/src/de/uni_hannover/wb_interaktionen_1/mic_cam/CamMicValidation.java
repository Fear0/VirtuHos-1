package de.uni_hannover.wb_interaktionen_1.mic_cam;
import javax.sound.sampled.*;

import de.uni_hannover.wb_interaktionen_1.website.WebsiteOpener;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import static org.opencv.videoio.Videoio.*;


public class CamMicValidation {
    private boolean camConnected;
    private boolean micConnected;


    public CamMicValidation(){

        this.camConnected = false;
        this.micConnected = false;
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
        WebsiteOpener wo = new WebsiteOpener("mac");
        boolean is_mac = wo.isMac();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        for(int i = 0; i < 10; i++) {
            VideoCapture capture;
            if(is_mac){
                capture = new VideoCapture(i);
            } else {
                capture = new VideoCapture(i, CAP_DSHOW);
            }

            //if cam wasn't found set variable on false
            if (capture.isOpened()) {
                camConnected = true;
                capture.release();
                break;
            }
            else{
                camConnected = false;

            }
            capture.release();
        }
    }

    public boolean getCam() {
        return camConnected;
    }

    public boolean getMic() {
        return micConnected;
    }

}