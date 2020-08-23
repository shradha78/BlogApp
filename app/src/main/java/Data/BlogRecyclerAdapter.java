package Data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.view.QuerySpec;
import com.squareup.picasso.Picasso;
import com.theblog.vlogapp.R;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import Activities.BlogDetails;
import Model.Blog;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> implements Serializable {
    public Context context;
    public List<Blog> blogList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater layoutInflater;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String keyval = "";


    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("In adapter","***********");
        Blog blog = blogList.get(position);
        String imageUrl = null;
        holder.title.setText(blog.getTitle());
        holder.location.setText(blog.getLocation());


        DateFormat df = new DateFormat();
        String date = (df.format("dd-MM-yyyy hh:mm:ss", new Date(Long.valueOf(blog.getTimestamp()))).toString());

        holder.timestamp.setText(date);
        imageUrl = blog.getImage();
        Log.d("Image url",imageUrl);
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_launcher_background).fit().into(holder.image);
        Log.d("Image","&&&&&&&&");

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,location,timestamp;
        public ImageView image;
        public Button deleteButton;
        String userid;
        DatabaseReference mref;
        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;
            title = (TextView) itemView.findViewById(R.id.postTitleListId);
            location =  (TextView) itemView.findViewById(R.id.postLocationId);
            timestamp =  (TextView) itemView.findViewById(R.id.timeStampListId);
            image = (ImageView) itemView.findViewById(R.id.postImageListId);
//            deleteButton = (Button) itemView.findViewById(R.id.deleteButtonId);
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Blog blog = blogList.get(position);
////                    deleteItem(blog.getUserid(),position);
//                }
//            });

            userid = null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Blog blog = blogList.get(getAdapterPosition());
                    Intent intent = new Intent(context, BlogDetails.class);

                    intent.putExtra("title",blog.getTitle());
                    intent.putExtra("desc",blog.getDesc());
                    intent.putExtra("location",blog.getLocation());
                    intent.putExtra("image",blog.getImage());

                    Log.d("Going to:" , "Details");
                    ctx.startActivity(intent);
                    ((Activity)context).finish();

                }
            });

        }

//        private void deleteItem(final String userid,final int pos) {
//            alertDialogBuilder = new AlertDialog.Builder(context);
//            layoutInflater =LayoutInflater.from(context);
//            mAuth = FirebaseAuth.getInstance();
//            mUser =mAuth.getCurrentUser();
//            View view = layoutInflater.inflate(R.layout.confirmation_dialog , null);
//            Button noButton = (Button) view.findViewById(R.id.noButtonID);
//            Button yesButton =(Button) view.findViewById(R.id.yesButtonID);
//            alertDialogBuilder.setView(view);
//            dialog = alertDialogBuilder.create();
//            dialog.show();
//            noButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            yesButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("MUsers").child(mUser.getUid()).child("mBlog");
//                    keyval = ref.push().getKey();
//                    ref.child(keyval);
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        snapshot.getRef().removeValue();
//
//
//                            blogList.remove(getAdapterPosition());
//                            notifyItemRemoved(getAdapterPosition());
//                            dialog.dismiss();
//
//
//
//                        }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//
//                });
//
//                }
//            });
//        }
    }
}
