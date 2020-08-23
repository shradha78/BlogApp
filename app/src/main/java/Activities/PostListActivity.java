package Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theblog.vlogapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Data.BlogRecyclerAdapter;
import Model.Blog;

public class PostListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public RecyclerView recyclerView;
    public BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;
    String userId ="";
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        mAuth = FirebaseAuth.getInstance();
        mUser =mAuth.getCurrentUser();
        userId = mAuth.getUid();
        mdatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mdatabase.getReference().child("MUsers").child(userId).child("mBlog");
        mDatabaseReference.keepSynced(true);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewID);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(blogRecyclerAdapter);
        blogList = new ArrayList<>();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add :
                if(mUser != null && mAuth != null){
                startActivity(new Intent(PostListActivity.this,AddPostActivity.class));
                finish();
                }
                break;
            case R.id.action_signOut:
                if(mUser != null && mAuth != null){
                    mAuth.signOut();
                    startActivity(new Intent(PostListActivity.this,MainActivity.class));
                    finish();
                }

                break;
            default:
                Log.d("NO ACTION MENU","$$$$$$$$$$$$$$$");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Blog blog = snapshot.getValue(Blog.class); // mapped to blog class
                blogList.add(blog);
                Collections.reverse(blogList);

                blogRecyclerAdapter = new BlogRecyclerAdapter(PostListActivity.this,blogList);
//                Log.d("Going","Adapter");
                recyclerView.setAdapter(blogRecyclerAdapter);
                blogRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {



            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}