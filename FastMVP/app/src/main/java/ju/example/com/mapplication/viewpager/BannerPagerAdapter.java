package ju.example.com.mapplication.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zanlabs.widget.infiniteviewpager.InfinitePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ju.example.com.mapplication.R;

/**
 * Created by zhangchengju on 17/4/14.
 */
public class BannerPagerAdapter extends InfinitePagerAdapter {
    private List<String> mHomeSectionItemList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public BannerPagerAdapter(Context ctx) {
        mLayoutInflater = LayoutInflater.from(ctx);
        mContext = ctx;
    }


    public void updateSectionItemContentList(List<String> homeSectionItemList) {
        mHomeSectionItemList.clear();
        mHomeSectionItemList.addAll(homeSectionItemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mHomeSectionItemList == null) {
            return 0;
        } else {
            return mHomeSectionItemList.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ImageView imageView;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_home_gallery, container, false);
        }
        imageView = (ImageView) convertView.findViewById(R.id.imageview);
        Glide.with(mContext).load(mHomeSectionItemList.get(position)).asBitmap().dontAnimate().centerCrop().into(imageView);
        return convertView;
    }
}