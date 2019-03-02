package lab.ourteam.newlab.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import lab.ourteam.newlab.R;

public class qrcode_activity extends AppCompatActivity implements  Iactivity {
    private CaptureManager capture;
    private DecoratedBarcodeView bv_barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_activity);
        initID();
        capture = new CaptureManager(this, bv_barcode);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
       // bv_barcode.decodeContinuous(barcodeCallback);

    }
   /* private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            bv_barcode.pause();
          if (result !=null){
                Log.e(getClass().getName(), "获取到的扫描结果是：" + result.getText());
                //可以对result进行一些判断，比如说扫描结果不符合就再进行一次扫描
                if (result.getText().contains("符合我的结果")){
                    //符合的可以不在扫描了，当然你想继续扫描也是可以的
                } else {
                    bv_barcode.resume();
                }
            }
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };*/
    @Override
    public void initID() {
        bv_barcode = (DecoratedBarcodeView) findViewById(R.id.bv_barcode);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return bv_barcode.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result !=null) {
            if(result.getContents() ==null ) {
                Log.d(getClass().getName(), "Cancelled");
                Toast.makeText(this, "扫描结果为空", Toast.LENGTH_LONG).show();
            } else {
                Log.d(getClass().getName(), "Scanned: " + result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"扫描结果为空", Toast.LENGTH_LONG).show();
        }
    }
}
