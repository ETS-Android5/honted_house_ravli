package com.pesonal.adsdk.vpn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.model.vpnmodel.CountryListItem;
import com.pesonal.adsdk.model.vpnmodel.ServerListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FreeServersAdapter extends RecyclerView.Adapter<FreeServersAdapter.FreeServersViewHolder> {

    private final ArrayList<String> strings;
    Context context;
    private Map<String, List<ServerListItem>> countryListItem;
    private CountryListItem country;
    private PassServerData mCallback;



    public FreeServersAdapter(Context context, Map<String, List<ServerListItem>> countryListItem, CountryListItem country,PassServerData passServerData) {
        this.context = context;
        this.countryListItem =countryListItem ;
        strings = new ArrayList<>(countryListItem.keySet());
        this.country = country;
        mCallback = passServerData;
    }

    @Override
    public FreeServersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new FreeServersViewHolder(inflater.inflate(R.layout.region_list_item, viewGroup, false));
    }

    public void onBindViewHolder(FreeServersViewHolder freeServersViewHolder, int i) {
        freeServersViewHolder.mRegionTitle.setText(strings.get(i));
        Glide.with(context).load(country.getFlagUrl()).into(freeServersViewHolder.mRegionImage);
        List<ServerListItem> serverListItems = countryListItem.get(strings.get(i));
        freeServersViewHolder.btnChange.setOnClickListener(view -> mCallback.getSelectedServer(serverListItems,country));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class FreeServersViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mItemView;
        private ImageView mRegionImage;
        private TextView mRegionTitle;
        private TextView btnChange;


        public FreeServersViewHolder(View view) {
            super(view);
            this.mRegionTitle = (TextView) view.findViewById(R.id.region_title);
            this.mRegionImage = (ImageView) view.findViewById(R.id.region_image);
            this.btnChange = (TextView) view.findViewById(R.id.btnChange);
            this.mItemView = (LinearLayout) view.findViewById(R.id.itemView);
        }
    }
}