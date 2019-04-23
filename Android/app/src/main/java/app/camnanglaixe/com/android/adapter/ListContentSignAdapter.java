package app.camnanglaixe.com.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.activities.ListSignActivity;
import app.camnanglaixe.com.android.models.ContentDetailRule;

/**
 * Created by taypham on 06/12/2016.
 */
public class ListContentSignAdapter extends BaseAdapter {

    public List<ContentDetailRule> contentDetailRules;
    private LayoutInflater mInflater;
    private ListSignActivity context;

    public ListContentSignAdapter(ListSignActivity context, List<ContentDetailRule> contentDetailRules) {
        this.context = context;
        this.contentDetailRules = contentDetailRules;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contentDetailRules.size();
    }

    @Override
    public Object getItem(int i) {
        return contentDetailRules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.adapter_list_content_sign, null);
            holder.imv = (ImageView) view.findViewById(R.id.sign_icon);
            holder.title = (TextView) view.findViewById(R.id.sign_title);
            holder.layout = (LinearLayout) view.findViewById(R.id.topic_tab_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(contentDetailRules.get(i).title);
        if(contentDetailRules.get(i).image!=null&&!contentDetailRules.get(i).image.equals("")) {
            holder.imv.setVisibility(View.VISIBLE);
            holder.imv.setImageDrawable(CommonUtils.getDrawableResourceByName(context, contentDetailRules.get(i).image.toLowerCase()));
        }
        else {
            holder.imv.setVisibility(View.INVISIBLE);
            //holder.imv.setImageDrawable(null);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.showDialogContent(contentDetailRules.get(i).title, contentDetailRules.get(i).detail, context.getResources().getIdentifier(contentDetailRules.get(i).image.toLowerCase(), "drawable", context.getPackageName()));
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView imv;
        TextView title;
        LinearLayout layout;
    }
}
