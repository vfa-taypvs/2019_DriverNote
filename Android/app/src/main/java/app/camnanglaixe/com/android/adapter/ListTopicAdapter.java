package app.camnanglaixe.com.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.camnanglaixe.com.android.Common.CommonUtils;
import app.camnanglaixe.com.android.R;
import app.camnanglaixe.com.android.models.Topic;

/**
 * Created by taypham on 02/12/2016.
 */
public class ListTopicAdapter extends BaseAdapter {

    private List<Topic> topics;
    private LayoutInflater mInflater;
    private Context context;
    public ListTopicAdapter(Context context, List<Topic> topics){
        this.context = context;
        this.topics = topics;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return topics.size();
    }

    @Override
    public Object getItem(int i) {
        return topics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.adapter_list_main_topic, null);
            holder.imgv = (ImageView) view.findViewById(R.id.topic_icon);
            holder.title = (TextView) view.findViewById(R.id.topic_title);
            holder.layout = (RelativeLayout) view.findViewById(R.id.topic_tab_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(topics.get(i).name);
//        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//            holder.layout.setBackground(CommonUtils.getDrawableResourceByName(context, "main_color_" + i + "_selector"));
//        }
//        else {
//            holder.layout.setBackgroundDrawable(CommonUtils.getDrawableResourceByName(context, "main_color_" + i + "_selector"));
//        }
        int index = i + 1;
        String count = (index < 10) ? ("0" + index) : String.valueOf(index);
        String icon_name = "icon" + count;
//        if(topics.get(i).icon!=null&&!topics.get(i).icon.equals(""))
//            holder.imgv.setImageDrawable(CommonUtils.getDrawableResourceByName(context, topics.get(i).icon));
            holder.imgv.setImageDrawable(CommonUtils.getDrawableResourceByName(context, icon_name));
        return view;

    }

    class ViewHolder {
        RelativeLayout layout;
        ImageView imgv;
        TextView title;
    }
}
