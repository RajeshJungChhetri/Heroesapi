package com.heroesapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import API.HeroesAPI;
import Model.Heroes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;
import url.url;

public class MainActivity extends AppCompatActivity {

    private EditText edHeroesName, edHeroesDesc;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edHeroesName = findViewById(R.id.edHeroesName);
        edHeroesDesc = findViewById(R.id.edHeroesDesc);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }

            private void Save() {
                String name = edHeroesName.getText().toString();
                String desc = edHeroesDesc.getText().toString();



                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                HeroesAPI heroesAPI = retrofit.create(HeroesAPI.class);

                Call<Void> heroesCall = heroesAPI.addHero (name,desc);

                heroesCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });




            }
        });

    }
}
