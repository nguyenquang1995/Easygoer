package fanvu.easygoer.gcm;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// NOTE:
// This class emulates a server for the purposes of this sample,
// but it's not meant to serve as an example for a production app server.
// This class should also not be included in the client (Android) application
// since it includes the server's API key. For information on GCM server
// implementation see: https://developers.google.com/cloud-messaging/server
public class GcmSender {
    String regId =
        "APA91bFZ0S-SuvaIfn8PrnsrESsH3acRrXcoYwknVdsIaiDZQRvjdRkeMlCd5WQnP8A5AuBpi7v7Il9EX-MXUvTdrKqjxHATbDfSlnI9S_Y9Bx5rvJ-UZjBNIUUJH9jcoywiDglK7UFq";
    String regId2 =
        "APA91bEC821vWwRhV3G_HTyN_EIPk9WIEFaW7Wi-HZQ0Y8MohinPNSfUZ-S_6NKaokv9qU7pc7wD8_GRPaZ75NYm_kInaBUxv9x2gYxnFXAzgpH1g5CUh465iQwdcqFNoiwcKSaRH9kD";
    String regId3 =
        "APA91bHraof_2wprn7qBEB5VuBNZWpuC5aB73BnQocUBeRT9LB5Hrii70yReIsH52Ut9NIRa0awE17cOZHMuWAGBMaQ23XJdYihuLOeSBzLYMBupVxx2wTY";
    String regId31 =
        "APA91bHraof_2wprn7qBEB5VuBNZWpuC5aB73BnQocUBeRT9LB5Hrii70yReIsH52Ut9NIRa0awE17cOZHMuWAGBMaQ23XJdYihuLOeSBzLYMBupVxx2wTY";
    String regId4 =
        "APA91bFz_Wt6B3PFtP9y9bqjhOKVdW7wUleHxZMNWiab0ZxTIN2MGBzFj9glQjdiUci5dkQ2qje-UCRXkgku1Um__YAWpSZ0yVtDHUmtBY2uEqQlUqzqxY0";
    String jack_reg_id =
        "APA91bFjm6_LhBdxleJGOa4P85Q4j-iqShZ1O8nl5zlIrb3wX_RDgbme6iRHj3CjU8ouhhUEZFe_jE4CKtXu1hWxQ_AbQbx5-myRzVBlZsvlWo6XI1mvx9w";
    public static String quang_reg_id =
        "APA91bGnPR7VXg9I093WDmR0wHK3d5jcdfwlkcAbJIl5ijnq-60A7IG3TeShjseIP5R3EMaETrx7ME_GHvss-eQqU8oa2vb4E2oknB54Fz58BKDqx8PEjcE";
    public static final String API_KEY = "AIzaSyCqSs95yw1uqYXBd1-uOpU7xK0KWGAASlM";

    public static void sendMessageToClient(String registerID, String message)
        throws JSONException, IOException {
        JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("message", message);
        jGcmData.put("to", registerID);
        /*else {
            jGcmData.put("to", "/topics/global");
        }*/
        jGcmData.put("data", jData);
        URL url = new URL("https://android.googleapis.com/gcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "key=" + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(jGcmData.toString().getBytes());
        InputStream inputStream = conn.getInputStream();
        String resp = IOUtils.toString(inputStream);
    }

    /*public static void sendToMobiles(String message, List<String> regIds)
        throws JSONException, IOException {
        if (regIds != null && regIds.size() > 0) {
            for (String regId : regIds) {
                // Thực hiện gửi tin tới từng user có regId tương ứng.
                if (regId == null) {
                    break;
                }
                // Prepare JSON containing the GCM message content. What to send and where to send.
                JSONObject jGcmData = new JSONObject();
                JSONObject jData = new JSONObject();
                jData.put("message", message);
                jGcmData.put("to", regId);
                // What to send in GCM message.
                jGcmData.put("data", jData);
                // Create connection to send GCM Message request.
                URL url = new URL("https://android.googleapis.com/gcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "key=" + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                // Send GCM message content.
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(jGcmData.toString().getBytes());
                System.out.println("data sent:..." + jGcmData.toString());
                // Read GCM response.
                InputStream inputStream = conn.getInputStream();
                String resp = IOUtils.toString(inputStream);
                // end of sending message to one regId.
            }
        }
    }*/
}

