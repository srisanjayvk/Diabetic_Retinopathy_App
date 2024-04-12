package com.example.DR.androiddiabeticretinopathyapp;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


class ColourAdapter extends BaseAdapter {

    Context context;
    String[] datavalues ;
    int [] colourvalues;

    public ColourAdapter(Context applicationContext, String[] data, int[] value) {

        this.datavalues = data;
        this.colourvalues = value;
        this.context = applicationContext;
    }

    @Override
    public int getCount() {
        return datavalues.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.rellayout = (RelativeLayout) convertView.findViewById(R.id.colurrel);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.headtitle);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.rellayout.setBackgroundColor(colourvalues[position]);
        holder.txtTitle.setText(datavalues[position]);

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        RelativeLayout rellayout;
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

}
