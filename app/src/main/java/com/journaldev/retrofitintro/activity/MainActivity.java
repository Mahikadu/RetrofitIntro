package com.journaldev.retrofitintro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.journaldev.retrofitintro.MyRecyclerItemClickListener;
import com.journaldev.retrofitintro.R;
import com.journaldev.retrofitintro.adapter.MoviesAdapter;
import com.journaldev.retrofitintro.model.Movie;
import com.journaldev.retrofitintro.model.MoviesResponse;
import com.journaldev.retrofitintro.rest.APIClient;
import com.journaldev.retrofitintro.rest.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;

    TextView popular,toprate,upcoming,nowplay;
    LinearLayout moviebtnlayout;


    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "5322d1109bc9489f0d5961c99f4faf34";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        popular = (TextView)findViewById(R.id.popular);
        toprate = (TextView) findViewById(R.id.toprate);
        upcoming = (TextView)findViewById(R.id.upcoming);
        nowplay = (TextView)findViewById(R.id.nowplay);

        moviebtnlayout = (LinearLayout)findViewById(R.id.moviebtnlayout);

        Button moviesbtn = (Button) findViewById(R.id.movies);
        Button tvshowsbtn = (Button) findViewById(R.id.tvshows);


        moviesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviebtnlayout.setVisibility(View.VISIBLE);
            }
        });

        toprate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIInterface apiService =
                        APIClient.getClient().create(APIInterface.class);

                Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        int statusCode = response.code();
                        List<Movie> movies = response.body().getResults();
                        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e(TAG, t.toString());
                    }
                });
            }
        });

        recyclerView.addOnItemTouchListener(
                new MyRecyclerItemClickListener(this, new MyRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                   //     showCustomDialog(view,position);
                    }
                })
        );




    }
}
