package fanvu.easygoer.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import fanvu.easygoer.gcm.R;
import fanvu.easygoer.activity.NotificationActivity;

/**
 * Created by framgia on 27/09/2016.
 */
public class Utils {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static ImageView chatHead;
    public static int CHAT_SIZE = 200;
    public static int TEXT_SIZE = 300;
    public static int PADDING = TEXT_SIZE / 2;
    public static Bitmap sBitmap;

    public static void addView(final Context context, int number) {
        WindowManager windowManager = (WindowManager) context.getSystemService
            (Context.WINDOW_SERVICE);
        chatHead = new ImageView(context);
        chatHead.setImageDrawable(drawNumber(context, number));
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
            CHAT_SIZE,
            CHAT_SIZE,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(chatHead, params);
    }

    public static BitmapDrawable drawNumber(Context context, int number) {
        String text;
        if (number < 100) {
            text = Integer.toString(number);
        } else {
            text = "99+";
        }
        Bitmap result = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher)
            .copy(Bitmap.Config.ARGB_8888, true);
        sBitmap = Bitmap.createBitmap(result.getWidth() + PADDING * 3, result.getHeight() +
                PADDING * 3,
            Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sBitmap);
        canvas.drawBitmap(result, PADDING, PADDING * 2, new Paint());
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(TEXT_SIZE);
        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setColor(Color.RED);
        paint2.setAntiAlias(true);
        paint2.setTextSize(TEXT_SIZE);
        canvas.drawCircle(TEXT_SIZE, TEXT_SIZE, TEXT_SIZE, paint2);
        canvas.drawText(text, PADDING / 2, TEXT_SIZE * 3 / 2, paint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), sBitmap);
        result.recycle();
        return bitmapDrawable;
    }

    public static void removeView(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService
            (Context.WINDOW_SERVICE);
        if (chatHead != null) {
            windowManager.removeView(chatHead);
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
}
