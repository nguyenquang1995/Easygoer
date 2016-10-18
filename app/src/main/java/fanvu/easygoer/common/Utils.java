package fanvu.easygoer.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import fanvu.easygoer.Constant;
import fanvu.easygoer.Controller;
import fanvu.easygoer.activity.NotificationActivity;
import fanvu.easygoer.gcm.Config;
import fanvu.easygoer.gcm.R;

/**
 * Created by framgia on 27/09/2016.
 */
public class Utils {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static ImageView chatHead;
    public static int CHAT_SIZE;
    public static int TEXT_SIZE;
    public static int CIRCLE_SIZE;
    public static final int PADDING_CIRCLE = 20;
    public static Bitmap sBitmap;

    public static void addView() {
        final Context context = Controller.getInstanceContext();
        if (chatHead != null) {
            removeView(context);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant
            .SHARE_PREFERENCE, Context.MODE_PRIVATE);
        int number = sharedPreferences.getInt(Constant.NUM_NOTIFICATION, 0);
        WindowManager windowManager = (WindowManager) context.getSystemService
            (Context.WINDOW_SERVICE);
        chatHead = new ImageView(context);
        chatHead.setImageBitmap(drawNumber(context, number));
        chatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(context);
                Intent intent = new Intent(context, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(chatHead, params);
    }

    public static Bitmap drawNumber(Context context, int number) {
        String text;
        if (number < 100) {
            text = Integer.toString(number);
        } else {
            text = "99+";
        }
        Bitmap result = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)
            .copy(Bitmap.Config.ARGB_8888, true);
        CHAT_SIZE = result.getWidth();
        TEXT_SIZE = CHAT_SIZE / 4;
        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setColor(Color.RED);
        paint2.setAntiAlias(true);
        paint2.setTextSize(TEXT_SIZE);
        paint2.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        paint2.getTextBounds(text, 0, text.length(), bounds);
        CIRCLE_SIZE = bounds.width() / 2 + PADDING_CIRCLE;
        sBitmap = Bitmap.createBitmap(result.getWidth() + CIRCLE_SIZE, result.getHeight() +
                CIRCLE_SIZE,
            Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sBitmap);
        canvas.drawBitmap(result, CIRCLE_SIZE, CIRCLE_SIZE, new Paint());
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawCircle(CIRCLE_SIZE, CIRCLE_SIZE, CIRCLE_SIZE, paint2);
        canvas.drawText(text, CIRCLE_SIZE - bounds.width() / 2, CIRCLE_SIZE + bounds.height() / 2,
            paint);
        result.recycle();
        return sBitmap;
    }

    public static void removeView(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService
            (Context.WINDOW_SERVICE);
        if (chatHead != null) {
            windowManager.removeView(chatHead);
            Utils.sBitmap.recycle();
        }
    }

    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show();
            } else {
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static void displayMessageOnScreen(Context context, String message) {
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
