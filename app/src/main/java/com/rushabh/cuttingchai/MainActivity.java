package com.rushabh.cuttingchai;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ImageFetcher.ImageFetcherListener {

    @BindView(R.id.editText)
    EditText etSearchQuery;

    ImageFetcher fetcher;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetcher=new ImageFetcher(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_search)
    void search(){
        String searchQuery=etSearchQuery.getText().toString();
        if(searchQuery.trim().length()==0){
            etSearchQuery.setError("Please enter search query");
        }
        else{
            performSearch(searchQuery);
        }
    }

    void performSearch(String query){
        dialog=ProgressDialog.show(this,"","Fetching...");
        fetcher.fetchImage(query,this);
    }

    @Override
    public void imageFetched(ArrayList<String> imageList) {

        dialog.dismiss();

        if(imageList.size()==0){
            Toast.makeText(this,"No Images",Toast.LENGTH_LONG).show();
        }
        else{
            startActivity(ImageGridActivity.getIntentForActivity(imageList,
                    etSearchQuery.getText().toString(),this));
        }
    }

    @Override
    public void failed() {

        dialog.dismiss();
        Toast.makeText(this,"Network Error",Toast.LENGTH_LONG).show();
    }
}
