package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import it.jaschke.alexandria.CameraPreview.CameraPreview;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class ScannerActivity extends Activity {
    private ZBarScannerView mScannerView;
    private Handler autoFocusHandler;
    private Camera mCamera;
    ImageScanner imageScanner;
    private boolean mPreviewingPhoto = true;
    private boolean barcodeScanned = false;
    CameraPreview mPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        autoFocusHandler = new Handler();
        mCamera = retrieveCameraInstance();
        imageScanner = new ImageScanner();

        imageScanner.setConfig(0, Config.X_DENSITY, 400);
        imageScanner.setConfig(0, Config.Y_DENSITY, 400);
        imageScanner.setConfig(0, Config.ENABLE, 0);
        imageScanner.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        imageScanner.setConfig(Symbol.ISBN13, Config.ENABLE, 1);


        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCb);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    AutoFocusCallback autoFocusCb = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            autoFocusHandler.postDelayed(autoFocus, 1000);
        }
    };

    private Runnable autoFocus = new Runnable() {
        public void run() {
            if (mPreviewingPhoto) {
                mCamera.autoFocus(autoFocusCb);

            }
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            Log.d("PPPPP", "SCANNED");
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(bytes);
            int result = imageScanner.scanImage(barcode);

            Log.d("WWWW", Integer.toString(result));
            if (result != 0) {
                mPreviewingPhoto = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                SymbolSet symbols = imageScanner.getResults();

                for (Symbol sym : symbols) {
                    barcodeScanned = true;
                    Log.d("PPPPP", symbols.toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("BARCODE", sym.getData());
                    setResult(1, returnIntent);
                    releaseCamera();
                    finish();
                }

            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();

    }


    public static Camera retrieveCameraInstance() {

        Camera camera = null;
        try {

            camera = Camera.open();
        } catch (Exception e) {
            Log.e("Error:", "Camera couldn't be retrieved");
        }
        return camera;

    }


    private void releaseCamera() {
        if (mCamera != null) {
            mPreviewingPhoto = false;
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onBackPressed() {
        releaseCamera();
        Intent intent = new Intent();
        intent.putExtra("BARCODE", "NULL");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public void cancelCameraRequest() {
        Intent intent = new Intent();
        intent.putExtra(ScannerConstants.ERROR_INFO, "Camera Unavailable");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    public boolean isCameraAvailable() {
        PackageManager packageManager = getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);

    }

}
