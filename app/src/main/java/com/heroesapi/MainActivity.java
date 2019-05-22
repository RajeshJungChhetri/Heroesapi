package com.heroesapi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

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
    private ImageView imgview;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edHeroesName = findViewById(R.id.edHeroesName);
        edHeroesDesc = findViewById(R.id.edHeroesDesc);
        btnSave = findViewById(R.id.btnSave);
        imgview = findViewById(R.id.imgview);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }


        });

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

                Call<Void> heroesCall = heroesAPI.addHero(name, desc);

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
                        Toast.makeText(MainActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

    private void BrowseImage() {
        Intent intent = new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();

            }
        }
        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private void previewImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imgview.setImageBitmap(myBitmap);
        }
    }


    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
}
