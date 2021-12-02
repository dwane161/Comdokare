package com.example.comdokare.ui.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.comdokare.MainActivity;
import com.example.comdokare.Product;
import com.example.comdokare.R;
import com.example.comdokare.ScannedProductActivity;
import com.example.comdokare.databinding.FragmentGalleryBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private String barcodeData;
    Boolean checkingBarCode = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = binding.surfaceView;
        initialiseDetectorsAndSources();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getActivity().getBaseContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && !checkingBarCode) {
                    checkingBarCode = true;
                    if (barcodes.valueAt(0).email != null) {
                        barcodeData = barcodes.valueAt(0).email.address;
                    } else {

                        barcodeData = barcodes.valueAt(0).displayValue;
                    }
                    Product product = getProduct(barcodeData);

                    if (product != null) {
                        Intent myIntent = new Intent(getActivity().getBaseContext(), ScannedProductActivity.class);
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                        myIntent.putExtra("scanned-product-key", product);
                        checkingBarCode = false;
                        getActivity().startActivity(myIntent);
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    private Product getProduct(String id) {
        String data = getAssetJsonData(getActivity().getBaseContext());
        Type type = new TypeToken<List<Product>>() {
        }.getType();
        List<Product> modelObject = new Gson().fromJson(data, type);
        if (modelObject != null) {
            return modelObject.stream().filter(productFind -> id.equals(productFind.barcodeId)).findAny().orElse(null);
        }
        return null;
    }

    @Nullable
    public static String getAssetJsonData(@NonNull Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data-products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }
}