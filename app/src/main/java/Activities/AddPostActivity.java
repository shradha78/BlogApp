package Activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.theblog.vlogapp.R;

import java.util.HashMap;
import java.util.Map;

import Model.Blog;

public class AddPostActivity extends AppCompatActivity {
   private ImageButton mPostImage;
   private EditText mPostTitle, mPostText,mPostLocation;
   private Button mSubmitButton;
   private ProgressBar mProgressBar;
    private DatabaseReference mPostDatabase;
static private final int Gallery_code = 1;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Uri imageUri; // for uri of image
    private StorageReference mStorage;
    private Uri resultUri = null;

    public AddPostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mAuth = FirebaseAuth.getInstance();
        mUser =mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressBar = new ProgressBar(this);
       mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MUsers").child(mAuth.getUid()).child("mBlog");
       mPostImage = (ImageButton) findViewById(R.id.addImageId);
       mPostTitle = (EditText) findViewById(R.id.addTitleId);
       mPostText = (EditText) findViewById(R.id.addTextId);
       mPostLocation = (EditText) findViewById(R.id.addLocationId);
       mSubmitButton = (Button) findViewById(R.id.submitpostButtonId);
       mPostImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //get gallery intent
               Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
               galleryIntent.setType("image/*");
              startActivityForResult(galleryIntent,Gallery_code);


           }
       });
       Log.d("HEREEE","((((((((((((");
  mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Log.d("HEREEE","(((((((((((()))))))))");
          startposting();
      }
  });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_code && resultCode == RESULT_OK){
          imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
//          mPostImage.setImageURI(imageUri);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                mPostImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startposting() {
        Log.d("HEREEE","(((((((((((())))))))))))))))))))))))))))***********");
        mProgressBar.setContentDescription("Posting to blog...");
        mProgressBar.isShown();
        final String titleVal = String.valueOf(mPostTitle.getText());
        final String descVal = String.valueOf(mPostText.getText());
        final String locationVal = String.valueOf(mPostLocation.getText());

        if(!titleVal.equals("") && !descVal.equals("") && imageUri != null && !locationVal.equals("")){
            //start posting data to database.

            StorageReference filepath = mStorage.child("MUsers").child(mUser.getUid()).child("MBlog_Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                    while(!downloadUrl.isSuccessful()) {
                        if(downloadUrl.isSuccessful()) {
                            break;
                        }
                    }
                    Log.e("Download URL: ", downloadUrl.getResult().toString());

                    DatabaseReference newPost = mPostDatabase.push();
                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("title", titleVal);
                    dataToSave.put("desc", descVal);
                    dataToSave.put("location",locationVal);
                    dataToSave.put("image", downloadUrl.getResult().toString());
                    dataToSave.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    dataToSave.put("userid", mUser.getUid());
                    newPost.setValue(dataToSave);
                    mProgressBar.cancelDragAndDrop();
                    startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                    finish();
                }
            });

        }
        

    }


}