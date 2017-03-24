package o.astra1st;


import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.media.CameraHelper;


public class MainActivity extends AppCompatActivity {


    private Camera mCamera;
    private TextureView mPreview;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;
    private TextView timer,pertanyaan;
    private boolean isInterview = true;
    private boolean isInterviewdone = false;
    private int counter_pertanyaan = 0;
    private CountDownTimer cdt_persiapan ,cdt_record;
    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private ImageButton captureButton;
    private CircularProgressBar circularProgressBar;
    String[] separated;
    ArrayList<Integer> durasipertanyaan = new ArrayList<>();
    String id;

    //button animation
    float[] hsv;
    int runColor;
    int hue = 0;
    ValueAnimator anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreview = (TextureView) findViewById(R.id.surfaceView);
        captureButton = (ImageButton) findViewById(R.id.control_interview);
        timer = (TextView) findViewById(R.id.timer);
        pertanyaan = (TextView) findViewById(R.id.pertanyaan);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.progressbar);

        Bundle bundle = getIntent().getExtras();
        separated = bundle.getStringArray("daftarPertanyaan");
        durasipertanyaan = bundle.getIntegerArrayList("durasiPertanyaan");
        id = bundle.getString("id");

        //button animation
        //button animation



         captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Switchfor();
            }
        });
    }

    public void Switchfor()
    {
        if (counter_pertanyaan < separated.length)
        {
            //siap siap
            pertanyaan.setText("siapsiap");

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (8*scale + 0.5f);
            captureButton.setPadding(dpAsPixels,0,0,0);
            captureButton.setImageResource(R.drawable.play);

            cdt_persiapan = new CountDownTimer(4000, 1000)
            {
                //tampilin sisa waktu
                public void onTick(long millisUntilFinished)
                {
                    timer.setText("" + millisUntilFinished / 1000);
                }

                public void onFinish()
                {
                    //enable button stop
                    //captureButton.setEnabled(true);
                    //interview
                    captureButton.setEnabled(true);
                    captureButton.setImageResource(R.drawable.stop);
                    float scale = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (1*scale + 0.5f);
                    captureButton.setPadding(dpAsPixels,0,0,0);

                    captureButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (isRecording == true)
                            {
                                cdt_record.cancel();
                                circularProgressBar.setProgress(0);
                                circularProgressBar.clearFocus();
                                circularProgressBar.clearAnimation();
                                onCaptureClick();
                                counter_pertanyaan++;
                                Switchfor();
                            }
                        }
                    });

                    pertanyaan.setText(separated[counter_pertanyaan]);
                    //mulai ngerekam
                    if (isRecording == false)
                    {
                        onCaptureClick();
                    }


                    cdt_record = new CountDownTimer((durasipertanyaan.get(counter_pertanyaan)*1000), 1000)
                    {
                        //tampilin sisa waktu
                        public void onTick(long millisUntilFinished) {
                            timer.setText("" + millisUntilFinished / 1000);
                        }

                        //stop jika waktu habis
                        public void onFinish() {
                            //stop ngerekam
                            circularProgressBar.setProgress(0);
                            if (isRecording == true)
                            {
                                onCaptureClick();
                                counter_pertanyaan++;
                                Switchfor();
                            }


                        }
                    }.start();
                    //buat progress bar
                    circularProgressBar.setProgress(100);
                    circularProgressBar.setColor(Color.parseColor("#D50000"));
                    circularProgressBar.setProgressWithAnimation(0,(durasipertanyaan.get(counter_pertanyaan)*1000));

                }
            }.start();
            captureButton.setEnabled(false);
            //buat progress bar
            circularProgressBar.setColor(Color.parseColor("#64DD17"));
            circularProgressBar.setProgressWithAnimation(100,3900);
        }
        else
        {
            pertanyaan.setText("beres!!!");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, UserAreaActivity.class);
                    intent.putExtra("id", id);
                    MainActivity.this.startActivity(intent);
                }
            }, 1000);





        }

    }
    /**
     * The capture button controls all user interaction. When recording, the button click
     * stops recording, releases {@link android.media.MediaRecorder} and {@link android.hardware.Camera}. When not recording,
     * it prepares the {@link android.media.MediaRecorder} and starts recording.
     *
     *
     */
    public void onCaptureClick() {
        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)


            // stop recording and release camera
            try {
                mMediaRecorder.stop();  // stop the recording
            } catch (RuntimeException e) {
                // RuntimeException is thrown when stop() is called immediately after start().
                // In this case the output file is not properly constructed ans should be deleted.
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
                //noinspection ResultOfMethodCallIgnored
                mOutputFile.delete();
            }
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            setCaptureButtonText("Start");
            isRecording = false;

            //releaseCamera();
            // END_INCLUDE(stop_release_media_recorder)

        } else {

            // BEGIN_INCLUDE(prepare_start_media_recorder)

            new MediaPrepareTask().execute(null, null, null);

            // END_INCLUDE(prepare_start_media_recorder)

        }
    }

    private void setCaptureButtonText(String title) {
        //captureButton.setText(title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){

        // BEGIN_INCLUDE (configure_preview)
        mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        //parameters.setRotation(0);
        mCamera.setParameters(parameters);
        //mCamera.setDisplayOrientation(90);


        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
            return false;
        }

        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        mCamera.setDisplayOrientation(90);
        mMediaRecorder.setOrientationHint(270);

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);


        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT );
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
        if (mOutputFile == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(mOutputFile.toString());
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                MainActivity.this.finish();
            }
            // inform the user that recording has started
            setCaptureButtonText("Stop");

        }
    }

}
