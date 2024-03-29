package cc.lkme.addemo;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ww.lp.rvrl_lib.LPRecyclerViewAdapter;
import com.ww.lp.rvrl_lib.LPRefreshLoadListener;
import com.ww.lp.rvrl_lib.ScrollChildSwipeRefreshLayout;
import com.ww.lp.rvrl_lib.SingleItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import cc.lkme.addemo.databinding.ActivityUriSchemeListBinding;

public class UriSchemeListActivity extends AppCompatActivity {

    private ActivityUriSchemeListBinding binding;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<AppInfo> mRVData;
    private LPRecyclerViewAdapter<AppInfo> lpRecyclerViewAdapter;
    private Button copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_uri_scheme_list);
        binding.lpRv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lpRv.setLayoutManager(mLayoutManager);
        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = binding.lpScsr;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(binding.lpRv);
        // specify an adapter (see also next example)
        mRVData = new ArrayList<>();
//        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, BR.lp_rv_item);
        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, cc.lkme.addemo.BR.lp_rv_item, swipeRefreshLayout, null);
//        lpRecyclerViewAdapter.setPageStartNum(0);
//        lpRecyclerViewAdapter.setOnLoadMoreListener(new LPRefreshLoadListener.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                lpRecyclerViewAdapter.showLoadingMore(true);
//                loadData(lpRecyclerViewAdapter.getPageCurrentNum() + 1);
//            }
//        });
        lpRecyclerViewAdapter.setOnRefreshListener(new LPRefreshLoadListener.onRefreshListener() {
            @Override
            public void onRefresh() {
                //数据刷新操作
                loadData();
            }
        });
        binding.lpRv.setAdapter(lpRecyclerViewAdapter);
        loadData();
        binding.lpRv.addOnItemTouchListener(new SingleItemClickListener(binding.lpRv, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppInfo appInfo = mRVData.get(position);
                if (appInfo.isInstalled()) {
                    if (appInfo.getUriScheme().contains(" ")) {
                        Toast.makeText(UriSchemeListActivity.this, "链接中含有空格！", Toast.LENGTH_SHORT).show();
                    } else {
                        //安装则唤起app
                        APPUtils.openAppWithUriScheme(UriSchemeListActivity.this, appInfo.getUriScheme(), null, binding.cbSingle.isChecked());
                    }
                } else {
                    //未安装则唤起应用宝安装
                    Uri uri = Uri.parse("market://details?id=" + appInfo.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.setPackage("com.tencent.android.qqdownloader");
                    goToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
//                Toast.makeText(UriSchemeListActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UriSchemeListActivity.this);
                builder.setTitle("Uri Scheme").setMessage(mRVData.get(position).getUriScheme()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("uri scheme", mRVData.get(position).getUriScheme()));
                        Toast.makeText(UriSchemeListActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        }));
        binding.paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cbm != null && cbm.hasPrimaryClip()) {
                    if (cbm.getPrimaryClip().getItemCount() > 0) {
                        binding.uriScheme.setText("");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            cbm.getPrimaryClipDescription().getTimestamp();
                        }
                        binding.uriScheme.setText(cbm.getPrimaryClip().getItemAt(0).getText());
                    } else {
                        Toast.makeText(UriSchemeListActivity.this, "剪切板中无内容！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.uriScheme.setText("");
                loadData();
            }
        });
        binding.uriScheme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadData();
            }

        });

        if (getIntent() != null) {
            String data = getIntent().getDataString();
            Log.i("data====", "onCreate: " + data);
            if (data != null && data.startsWith("openapp.jdmobile")) {
                String lastInfo;
                if (data.contains("{")) {
                    String params = data.substring(data.indexOf("{"));
                    String paramsEncode = URLEncoder.encode(params);
                    lastInfo = "openapp.jdmobile://virtual?params=" + paramsEncode;
                } else {
                    lastInfo = data;
                }

                binding.uriScheme.setText(lastInfo);
            }

//            Toast.makeText(UriSchemeListActivity.this, "data is " + data, Toast.LENGTH_LONG).show();
        }
        binding.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cbm.setPrimaryClip(ClipData.newPlainText("uri scheme", binding.uriScheme.getText().toString()));
                Toast.makeText(UriSchemeListActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void requestPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void loadData() {
        ArrayList<AppInfo> data = new ArrayList<>();
        String uriScheme = binding.uriScheme.getText().toString();
        String[] uri_scheme_arr;
        if (!TextUtils.isEmpty(uriScheme)) {
            try {
                JSONObject jsonObject = new JSONObject(uriScheme);
                uri_scheme_arr = new String[1];
                uri_scheme_arr[0] = jsonObject.optString("android_scheme");
            } catch (JSONException e) {
                uri_scheme_arr = TextUtils.split(binding.uriScheme.getText().toString(), ";");
                e.printStackTrace();
            }
        } else {
            uri_scheme_arr = new String[0];
        }
        for (String uri_scheme : uri_scheme_arr) {
            AppInfo appInfo = APPUtils.getAppInfoByScheme(this, uri_scheme);
            if (appInfo == null) {
                appInfo = new AppInfo();
                appInfo.setUriScheme(uri_scheme);
                appInfo.setInstalled(false);
                appInfo.setAppName("未知");
                appInfo.setAppIconDrawable(ContextCompat.getDrawable(UriSchemeListActivity.this, R.drawable.no_app));
            }
            data.add(appInfo);
        }
        if (uri_scheme_arr.length - data.size() == 0) {
            binding.result.setTextColor(ContextCompat.getColor(UriSchemeListActivity.this, R.color.colorPrimary));
        } else {
            binding.result.setTextColor(ContextCompat.getColor(UriSchemeListActivity.this, R.color.colorAccent));
        }
        binding.result.setText(getString(R.string.uri_tips, uri_scheme_arr.length, data.size(), uri_scheme_arr.length - data.size()));
        lpRecyclerViewAdapter.loadDataSuccess(data, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                //add the function to perform here
                Intent intent = new Intent(UriSchemeListActivity.this, ScanActivity.class);
                startActivityForResult(intent, 1000);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                String result = data.getStringExtra("result");
                binding.uriScheme.setText(result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
