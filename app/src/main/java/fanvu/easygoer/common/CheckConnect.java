package fanvu.easygoer.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnect {
	 
    private Context _context;
 
    //Hàm dựng khởi tạo đối tượng
    public CheckConnect(Context context) {
        this._context = context;
    }
 
    public boolean checkMobileInternetConn() {
        //Tạo đối tương ConnectivityManager để trả về thông tin mạng
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //Nếu đối tượng khác null
        if (connectivity != null) {
            //Nhận thông tin mạng
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                //Tìm kiếm thiết bị đã kết nối được internet chưa
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean checkData() {
//        TelephonyManager tm = (TelephonyManager) this._context.getSystemService(Context.TELEPHONY_SERVICE);
//        Integer a= tm.getDataState();
//        if (tm.getDataState() == TelephonyManager.DATA_CONNECTED) {
//                        return true;
//        }
//        return false;
    	  //Tạo đối tương ConnectivityManager để trả về thông tin mạng
    	  ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    		        return true;
    		    }
    		    return false;
}

 
}