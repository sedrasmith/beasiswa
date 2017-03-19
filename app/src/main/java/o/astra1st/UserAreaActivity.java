package o.astra1st;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
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
    TabHost host;
    //-------- var tab 1 ------------//
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    //-------- var tab 1 ------------//

    //-------- var tab 2 ------------//
    private ViewPager viewPager2;
    private ViewPagerAdapter viewPagerAdapter2;
    private LinearLayout dotsLayout2;
    private TextView[] dots2;
    private int[] layouts2;
    private Button btnSkip2, btnNext2;
    //-------- var tab 2 ------------//




    //-------- var tab 4 ------------//
    private static final int PICK_IMAGE_REQUEST = 234;
    //Buttons
    private CircularProgressButton buttonUpload;
    //ImageView
    private ImageView imageView;
    //a Uri object to store file path
    private Uri filePath;
    //firebase storage reference
    private StorageReference storageReference;
    //progress upload
    int jumlahfile=0,counterupload,flag=1;
    //-------- var tab 4 ------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);




        getSupportActionBar().setElevation(0);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        host.getTabWidget().setDividerDrawable(null);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if("Petunjuk".equals(tabId)) {

                    layouts = new int[]{
                            R.layout.slide1,
                            R.layout.slide2,
                            R.layout.slide3,
                            R.layout.slide4,
                            R.layout.slide5};
                }

                else if("Tab Two".equals(tabId)) {

                    layouts[0] = R.layout.slide6;
                    layouts[1] = R.layout.slide7;
                    layouts[2] = R.layout.slide8;

                }

            }});

        //----------------------------Tab 1-------------------------------

        TabHost.TabSpec spec = host.newTabSpec("Petunjuk");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab1));
        host.addTab(spec);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // adding bottom dots
        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        //----------------------------Tab 1-------------------------------



        //----------------------------Tab 2-------------------------------
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab2));
        host.addTab(spec);

        viewPager2 = (ViewPager) findViewById(R.id.view_pager2);
        dotsLayout2 = (LinearLayout) findViewById(R.id.layoutDots2);
        btnSkip2 = (Button) findViewById(R.id.btn_skip2);
        btnNext2 = (Button) findViewById(R.id.btn_next2);

        layouts2 = new int[]{
                R.layout.slide6,
                R.layout.slide7,
                R.layout.slide8};

        addBottomDots2(0);

        viewPagerAdapter2 = new ViewPagerAdapter();
        viewPager2.setAdapter(viewPagerAdapter2);
        viewPager2.addOnPageChangeListener(viewPagerPageChangeListener);

        host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
        //----------------------------Tab 2-------------------------------


        //----------------------------Tab 3-------------------------------

        final Button start = (Button) findViewById(R.id.start);

        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab3));
        host.addTab(spec);

        //host.getTabWidget().getChildTabViewAt(2).setEnabled(false);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //download pertanyaan
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference islandRef;

                islandRef = storageRef.child("petanyaan/pertanyaan.txt");

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Pertanyaan");

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
                                aBuffer += aDataRow;
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

        File[] dir;
        TextView keterangan;
        ///debug
        buttonUpload = (CircularProgressButton) findViewById(R.id.upload_fancy);

        storageReference = FirebaseStorage.getInstance().getReference();

        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab4));
        host.addTab(spec);

        host.getTabWidget().getChildTabViewAt(3).setEnabled(true);

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        //+ File.separator + "VID_"+ "1" + ".mp4");

        dir = file.listFiles();




        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonUpload.setClickable(false);
                uploadFile();

            }
        });


        //----------------------------Tab 4-------------------------------
    }



    private void uploadFile() {
        //if there is a file to upload

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        //+ File.separator + "VID_"+ "1" + ".mp4");

        File[] direktori = file.listFiles();
        jumlahfile = direktori.length;

        for (int u = 0; u < direktori.length; u++)
        {
            counterupload = u + 1;

            Intent data = new Intent();
            data.setData(Uri.fromFile(direktori[u]));
            filePath = data.getData();

            if (filePath != null) {
                //displaying a progress dialog while upload is going on
                //tambah counter upload

                StorageReference riversRef = storageReference.child("images/VID_" + u + ".mp4");
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog

                                buttonUpload.setClickable(true);

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                buttonUpload.setClickable(true);

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                int progress_int = (int)progress*counterupload/jumlahfile;

                                if(progress_int != 0)
                                {
                                    buttonUpload.setProgress(progress_int);
                                }
                                else
                                {
                                    buttonUpload.setProgress(1);
                                }


                                //displaying percentage in progress dialog

                            }
                        });
                buttonUpload.setProgress(50);
            }
            else {
                Toast.makeText(getApplicationContext(), "faill", Toast.LENGTH_LONG).show();
                buttonUpload.setProgress(-1);
                buttonUpload.setClickable(true);
            }
        }


    }

    //----------------- prosedur dan fungsi tab 1-----------------//

    public  void btnSkipClick(View v)
    {
        //launchHomeScreen();
    }

    public  void btnNextClick(View v)
    {
        // checking for last page
        // if last page home screen will be launched
        int current = getItem(1);
        if (current < layouts.length) {
            // move to next screen
            viewPager.setCurrentItem(current);
        } else {
            //launchHomeScreen();
        }
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#90A4AE"));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#232b2b"));
    }

    private void addBottomDots2(int currentPage) {
        dots2 = new TextView[layouts2.length];

        dotsLayout2.removeAllViews();
        for (int i = 0; i < dots2.length; i++) {
            dots2[i] = new TextView(this);
            dots2[i].setText(Html.fromHtml("&#8226;"));
            dots2[i].setTextSize(35);
            dots2[i].setTextColor(Color.parseColor("#90A4AE"));
            dotsLayout2.addView(dots2[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#232b2b"));
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;


        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                Button verifikasi1 = (Button) findViewById(R.id.verifikasi1);
                    verifikasi1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            host.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                            host.setCurrentTab(1);
                        }
                    });
                    break;
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    //----------------- prosedur dan fungsi tab 1-----------------//


}
