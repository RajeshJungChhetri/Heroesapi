package com.heroesapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import API.HeroesAPI;
import Model.Heroes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import url.url;

public class GET extends AppCompatActivity {

    private TextView txtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        txtView = findViewById(R.id.txtView);

        Retrofit retrofit =new  Retrofit.Builder()
                .baseUrl(url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesAPI heroesAPI = retrofit.create(HeroesAPI.class);

        final Call<List<Heroes>> listCall = heroesAPI.getHeroes();

        listCall.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
                if (!response.isSuccessful()) {
                    txtView.setText("Code : "+ response.code());
                    return;
                }

                List<Heroes> heroesList = response.body();
                for (Heroes heroes: heroesList) {
                    String content = "" ;

                    content += "ID : " + heroes.getId() + "\n";
                    content += "Hero's name : " + heroes.getName() + "\n";
                    content += "Hero's Desc : " + heroes.getDesc() + "\n";

                    txtView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {
                txtView.setText("Error " + t.getMessage());

            }
        });

    }
}
