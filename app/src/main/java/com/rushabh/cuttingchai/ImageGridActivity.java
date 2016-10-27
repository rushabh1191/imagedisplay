package com.rushabh.cuttingchai;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

public class ImageGridActivity extends AppCompatActivity {

    private static final String ARG_IMAGE_LIST = "arg_image_array_list";
    private static final String ARG_IMAGE_QUERY = "arg_query";
    @BindView(R.id.content_image_grid)
    ListView imageGrid;


    ArrayList<String> imageUrls;
    ImageGridAdapter imageGridAdapter;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        ButterKnife.bind(this);




        if(savedInstanceState==null) {
            savedInstanceState=getIntent().getExtras();
        }

        imageUrls = savedInstanceState.getStringArrayList(ARG_IMAGE_LIST);
        title=savedInstanceState.getString(ARG_IMAGE_QUERY);

        getSupportActionBar().setTitle("Result For \""+title+"\"");

        if(imageUrls==null){
            throw new IllegalArgumentException("No image list provided");
        }
        imageGridAdapter=new ImageGridAdapter(imageUrls,this);
        imageGrid.setAdapter(imageGridAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(ARG_IMAGE_LIST,imageUrls);
        outState.putString(ARG_IMAGE_QUERY,title);
    }

    public static Intent getIntentForActivity(ArrayList<String> list,String query, Context context){

        Intent intent=new Intent(context,ImageGridActivity.class);
        intent.putExtra(ARG_IMAGE_LIST,list);
        intent.putExtra(ARG_IMAGE_QUERY,query);
        return intent;
    }


}

class ImageGridView {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    public ImageGridView(View itemView) {
        ButterKnife.bind(this,itemView);
    }
}
class ImageGridAdapter extends BaseAdapter{


    ArrayList<String> imageList;
    Context context;

    LayoutInflater inflater;
    ImageGridAdapter(ArrayList<String> imageList, Context context){
        this.imageList=imageList;
        this.context=context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(imageList==null){
            return 0;
        }
        return imageList.size();
    }

    @Override
    public String getItem(int i) {
        return imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageGridView imageGridView;
        if(view==null){
            view=inflater.inflate(R.layout.item_image,null,false);
            imageGridView=new ImageGridView(view);
            view.setTag(imageGridView);
        }
        else{
            imageGridView= (ImageGridView) view.getTag();
        }

        String imageUrl=imageList.get(i);
        Picasso.with(context).load(imageUrl).placeholder(R.drawable.default_placeholder).into(imageGridView.ivImage);
        return view;
    }
}
