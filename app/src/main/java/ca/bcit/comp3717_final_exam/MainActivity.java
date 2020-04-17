package ca.bcit.comp3717_final_exam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText title, description, url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        url = findViewById(R.id.url);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMovie(title.getText().toString(),
                        description.getText().toString(),
                        url.getText().toString());
            }
        });

        readSingleMovieCustomObject();
    }

    private void readSingleMovieCustomObject() {
        CollectionReference movies = db.collection("MovieItem");
        movies.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    MovieItem readMovie = doc.toObject(MovieItem.class);
                }
            }
        });
    }


    private void addNewMovie(String title, String description, String url) {
        Map<String, Object> newMovie = new HashMap<>();
        newMovie.put("title", title);
        newMovie.put("description", description);
        newMovie.put("url", url);

        db.collection("MovieItem").document(title).set(newMovie);
    }
}
