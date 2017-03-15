package o.astra1st;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class UserAreaActivity extends AppCompatActivity {
    TabHost tabHost;
    //-------- var tab 1 ------------//
    //ImageSwitcher imageTutorial;
    TextView judul,penjelasan;
    CardView cardview_tutorial;
    Animation in, in1, out, out1;
    int i = 0;
    ViewAnimator viewAnimator;
    //-------- var tab 1 ------------//

    //-------- var tab 4 ------------//
    private static final int PICK_IMAGE_REQUEST = 234;
    //Buttons
    private Button buttonUpload;
    //ImageView
    private ImageView imageView;
    //a Uri object to store file path
    private Uri filePath;
    //firebase storage reference
    private StorageReference storageReference;
    //-------- var tab 4 ------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        getSupportActionBar().setElevation(0);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        host.getTabWidget().setDividerDrawable(null);




        //----------------------------Tab 1-------------------------------

        final Animation inAnim = AnimationUtils.loadAnimation(this,R.anim.in);
        final Animation outAnim = AnimationUtils.loadAnimation(this,R.anim.out);
        final Animation inAnim_1 = AnimationUtils.loadAnimation(this,R.anim.in1);
        final Animation outAnim_1 = AnimationUtils.loadAnimation(this,R.anim.out1);

        TabHost.TabSpec spec = host.newTabSpec("Petunjuk");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab1));
        host.addTab(spec);


        //imageTutorial = (ImageSwitcher) findViewById(R.id.imageTutorial);
        //judul = (TextView) findViewById(R.id.judul_tutorial);
        // penjelasan = (TextView) findViewById(R.id.penjelasan_tutorial);
        //cardview_tutorial = (CardView) findViewById(R.id.cardview_tutorial);
        /*
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
        */


        in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in);
        out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out);
        in1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in1);
        out1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out1);

        //----------------------------Tab 1-------------------------------



        //----------------------------Tab 2-------------------------------
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab2));
        host.addTab(spec);
        //----------------------------Tab 2-------------------------------


        //----------------------------Tab 3-------------------------------

        final Button start = (Button) findViewById(R.id.start);

        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab3));
        host.addTab(spec);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //download pertanyaan
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference islandRef;

                islandRef = storageRef.child("petanyaan/pertanyaan.txt");

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "CameraSample");

                final File mediaFile;
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "pertanyaan"+".txt");



                islandRef.getFile(mediaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //read file.txt
                        try {
                            FileInputStream fIn = new FileInputStream(mediaFile);
                            BufferedReader myReader = new BufferedReader(
                                    new InputStreamReader(fIn));
                            String aDataRow = "";
                            String aBuffer = "";
                            while ((aDataRow = myReader.readLine()) != null) {
                                aBuffer += aDataRow + "\n";
                            }
                            myReader.close();
                            final String[] separated = aBuffer.split(",");
                            int length = separated.length;
                            Toast.makeText(getApplicationContext(),"downloaded yeay = " + length, Toast.LENGTH_LONG).show();

                            new CountDownTimer(1000, 1000)
                            {
                                public void onTick(long millisUntilFinished) {   }

                                //delay 1 detik terus ganti halaman
                                public void onFinish()
                                {
                                    Intent intent = new Intent(UserAreaActivity.this, MainActivity.class);
                                    intent.putExtra("daftarPertanyaan", separated);
                                    UserAreaActivity.this.startActivity(intent);
                                }
                            }.start();

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



            }

        });


        //----------------------------Tab 3-------------------------------

        //----------------------------Tab 4-------------------------------
        buttonUpload = (Button) findViewById(R.id.upload);
        storageReference = FirebaseStorage.getInstance().getReference();

        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab4));
        host.addTab(spec);



        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadFile();
            }
        });


        //----------------------------Tab 4-------------------------------
    }



    private void uploadFile() {
        //if there is a file to upload
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample" + File.separator +
                "VID_"+ "1" + ".mp4");

        Intent data = new Intent();
        data.setData(Uri.fromFile(file));
        filePath = data.getData();

        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/VID_1.mp4");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "faill", Toast.LENGTH_LONG).show();
        }
    }


}
