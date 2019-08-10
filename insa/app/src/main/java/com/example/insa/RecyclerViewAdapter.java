package com.example.insa;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    Context context;

    //이거는 아이템을 클릭했을 때 다른엑티비티로 넘어가기 위해 설정 .엑티비티에서 엑티비티 이동은 Context가 딱히 필요 없어 보인다만, 클래스에서 엑티비티로 이동시 필요한 것으로 추정됨.


    ArrayList<MapData> listData =new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

//넘어온 데이터를 담을 객체형 Arraylist를 선언.


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
// return 인자는 ViewHolder 입니다.


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ItemViewHolder(view);



//메인에서 RecyclerView를 바인딩하고 어댑터에 연결 시켰다면 어뎁터에서는 Recycler뷰에 들어갈 Item xml을 //Adapter에 연결 시킴. 따라서 리사이클러 뷰와 아이템 xml을 연결시킴.
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
// Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }
    @Override
    public int getItemCount() {
// RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(MapData map) {
// 외부에서 item을 추가시킬 함수입니다.
       if(listData==null){
           listData = new ArrayList<>();
       }

           listData.add(map);

        notifyDataSetChanged();

//아까 메인에서 넘긴 데이터는 이렇게 여기서 쌓임. 즉 addItem함수를 통해 listData를 만듬.
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        //이곳에서 각 Text나 Image를 바인딩 시키고 클릭이벤트에 대한 처리도 함. 클릭이벤트는 바인딩과 동시에 진행하므로,,



        String date;
        String owner_comment;
        String owner_date;
        String writer;
        String star;

        @BindView(R.id.listIV)
        ImageView listiv;

        @BindView(R.id.listTV)
                TextView listtv;








        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



        }

        void onBind(MapData data) {

            listtv.setText(data.getSimple());
            Glide.with(context).load(data.getUrl()).into(listiv);



        }
    }


}