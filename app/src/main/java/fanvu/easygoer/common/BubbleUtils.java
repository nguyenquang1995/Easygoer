package fanvu.easygoer.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import fanvu.easygoer.LoginActivity;
import fanvu.easygoer.gcm.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by framgia on 29/09/2016.
 */
public class BubbleUtils {
    public static BubblesManager bubblesManager;

    public static void addBubble(final Context context, final int number) {
        bubblesManager = new BubblesManager.Builder(context)
            .setTrashLayout(R.layout.bubble_trash_layout)
            .setInitializationCallback(new OnInitializedCallback() {
                @Override
                public void onInitialized() {
                    final BubbleLayout
                        bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R
                        .layout
                        .bubble_layout, null);
                    bubbleLayout.setShouldStickToWall(true);
                    bubbleLayout.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
                        @Override
                        public void onBubbleClick(BubbleLayout bubble) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    });
                    bubbleLayout.setOnBubbleRemoveListener(
                        new BubbleLayout.OnBubbleRemoveListener() {
                            @Override
                            public void onBubbleRemoved(BubbleLayout bubble) {
                                bubblesManager.removeBubble(bubbleLayout);
                                bubblesManager.recycle();
                            }
                        });
                    ImageView imageView = (ImageView) bubbleLayout.findViewById(R.id.avatar);
                    Glide.with(context).load(R.drawable.ic_launcher)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(imageView);
                    String text = Integer.toString(number);
                    TextView textView = (TextView) bubbleLayout.findViewById(R.id.number);
                    textView.setText(text);
                    bubblesManager.addBubble(bubbleLayout, 60, 20);
                }
            })
            .build();
        bubblesManager.initialize();
    }
}
