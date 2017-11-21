package com.edoardo.Inventario.classivarie;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edoardo.Inventario.R;

import java.util.ArrayList;

/**
 * Created by edoardo on 19/10/2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView id;
        TextView barcode;
        TextView quantita_number;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

/*        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }

*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;


        if (convertView == null) {
//            if (position % 2 == 0) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_item2, parent, false);
                viewHolder.id = (TextView) convertView.findViewById(R.id.id);
                viewHolder.barcode = (TextView) convertView.findViewById(R.id.barcode);
                viewHolder.quantita_number = (TextView) convertView.findViewById(R.id.quantita_number);

                //convertView.setBackgroundColor(mContext.getResources().getColor(R.color.mycolor));
                result=convertView;
//            } else {
//                viewHolder = new ViewHolder();
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                convertView = inflater.inflate(R.layout.row_item2, parent, false);

//                viewHolder.id = (TextView) convertView.findViewById(R.id.id);
//                viewHolder.barcode = (TextView) convertView.findViewById(R.id.barcode);
//                viewHolder.quantita_number = (TextView) convertView.findViewById(R.id.quantita_number);
                convertView.setBackgroundColor(Color.WHITE);
//                result=convertView;
//            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;
try {
    viewHolder.id.setText(dataModel.getId().toString());
    viewHolder.barcode.setText(dataModel.getBarcode());
    viewHolder.quantita_number.setText(Integer.toString(dataModel.getQta()));



}catch (Exception e){
    e.toString();
}

        // Return the completed view to render on screen
        return convertView;
    }
}