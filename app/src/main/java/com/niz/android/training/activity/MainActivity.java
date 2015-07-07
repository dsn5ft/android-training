package com.niz.android.training.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.niz.android.training.R;
import com.niz.android.training.api.model.Image;
import com.niz.android.training.service.ImageService;
import com.niz.android.training.service.ImageType;
import com.niz.android.training.util.ImageLoaderUtils;
import org.apache.commons.io.IOUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private ImageService imageService;
    private ImageLoaderUtils imageLoaderUtils;

    @InjectView(R.id.title) TextView textViewTitle;
    @InjectView(R.id.imageView) ImageView imageView;

    @OnClick(R.id.imageView)
    public void onImageViewClicked() {
        loadRandomImage();
    }

    @OnClick(R.id.buttonMurray)
    public void onButtonMurrayClicked() {
        startImageListActivity(ImageType.MURRAY);
    }

    @OnClick(R.id.buttonCage)
    public void onButtonCageClicked() {
        startImageListActivity(ImageType.CAGE);
    }

    @OnClick(R.id.buttonRandom)
    public void onButtonRandomClicked() {
        startImageListActivity(ImageType.RANDOM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        imageService = new ImageService();
        imageLoaderUtils = new ImageLoaderUtils();

        loadRandomImage();
    }

    private void loadRandomImage() {
        imageService.getImage(ImageType.RANDOM, new Callback<Image>() {
            @Override
            public void success(final Image image, Response response) {
                imageLoaderUtils.loadImage(MainActivity.this, image, imageView, textViewTitle);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void startImageListActivity(ImageType imageType) {
        startActivity(ImageListActivity.getIntent(this, imageType));
    }

    private void makeNonBlockingApiCall() {
        String url = "http://live.yodle.com/api/v1/contacts/12345";

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return makeBlockingApiCall(params[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                // do something with result (update UI)
            }
        }.execute(url);
    }

    private String makeBlockingApiCall(String url) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            if (urlConnection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) IOUtils.closeQuietly(reader);
        }
        return response.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO: launch settings page
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

