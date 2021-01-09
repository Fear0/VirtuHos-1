package de.uni_hannover.wb_interaktionen_1.website;

import java.io.IOException;

/** This class can open a url in the standard browser.
 *
 * @author David Sasse
 */
public class WebsiteOpener {

    private String bbburl;

    /** The constructor for the WebsiteOpener.
     *
     * @param url The url that has to be opened.
     */
    public WebsiteOpener(String url){
        this.bbburl = url;
    }

    /** Opens the URL
     *
     * This methode opens the URL, that is passed in the construtor in the standard webbrowser.
     */
    public void open() {
        Runtime rt = Runtime.getRuntime();
        try {
            // We have to distinguish between the different operatin systems, because the commandos to open the browser are different.
            if (isWindows()) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + bbburl).waitFor();
            } else if (isMac()) {
                String[] cmd = {"open", bbburl};
                rt.exec(cmd).waitFor();
            } else if (isUnix()) {
                String[] cmd = {"xdg-open", bbburl};
                rt.exec(cmd).waitFor();
            } else {
                try {
                    throw new IllegalStateException();
                } catch (IllegalStateException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /** Checks if operating System is Windows.
     *
     * @return True, if the operating system is Windows and false otherwise.
     */
    public static boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("win");
    }

    /** Checks if operating System is Mac OS.
     *
     * @return True, if the operating system is Mac OS and false otherwise.
     */
    public static boolean isMac() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("mac");
    }

    /** Checks if operating System is Unix.
     *
     * @return True, if the operating system is Unix and false otherwise.
     */
    public static boolean isUnix() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0;
    }
}
