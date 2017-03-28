package o.astra1st;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
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
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;


public class UserAreaActivity extends AppCompatActivity {
    TabHost tabHost;
    TabHost host;

    boolean istab1 = false, istutor1_done = false, istutor2_done = false, isinterview_done = false, load_awal = false;
    ProgressDialog prog_dial;



    //-------- firebase ------------//

    FirebaseDatabase database;
    DatabaseReference myRef;
    username user;
    String id;

    //-------- firebase ------------//


    //-------- var tab 1 ------------//
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext, verifikasi1;
    //-------- var tab 1 ------------//

    //-------- var tab 2 ------------//
    private ViewPager viewPager2;
    private ViewPagerAdapter viewPagerAdapter2;
    private LinearLayout dotsLayout2;
    private TextView[] dots2;
    private int[] layouts2;
    private Button btnSkip2, btnNext2, verifikasi2;
    //-------- var tab 2 ------------//

    //-------- var tab 3 ------------//
    private TextView jumlah_pertanyaan;// = (TextView) findViewById(R.id.jumlah_pertanyaan_text);
    private TextView total_waktu;// = (TextView) findViewById(R.id.total_menit_text);
    private TextView jumlah_data;// = (TextView) findViewById(R.id.total_data_text);
    private TextView nama;// = (TextView) findViewById(R.id.nama);
    private TextView ttl;// = (TextView) findViewById(R.id.ttl);
    private String[] separated;
    private ArrayList<Integer> durasipertanyaan = new ArrayList<Integer>();
    private String[] durasi;
    //-------- var tab 3 ------------//

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
    File file_to_be_uploaded;
    File[] direktori;
    int jumlahfile=0,counterupload = 0,flag=1;
    private TextView jumlah_video;
    private TextView jumlah_uploaded;
    private TextView jumlah_data_tab4;
    private TextView nama_tab4;
    private TextView ttl_tab4;
    //-------- var tab 4 ------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        if (!load_awal)
        {
            prog_dial = new ProgressDialog(this);
            prog_dial.setProgressStyle(R.style.SpinKitView_FoldingCube);
            CubeGrid doubleBounce = new CubeGrid();
            doubleBounce.setColor(Color.parseColor("#232b2b"));
            prog_dial.setIndeterminateDrawable(doubleBounce);
            prog_dial.setCanceledOnTouchOutside(false);
            prog_dial.setMessage("sync");
            prog_dial.show();
        }

        getSupportActionBar().setElevation(0);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        host.getTabWidget().setDividerDrawable(null);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if("Petunjuk".equals(tabId))
                {
                    layouts = new int[]{
                            R.layout.slide1,
                            R.layout.slide2,
                            R.layout.slide3,
                            R.layout.slide4,
                            R.layout.slide9,
                            R.layout.slide5};
                    istab1 = true;
                    setTitle("Persiapan");
                }

                else if("Tab Two".equals(tabId))
                {
                    layouts[0] = R.layout.slide6;
                    layouts[1] = R.layout.slide7;
                    layouts[2] = R.layout.slide8;
                    layouts[3] = R.layout.slide10;
                    layouts[4] = R.layout.slide11;
                    layouts[5] = R.layout.silde12;
                    istab1 = false;
                    setTitle("Petunjuk Penggunaan");
                }

                else if("Tab Three".equals(tabId))
                {
                    setTitle("Interview");
                }

                else if("Tab Four".equals(tabId))
                {
                    setTitle("Upload");
                }

            }});




        //----------------------------Tab 1-------------------------------

        TabHost.TabSpec spec = host.newTabSpec("Petunjuk");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab1));
        host.addTab(spec);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        //btnSkip = (Button) findViewById(R.id.btn_skip);
       // btnNext = (Button) findViewById(R.id.btn_next);


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
       // btnSkip2 = (Button) findViewById(R.id.btn_skip2);
        //btnNext2 = (Button) findViewById(R.id.btn_next2);

        host.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.persiapan)
                {
                    Toast.makeText(getApplicationContext(),"selesaikan persiapan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    host.setCurrentTab(0);
                }
                else
                {
                    host.setCurrentTab(1);
                }
            }
        });

        layouts2 = new int[]{
                R.layout.slide6,
                R.layout.slide7,
                R.layout.slide8,
                R.layout.slide10,
                R.layout.slide11,
                R.layout.silde12};

        addBottomDots2(0);

        viewPagerAdapter2 = new ViewPagerAdapter();
        viewPager2.setAdapter(viewPagerAdapter2);
        viewPager2.addOnPageChangeListener(viewPagerPageChangeListener);


        //host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
        //----------------------------Tab 2-------------------------------


        //----------------------------Tab 3-------------------------------

        final Button start = (Button) findViewById(R.id.start);
        jumlah_pertanyaan = (TextView) findViewById(R.id.jumlah_pertanyaan_text);
        total_waktu = (TextView) findViewById(R.id.total_menit_text);
        jumlah_data = (TextView) findViewById(R.id.total_data_text);
        nama = (TextView) findViewById(R.id.nama);
        ttl = (TextView) findViewById(R.id.ttl);

        if (isinterview_done)
        {
            start.setClickable(false);
            start.setBackgroundColor(Color.parseColor("#8BC34A"));
        }

        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab3));
        host.addTab(spec);

        host.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.persiapan && !user.petunjuk_penggunaan)
                {
                    Toast.makeText(getApplicationContext(),"selesaikan persiapan & petunjuk penggunaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    host.setCurrentTab(0);
                }
                else  if(!user.petunjuk_penggunaan && user.persiapan)
                {
                    Toast.makeText(getApplicationContext(),"selesaikan petunjuk penggunaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    host.setCurrentTab(1);
                }
                else
                {
                    host.setCurrentTab(2);
                }
            }
        });

        //host.getTabWidget().getChildTabViewAt(2).setEnabled(false);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //download pertanyaan
                start.setClickable(false);

                start.setBackgroundColor(Color.parseColor("#8BC34A"));


                new CountDownTimer(1000, 1000)
                {
                    public void onTick(long millisUntilFinished) {   }

                    //delay 1 detik terus ganti halaman
                    public void onFinish()
                    {
                        user.interview = true;
                        myRef.child("user").child(id).child("interview").setValue(true);

                        Intent intent = new Intent(UserAreaActivity.this, MainActivity.class);
                        intent.putExtra("daftarPertanyaan", separated);
                        intent.putExtra("durasiPertanyaan", durasipertanyaan);
                        intent.putExtra("id", id);
                        UserAreaActivity.this.startActivity(intent);
                    }
                }.start();

                /*
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

                            //final String[] separated = user.pertanyaan.split(",");
                            //final ArrayList<Integer> durasipertanyaan = new ArrayList<Integer>();
                            //final String[] durasi = user.durasi.split(",");

                            //for(int i = 0; i < durasi.length; i++)
                            //{
                            //    durasipertanyaan.add(i, Integer.parseInt(durasi[i]));
                            //}



                            //int length = separated.length;
                            //Toast.makeText(getApplicationContext(),"downloaded yeay = " + length, Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        start.setClickable(true);
                        Toast.makeText(getApplicationContext(),"fail to sync", Toast.LENGTH_SHORT).show();
                    }
                });
                */

            }
        });


        //----------------------------Tab 3-------------------------------

        //----------------------------Tab 4-------------------------------
        File[] dir;
        TextView keterangan;

        buttonUpload = (CircularProgressButton) findViewById(R.id.upload_fancy);
        jumlah_video = (TextView) findViewById(R.id.jumlah_video);
        jumlah_uploaded = (TextView) findViewById(R.id.video_uploaded);
        jumlah_data_tab4 = (TextView) findViewById(R.id.total_data_text_4);
        nama_tab4 = (TextView) findViewById(R.id.nama_tab4);
        ttl_tab4 = (TextView) findViewById(R.id.ttl_tab4);

        storageReference = FirebaseStorage.getInstance().getReference();

        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",  getResources().getDrawable(R.drawable.tab4));
        host.addTab(spec);

        //host.getTabWidget().getChildTabViewAt(3).setEnabled(false);

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        //+ File.separator + "VID_"+ "1" + ".mp4");

        dir = file.listFiles();


        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonUpload.setClickable(false);
                file_to_be_uploaded = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
                direktori = file_to_be_uploaded.listFiles();
                jumlahfile = direktori.length;
                uploadFile();

            }
        });


        //----------------------------Tab 4-------------------------------

        //----------------------------FIREBASE-------------------------------
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");


        // Read from the database
        myRef.child("user").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = dataSnapshot.getValue(username.class);

                jumlah_uploaded.setText(Integer.toString(user.uploaded));


                if(!load_awal)
                {
                    setdata_tab3();
                    setdata_tab4();
                    isinterview_done = user.interview;
                    if (isinterview_done)
                    {
                        start.setClickable(false);
                        start.setBackgroundColor(Color.parseColor("#8BC34A"));
                        start.setText("interview done");
                        host.setCurrentTab(3);
                    }

                    Toast.makeText(getApplicationContext(),"sync done", Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            prog_dial.dismiss();
                        }
                    }, 1000);

                    load_awal = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"tidak dapat sync", Toast.LENGTH_SHORT).show();

            }
        });
        //----------------------------FIREBASE-------------------------------


    }



    private void uploadFile()
    {
        //if there is a file to upload

        //File file_to_be_uploaded = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        //+ File.separator + "VID_"+ "1" + ".mp4");

        //File[] direktori = file_to_be_uploaded.listFiles();
        //jumlahfile = direktori.length;

        //for (int u = 0; u < direktori.length; u++)
        //{
        //}
            Intent data = new Intent();
            data.setData(Uri.fromFile(direktori[counterupload]));
            filePath = data.getData();

            if (filePath != null)
            {
                //displaying a progress dialog while upload is going on
                //tambah counter upload

                StorageReference riversRef = storageReference.child( id + "/VID_" + counterupload + ".mp4");
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                if(counterupload < (direktori.length-1))
                                {
                                    counterupload++;
                                    uploadFile();
                                    myRef.child("user").child(id).child("uploaded").setValue(counterupload);
                                }
                                else
                                {
                                    buttonUpload.setProgress(100);
                                    myRef.child("user").child(id).child("uploaded").setValue(counterupload+1);
                                }
                                buttonUpload.setClickable(false);

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

                                //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                int progress_int = (int)(((double)(counterupload)/jumlahfile)*100);

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
                //buttonUpload.setProgress(50);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "faill", Toast.LENGTH_LONG).show();
                buttonUpload.setProgress(-1);
                buttonUpload.setClickable(true);
            }

    }

    //----------------- prosedur dan fungsi tab 1-----------------//

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
            if (istab1)
            {
                addBottomDots(position);
            }
            else
            {
                addBottomDots2(position);
            }




            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length) {
                // last page. make button text to GOT IT
               // btnNext.setText("start");
               // btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
               // btnNext.setText("next");
               // btnSkip.setVisibility(View.VISIBLE);
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

        if (dots2.length > 0)
            dots2[currentPage].setTextColor(Color.parseColor("#232b2b"));
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
                case 5:
                    if (layouts[5] == R.layout.slide5)
                    {
                        verifikasi1 = (Button) findViewById(R.id.verifikasi1);
                        if (user.persiapan)
                        {
                            verifikasi1.setClickable(false);
                            verifikasi1.setBackgroundColor(Color.parseColor("#8BC34A"));
                        }
                        else
                        {
                            verifikasi1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    verifikasi1.setClickable(false);
                                    verifikasi1.setBackgroundColor(Color.parseColor("#8BC34A"));
                                    istutor1_done = true;
                                    user.persiapan =true;
                                    myRef.child("user").child(id).child("persiapan").setValue(true);
                                    host.getTabWidget().getChildTabViewAt(1).setEnabled(true);
                                    host.setCurrentTab(1);
                                }
                            });
                        }
                    }

                    if (layouts[5] == R.layout.silde12)
                    {

                        verifikasi2 = (Button) findViewById(R.id.verifikasi2);
                        if (user.petunjuk_penggunaan)
                        {
                            verifikasi2.setClickable(false);
                            verifikasi2.setBackgroundColor(Color.parseColor("#8BC34A"));
                        }
                        else
                        {

                            verifikasi2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    verifikasi2.setClickable(false);
                                    verifikasi2.setBackgroundColor(Color.parseColor("#8BC34A"));
                                    istutor2_done = true;
                                    user.petunjuk_penggunaan =true;
                                    myRef.child("user").child(id).child("petunjuk_penggunaan").setValue(true);
                                    host.getTabWidget().getChildTabViewAt(2).setEnabled(true);
                                    host.setCurrentTab(2);
                                }
                            });
                        }


                    }

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

    public static class username {
        public boolean login;
        public boolean persiapan = false;
        public boolean petunjuk_penggunaan = false;
        public boolean interview = false;
        public Integer uploaded = 0;
        public String pertanyaan;
        public String durasi;
        public String nama;
        public String TTL;

        public username(){}
    }
    //----------------- prosedur dan fungsi tab 1-----------------//

    public void setdata_tab3()
    {

        //nama
        nama.setText(user.nama);

        //ttl
        ttl.setText(user.TTL);

        //jumah pertanyaan
        separated = user.pertanyaan.split(",");
        jumlah_pertanyaan.setText(Integer.toString(separated.length));

        //jumlah durasi
        Integer total_durasi = 0;
        //final ArrayList<Integer> durasipertanyaan = new ArrayList<Integer>();
        durasi = user.durasi.split(",");

        for(int i = 0; i < durasi.length; i++)
        {
            durasipertanyaan.add(i, Integer.parseInt(durasi[i]));
            total_durasi += Integer.parseInt(durasi[i]) + 7; //7 detik waktu siap siap
        }

        if (total_durasi<60)
        {
            total_waktu.setText("1");
        }
        else
        {
            total_waktu.setText(Integer.toString(total_durasi/60));
        }

        //jumlah data
        jumlah_data.setText(Integer.toString(total_durasi*4/60));
        jumlah_data_tab4.setText(Integer.toString(total_durasi*4/60));
    }

    public void setdata_tab4()
    {
        //nama
        nama_tab4.setText(user.nama);

        //ttl
        ttl_tab4.setText(user.TTL);

        //jumlah video
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        File[] direktori = file.listFiles();
        int video = direktori.length;
        jumlah_video.setText(Integer.toString(video));

        //jumlah uploaded
        jumlah_uploaded.setText(Integer.toString(user.uploaded));

        if( direktori.length == user.uploaded)
        {
            buttonUpload.setClickable(false);
            buttonUpload.setProgress(100);
        }
        else
        {
            buttonUpload.setClickable(true);
            buttonUpload.setProgress(0);
        }

    }
}
