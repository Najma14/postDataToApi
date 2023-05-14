package com.example.postdatatoapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt, jobEdt;
    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdt=findViewById(R.id.idEdtName);
        jobEdt=findViewById(R.id.idEdtJob);
        postDataBtn=findViewById(R.id.idBtnPost);
        responseTV=findViewById(R.id.idTVResponse);
        loadingPB=findViewById(R.id.idLoadingPB);

        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEdt.getText().toString().isEmpty()&&jobEdt.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                postData(nameEdt.getText().toString(),jobEdt.getText().toString());


            }
        });
    }
    private void postData(String name,String job)
    {
        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        modal modal=new modal(name,job);

        // calling a method to create a post and passing our modal class.
        Call<modal> call = retrofitAPI.createPost(modal);
        call.enqueue(new Callback<modal>() {

            @Override
            public void onResponse(Call<com.example.postdatatoapi.modal> call, Response<com.example.postdatatoapi.modal> response) {
                // this method is called when we get response from our api.
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.
                jobEdt.setText("");
                nameEdt.setText("");

                // we are getting response from our body
                // and passing it to our modal class.
                modal responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getName() + "\n" + "Job : " + responseFromAPI.getJob();

                // below line we are setting our
                // string to our text view.
                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<com.example.postdatatoapi.modal> call, Throwable t) {


                // setting text to our text view when
                // we get error response from API.
                responseTV.setText("Error found is : " + t.getMessage());
            }

            });
        }
}