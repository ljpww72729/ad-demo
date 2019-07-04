package cc.lkme.addemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;

public class StorageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage);
        TextView info = findViewById(R.id.info);
        String infoStr = "Environment.getDataDirectory()==" + Environment.getDataDirectory().getAbsolutePath() + "\n";
        File[] dataFiles = Environment.getDataDirectory().listFiles();
        if (dataFiles != null) {
            for (int i = 0; i < dataFiles.length; i++) {
                infoStr += dataFiles[i] + "\n";
            }
        }

        infoStr += "Environment.getRootDirectory()==" + Environment.getRootDirectory().getAbsolutePath() + "\n";
        File[] rootFiles = Environment.getRootDirectory().listFiles();
        if (rootFiles != null) {
            for (int i = 0; i < rootFiles.length; i++) {
                infoStr += rootFiles[i] + "\n";
            }
        }
        infoStr += "Environment.getExternalStorageDirectory()==" + Environment.getExternalStorageDirectory().getAbsolutePath() + "\n";
        infoStr += "getFilesDir()==" + getFilesDir().getAbsolutePath() + "\n";
        infoStr += "getCacheDir()==" + getCacheDir().getAbsolutePath() + "\n";
        infoStr += "getExternalCacheDir()==" + getExternalCacheDir().getAbsolutePath() + "\n";
        info.setText(infoStr);

    }


}
