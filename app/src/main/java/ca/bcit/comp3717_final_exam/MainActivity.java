package ca.bcit.comp3717_final_exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText title, description, url;
    RecyclerView mFirestoreList;

    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        url = findViewById(R.id.url);
        Button button = findViewById(R.id.button);
        mFirestoreList = findViewById(R.id.movies);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMovie(title.getText().toString(),
                        description.getText().toString(),
                        url.getText().toString());
            }
        });

        Query query = db.collection("MovieItem");

        FirestoreRecyclerOptions<MovieItem> options = new FirestoreRecyclerOptions.Builder<MovieItem>()
                .setQuery(query, MovieItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MovieItem, MoviesViewHolder>(options) {
            @NonNull
            @Override
            public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new MoviesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MoviesViewHolder holder, int position, @NonNull MovieItem model) {
                holder.list_title.setText(model.getTitle());
                holder.list_description.setText(model.getDescription());
                holder.list_url.setText(model.getUrl());
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }


    private void addNewMovie(String title, String description, String url) {
        Map<String, Object> newMovie = new HashMap<>();
        newMovie.put("title", title);
        newMovie.put("description", description);
        newMovie.put("url", url);

        db.collection("MovieItem").document(title).set(newMovie);
    }

    private class MoviesViewHolder extends RecyclerView.ViewHolder{

        private TextView list_title;
        private TextView list_description;
        private TextView list_url;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_title = itemView.findViewById(R.id.list_title);
            list_description = itemView.findViewById(R.id.list_description);
            list_url = itemView.findViewById(R.id.list_url);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
