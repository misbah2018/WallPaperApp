package com.example.videostraming;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private Button setWallpaperButton;
    private List<String> imageNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        setWallpaperButton = findViewById(R.id.setWallpaperButton);

        // Replace with your list of image names
        imageNameList = new ArrayList<>();
        //imageNameList.add("wallpaper_1");
        imageNameList.add("wallpaper_2");
        imageNameList.add("wallpaper_3");
        // Add more image names as needed

        ImageAdapter imageAdapter = new ImageAdapter();
        gridView.setAdapter(imageAdapter);

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = imageAdapter.getSelectedPosition();
                if (selectedPosition != -1) {
                    String imageName = imageNameList.get(selectedPosition);
                    new SetWallpaperTask().execute(imageName);
                } else {
                    Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {

        private int selectedPosition = -1;

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public int getCount() {
            return imageNameList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250)); // Set desired size for grid items
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            String imageName = imageNameList.get(position);
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            imageView.setImageResource(resourceId);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });

            // Highlight selected image
            if (selectedPosition == position) {
                imageView.setBackgroundResource(R.drawable.initial_wallpaper);
            } else {
                imageView.setBackgroundResource(0);
            }

            return imageView;
        }
    }

    private class SetWallpaperTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... imageNames) {
            String imageName = imageNames[0];
            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to set wallpaper.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
