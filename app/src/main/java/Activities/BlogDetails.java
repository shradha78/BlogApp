package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theblog.vlogapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import Data.BlogRecyclerAdapter;
import Model.Blog;

public class BlogDetails extends AppCompatActivity implements Serializable {
    private TextView title, desc, location;
    private ImageView image;
    private Blog blog;
    private Button backButton;
    private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        Log.d("Here in", "details");
        Bundle m = getIntent().getExtras();
        setUpUI();
       if(m != null){
           title.setText(m.getString("title"));
           desc.setText(m.getString("desc"));
           location.setText(m.getString("location"));
           Picasso.get().load(m.getString("image")).into(image);

       }
       backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BlogDetails.this, PostListActivity.class));
                finish();
            }
        });
    }

    public void setUpUI() {
        title = (TextView) findViewById(R.id.blogTitleID);
        desc = (TextView)findViewById(R.id.blogDescID);
        location = (TextView) findViewById(R.id.blogLocationID);
        image = (ImageView)findViewById(R.id.blogImageID);
        backButton = (Button) findViewById(R.id.backButttonID);

    }
}