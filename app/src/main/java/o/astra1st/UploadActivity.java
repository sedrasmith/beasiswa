package o.astra1st;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class UploadActivity extends AppCompatActivity {

    //a constant to track the file chooser intent
    /*
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonUpload;
    Button buttonList;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    //firebase storage reference
    private StorageReference storageReference;

    //array file
    File[] dir;

    TextView keterangan;

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    /*
        storageReference = FirebaseStorage.getInstance().getReference();

        buttonList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadFile();
            }
        });

    */
    }

    /*
    private void uploadFile() {
        //if there is a file to upload

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)+ File.separator + "CameraSample");
        dir = file.listFiles();

        for (int u = 0; u <= dir.length; u++)
        {
            Intent data = new Intent();
            data.setData(Uri.fromFile(dir[1]));
            filePath = data.getData();

            if (filePath != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();



                StorageReference riversRef = storageReference.child("imagess/");
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
    */


}
