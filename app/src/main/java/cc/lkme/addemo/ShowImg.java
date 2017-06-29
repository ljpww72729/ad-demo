package cc.lkme.addemo;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cc.lkme.addemo.databinding.ActivityShowImgBinding;

public class ShowImg extends AppCompatActivity {

    ActivityShowImgBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_img);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_img);
        new ImgAsyncTask().execute("http://img.lkme.cc/2/1X1");
    }

    class ImgAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String[] params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://img.lkme.cc/2/1:1");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            if (o != null){
                binding.showImg.setImageBitmap(o);
            }
        }
    }

}
