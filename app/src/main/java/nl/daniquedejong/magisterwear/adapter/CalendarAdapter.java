package nl.daniquedejong.magisterwear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ilexiconn.magister.container.Appointment;

import java.util.ArrayList;

import nl.daniquedejong.magisterwear.R;

/**
 * Created by daniq on 7-4-2017.
 */

public class CalendarAdapter extends ArrayAdapter<Appointment> implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<Appointment> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView lesson;
        TextView lessonlocation;
        TextView lessontime;
        RelativeLayout row_item;
    }

    public CalendarAdapter(ArrayList<Appointment> data, Context context) {
        super(context, R.layout.row_item_appointment, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Appointment dataModel = (Appointment) object;

    }

    @Override
    public boolean onLongClick(View v) {
        //Toevoegen aan favoriet
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Appointment dataModel = (Appointment) object;
        return true;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Appointment dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_appointment, parent, false);
            viewHolder.lesson = (TextView) convertView.findViewById(R.id.lesson);
            //viewHolder.lessonlocation = (TextView) convertView.findViewById(R.id.lessonlocation);
            //viewHolder.lessontime = (TextView) convertView.findViewById(R.id.lessontime);
            viewHolder.row_item = (RelativeLayout) convertView.findViewById(R.id.row_item);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;

        String lesson = dataModel.description;
        String lessonlocation = dataModel.location;
        String lessontime = dataModel.startDate.getHours() + ":" +
                dataModel.startDate.getMinutes() + " - " +
                dataModel.endDate.getHours() + ":" +
                dataModel.endDate.getMinutes();


        viewHolder.lesson.setText(lesson);
        viewHolder.lessonlocation.setText(lessonlocation);
        viewHolder.lessontime.setText(lessontime);

        viewHolder.row_item.setOnClickListener(this);
        viewHolder.row_item.setOnLongClickListener(this);
        viewHolder.row_item.setTag(position);
        return convertView;
    }
}