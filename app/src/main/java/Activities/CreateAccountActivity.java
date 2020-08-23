package Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.theblog.vlogapp.R;

public class CreateAccountActivity extends AppCompatActivity {
private EditText firstName, lastName, email, password;
private Button createAccntButton;
private DatabaseReference mDatabaseReference;
private ImageButton profilePic;
public final static int GalleryCode = 1;
private FirebaseDatabase mDatabase;
private Uri resultUri = null;
private FirebaseAuth mAuth;
private ProgressBar mprogress;
private StorageReference mFirebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        mprogress = new ProgressBar(this);


        firstName = (EditText) findViewById(R.id.firstNameCAID);
        lastName = (EditText) findViewById(R.id.LastNameCAID);
        email = (EditText) findViewById(R.id.emailCAID);
        password = (EditText) findViewById(R.id.passwordCAID);
        profilePic = (ImageButton) findViewById(R.id.profileId);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,GalleryCode);
            }
        });
        createAccntButton = (Button) findViewById(R.id.CreateAccntButtonID);
        createAccntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccnt();
            }
        });



    }

    private void createNewAccnt() {
        final String fn = firstName.getText().toString().trim();
        final String ln = lastName.getText().toString().trim();
        String e  = email.getText().toString().trim();
        String p = password.getText().toString().trim();
        if(!fn.equals("") && !ln.equals("") && !e.equals("") && !p.equals("")){
//            mprogress.setContentDescription("Creating Account");
//            mprogress.showContextMenu();

            mAuth.createUserWithEmailAndPassword(e,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null) {
                        StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics").child(resultUri.getLastPathSegment());
                        imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserref = mDatabaseReference.child(userId);
                                currentUserref.child("First Name").setValue(fn);
                                currentUserref.child("Last Name").setValue(ln);
                                currentUserref.child("Profile Pic").setValue(resultUri.toString());


                                Intent intent = new Intent(CreateAccountActivity.this,PostListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });




                    }
                }
            });

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryCode && resultCode == RESULT_OK){
            Uri mimageUri = data.getData();
            CropImage.activity(mimageUri).setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profilePic.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}