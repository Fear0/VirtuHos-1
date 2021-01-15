package wb.analyse1.bbbapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Does https requests to communicate with a BBB API
 */
public class BBBRequest {

    private static final String url = "https://bbb2.se.uni-hannover.de/bigbluebutton/api/";
    private static final String secret = "Z69sY1UtTsFRg2jXez1lNhfG4f5dTza4ZLZSixTycI";

    /**
     * constructor; nothing to do here for now
     */
    public BBBRequest() {
    }

    /**
     * Calculates a checksum in order to authorize
     *
     * @param text request data
     * @return SHA-1 checksum over text and secret
     */
    private static String checksum(String... text) {
        MessageDigest encoder;
        try {
            encoder = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        String msg = String.join("", text) + secret;
        byte[] byteHash = encoder.digest(msg.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, byteHash));
    }

    /**
     * Sends a get request
     *
     * @param methodName name of method
     * @param query      query to append
     * @return BBB server XML response
     */
    protected String get(String methodName, String query) {

        String uri = url + methodName + "?";
        if (query != null) {
            String sum = checksum(methodName, query);
            uri += query + "&checksum=" + sum;
        } else {
            String sum = checksum(methodName);
            uri += "checksum=" + sum;
        }
        if (methodName.equals("join")) {
            return uri;
        }
        try {
            URL u = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder res = new StringBuilder();

            String l = "";
            while ((l = reader.readLine()) != null) {
                res.append(l);
            }

            reader.close();
            //do not get conn.disconnect (will be handled automatically)

            return res.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
