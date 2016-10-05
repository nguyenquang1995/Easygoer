package fanvu.easygoer.gcm;

	
	import org.apache.commons.io.IOUtils;
	//import org.json.JSONObject;
//import org.json.JSONObject;
	import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

	// NOTE:
	// This class emulates a server for the purposes of this sample,
	// but it's not meant to serve as an example for a production app server.
	// This class should also not be included in the client (Android) application
	// since it includes the server's API key. For information on GCM server
	// implementation see: https://developers.google.com/cloud-messaging/server
	public class GcmSender {
		
		String regId = "APA91bFZ0S-SuvaIfn8PrnsrESsH3acRrXcoYwknVdsIaiDZQRvjdRkeMlCd5WQnP8A5AuBpi7v7Il9EX-MXUvTdrKqjxHATbDfSlnI9S_Y9Bx5rvJ-UZjBNIUUJH9jcoywiDglK7UFq";
		
		String regId2 ="APA91bEC821vWwRhV3G_HTyN_EIPk9WIEFaW7Wi-HZQ0Y8MohinPNSfUZ-S_6NKaokv9qU7pc7wD8_GRPaZ75NYm_kInaBUxv9x2gYxnFXAzgpH1g5CUh465iQwdcqFNoiwcKSaRH9kD";
		String regId3 ="APA91bHraof_2wprn7qBEB5VuBNZWpuC5aB73BnQocUBeRT9LB5Hrii70yReIsH52Ut9NIRa0awE17cOZHMuWAGBMaQ23XJdYihuLOeSBzLYMBupVxx2wTY";
        String regId31 ="APA91bHraof_2wprn7qBEB5VuBNZWpuC5aB73BnQocUBeRT9LB5Hrii70yReIsH52Ut9NIRa0awE17cOZHMuWAGBMaQ23XJdYihuLOeSBzLYMBupVxx2wTY";
        String regId4 ="APA91bFz_Wt6B3PFtP9y9bqjhOKVdW7wUleHxZMNWiab0ZxTIN2MGBzFj9glQjdiUci5dkQ2qje-UCRXkgku1Um__YAWpSZ0yVtDHUmtBY2uEqQlUqzqxY0";
        String jack_reg_id = "APA91bFjm6_LhBdxleJGOa4P85Q4j-iqShZ1O8nl5zlIrb3wX_RDgbme6iRHj3CjU8ouhhUEZFe_jE4CKtXu1hWxQ_AbQbx5-myRzVBlZsvlWo6XI1mvx9w";
        String quang_reg_id ="APA91bEzrn7fBhsnQNEZ2fnMDCeIy26vQozPo5AoJDlcPW2zpms-Vk-iG74OcjAvdgQGs9aCM75O_CrzxBMmC9JIOUVZG7mzvCieTREu0Ib4p9AkfkoFAOs";

	    public static final String API_KEY = "AIzaSyCqSs95yw1uqYXBd1-uOpU7xK0KWGAASlM";

	    public static void main(String[] args) {
	        if (args.length < 1 || args.length > 2 || args[0] == null) {
	            System.err.println("usage: ./gradlew run -Pmsg=\"MESSAGE\" [-Pto=\"DEVICE_TOKEN\"]");
	            System.err.println("");
	            System.err.println("Specify a test message to broadcast via GCM. If a device's GCM registration token is\n" +
	                    "specified, the message will only be sent to that device. Otherwise, the message \n" +
	                    "will be sent to all devices subscribed to the \"global\" topic.");
	            System.err.println("");
	            System.err.println("Example (Broadcast):\n" +
	                    "On Windows:   .\\gradlew.bat run -Pmsg=\"<Your_Message>\"\n" +
	                    "On Linux/Mac: ./gradlew run -Pmsg=\"<Your_Message>\"");
	            System.err.println("");
	            System.err.println("Example (Unicast):\n" +
	                    "On Windows:   .\\gradlew.bat run -Pmsg=\"<Your_Message>\" -Pto=\"<Your_Token>\"\n" +
	                    "On Linux/Mac: ./gradlew run -Pmsg=\"<Your_Message>\" -Pto=\"<Your_Token>\"");
	            System.exit(1);
	        }
	        try {
	            // Prepare JSON containing the GCM message content. What to send and where to send.
	            JSONObject jGcmData = new JSONObject();
	            JSONObject jData = new JSONObject();
	            jData.put("message", args[0].trim());
	            //jData.put("message", "Hello from easyGoer");
	            // Where to send GCM message.
	            if (args.length > 1 && args[1] != null) {
	                jGcmData.put("to", args[1].trim());
	            } else {
	                jGcmData.put("to", "/topics/global");
	            }
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
	            
	            System.out.println("data sent:..."+jGcmData.toString());

	            // Read GCM response.
	            InputStream inputStream = conn.getInputStream();
	            String resp = IOUtils.toString(inputStream);
	            System.out.println(resp);
	            System.out.println("Check your device/emulator for notification or logcat for " +
	                    "confirmation of the receipt of the GCM message.");
	        } catch (IOException e) {
	            System.out.println("Unable to send GCM message.");
	            System.out.println("Please ensure that API_KEY has been replaced by the server " +
	                    "API key, and that the device's registration token is correct (if specified).");
	            e.printStackTrace();
	        }
	    }
	    
	    public static void sendToMobiles(String message,List<String> regIds){
	    	
	    	if(regIds != null && regIds.size() >0 ){
	    		for(String regId: regIds){
	    			// Thực hiện gửi tin tới từng user có regId tương ứng.
	    			if(regId == null){
	    				break;
	    			}
	    			try {
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
	    	            
	    	            System.out.println("data sent:..."+jGcmData.toString());

	    	            // Read GCM response.
	    	            InputStream inputStream = conn.getInputStream();
	    	            String resp = IOUtils.toString(inputStream);
	    	            System.out.println(resp);
	    	            System.out.println("Check your device/emulator for notification or logcat for " +
	    	                    "confirmation of the receipt of the GCM message.");
	    	        } catch (IOException e) {
	    	            System.out.println("Unable to send GCM message.");
	    	            System.out.println("Please ensure that API_KEY has been replaced by the server " +
	    	                    "API key, and that the device's registration token is correct (if specified).");
	    	            e.printStackTrace();
	    	        }
	    		// end of sending message to one regId.	
	    		}
	    	}
	    	
	    	
	    }

	}
