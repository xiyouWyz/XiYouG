package com.example.wyz.xiyoug.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.example.wyz.xiyoug.CarmerUtil.QRCodeSupport;
import com.example.wyz.xiyoug.CarmerUtil.view.FinderViewStyle2;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;


/**
 * @author bingbing
 * @date 15/9/22
 */
public class ScanActivity extends AppCompatActivity implements QRCodeSupport.OnScanResultListener {

    private SurfaceView mSurfaceView;
    private FinderViewStyle2 mFinderView;
    private QRCodeSupport mQRCodeSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

        mFinderView = (FinderViewStyle2) findViewById(R.id.capture_viewfinder_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.sufaceview);
        mQRCodeSupport = new QRCodeSupport(mSurfaceView, mFinderView);
        mQRCodeSupport.setScanResultListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeSupport.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQRCodeSupport.onPause();
    }

    @Override
    public void onScanResult(String notNullResult, byte[] resultBytes) {
        Intent intent = new Intent();
        String url= HttpLinkHeader.BOOK_DETAIL_BARCODE+notNullResult;
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        intent.putExtras(bundle);
        intent.setClass(ScanActivity.this,BookDetailActivity.class);
        startActivity(intent);
      /*  intent.putExtra("result", notNullResult);
        intent.putExtra("resultByte",resultBytes);
        setResult(Activity.RESULT_OK, intent);*/
        finish();
    }
}
