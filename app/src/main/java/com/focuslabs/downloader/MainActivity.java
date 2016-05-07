package com.focuslabs.downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText mUrlField;
    private EditText mPortField;
    private EditText mFilenameField;

    private Button mDownloadButton;


    public static final int progress_bar_type = 0;

    public String fileToBeSaved = "file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDownloadButton = (Button) findViewById(R.id.buttonDownload);

        mUrlField = (EditText) findViewById(R.id.edittexturl);
        mPortField = (EditText) findViewById(R.id.edittextport);
        mFilenameField = (EditText) findViewById(R.id.edittextfilename);

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Trying to download file.", Toast.LENGTH_LONG).show();

                String url = mUrlField.getText().toString()+ mPortField.getText().toString() + "/" + mFilenameField.getText().toString();
                fileToBeSaved = mFilenameField.getText().toString();
                Log.i("URL: ", url);
                new DownloadFile().execute(url);
            }
        });


    }



    private class DownloadFile extends AsyncTask<String, String, String>{

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Download started.", Toast.LENGTH_LONG).show();

        }
        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // Get Music file length
                int lenghtOfFile = connection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, fileToBeSaved);
                fileToBeSaved = "downloaded-file-" + String.valueOf(new Random(Long.MAX_VALUE).nextLong());
                OutputStream output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e){
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            Toast.makeText(getApplicationContext(), "Download complete.", Toast.LENGTH_LONG).show();

        }
    }
}
