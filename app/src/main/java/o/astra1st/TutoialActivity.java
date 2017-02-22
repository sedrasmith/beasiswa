package o.astra1st;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class TutoialActivity extends AppCompatActivity {

    Integer[] image = {R.drawable.coba_1, R.drawable.coba_4, R.drawable.coba_3, R.drawable.coba_2};
    ImageSwitcher imageTutorial;
    ImageButton next, prev;
    Animation in, in1, out, out1;
    int i = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoial);

        /*
        imageTutorial = (ImageSwitcher) findViewById(R.id.imageTutorial);

        imageTutorial.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(
                        new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });


        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
        in1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in1);
        out1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out1);


        next = (ImageButton) findViewById(R.id.next1);
        prev = (ImageButton) findViewById(R.id.prev1);

        imageTutorial.setImageResource(image[0]);

        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                imageTutorial.setInAnimation(in);
                imageTutorial.setOutAnimation(out);

                if (i > 0) {
                    i--;
                    imageTutorial.setImageResource(image[i]);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                imageTutorial.setInAnimation(in1);
                imageTutorial.setOutAnimation(out1);
                if (i < image.length - 1) {
                    i++;
                    imageTutorial.setImageResource(image[i]);
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        */
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Tutoial Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
