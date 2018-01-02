package cc.lkme.addemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ww.lp.rvrl_lib.LPRecyclerViewAdapter;
import com.ww.lp.rvrl_lib.LPRefreshLoadListener;
import com.ww.lp.rvrl_lib.ScrollChildSwipeRefreshLayout;
import com.ww.lp.rvrl_lib.SingleItemClickListener;

import java.math.BigInteger;
import java.util.ArrayList;

import cc.lkme.addemo.databinding.PChklistBinding;

/**
 * Created by LinkedME06 on 2017/12/29.
 */

public class PChklistActivity extends AppCompatActivity {

    private PChklistBinding binding;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<PChklstInfo> mRVData;
    private LPRecyclerViewAdapter<PChklstInfo> lpRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_chklist);
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
        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.p_chklst_view_item, cc.lkme.addemo.BR.lp_rv_item, swipeRefreshLayout, null);
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
                PChklstInfo appInfo = mRVData.get(position);

//                if (appInfo.isInstalled()) {
//                    //安装则唤起app
//                    APPUtils.openAppWithUriScheme(PChklistActivity.this, appInfo.getUriScheme(), null, binding.cbSingle.isChecked());
//                } else {
//                    //未安装则唤起应用宝安装
//                    Uri uri = Uri.parse("market://details?id=" + appInfo.getPackageName());
//                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                    goToMarket.setPackage("com.tencent.android.qqdownloader");
//                    goToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        startActivity(goToMarket);
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Toast.makeText(UriSchemeListActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PChklistActivity.this);
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
                        Toast.makeText(PChklistActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        }));

        binding.checkResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkgList = binding.pList.getText().toString();
                if (TextUtils.isEmpty(pkgList)) {
                    Toast.makeText(PChklistActivity.this, "包名列表为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String result = binding.pChkResult.getText().toString();
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(PChklistActivity.this, "结果值为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadData();
            }
        });

    }

    private void loadData() {
        ArrayList<PChklstInfo> data = new ArrayList<>();
        String pkgList = binding.pList.getText().toString();
        String result = binding.pChkResult.getText().toString();
        if (!TextUtils.isEmpty(pkgList) && !TextUtils.isEmpty(result)) {
            BigInteger bigInteger = new BigInteger(binding.pChkResult.getText().toString());
            String[] resultArr = bigInteger.toString(2).split("");
            for (int i = 0; i < resultArr.length; i++) {
                System.out.println("resultArr[" + i + "]==" + resultArr[i]);
            }
            String[] pkgArr = binding.pList.getText().toString().split(",");
            if (resultArr.length > 0 && pkgArr.length > 0) {
                for (int i = 0; i < pkgArr.length; i++) {
                    PChklstInfo pChklstInfo = (PChklstInfo) APPUtils.getPChklstInfoByPkgName(PChklistActivity.this, pkgArr[i]);
                    if (pChklstInfo == null) {
                        pChklstInfo = new PChklstInfo();
                        pChklstInfo.setInstalled(false);
                        pChklstInfo.setAppName("未知");
                        pChklstInfo.setAppIconDrawable(ContextCompat.getDrawable(PChklistActivity.this, R.drawable.no_app));
                    }
                    pChklstInfo.setPackageName(pkgArr[i]);
                    pChklstInfo.setCheckResult(resultArr[i + 9].equals("1"));
                    data.add(pChklstInfo);
                }
            }
        }


//        if (uri_scheme_arr.length - data.size() == 0) {
//            binding.result.setTextColor(ContextCompat.getColor(PChklistActivity.this, R.color.colorPrimary));
//        } else {
//            binding.result.setTextColor(ContextCompat.getColor(PChklistActivity.this, R.color.colorAccent));
//        }
//        binding.result.setText(getString(R.string.uri_tips, uri_scheme_arr.length, data.size(), uri_scheme_arr.length - data.size()));
        lpRecyclerViewAdapter.loadDataSuccess(data, 0);
    }
}
