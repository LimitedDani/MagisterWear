package nl.daniquedejong.magisterwear.adapter;

import android.support.wear.widget.WearableRecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ilexiconn.magister.container.Appointment;

import java.util.ArrayList;

import nl.daniquedejong.magisterwear.R;

/**
 * Created by daniq on 11/3/2017.
 */

public class CalendarRecAdapter extends
        WearableRecyclerView.Adapter<CalendarRecAdapter.ViewHolder> {

    private static final String TAG = "CustomRecyclerAdapter";

    private ArrayList<Appointment> mDataSet;
    private Controller mController;
    public static class ViewHolder extends WearableRecyclerView.ViewHolder {

        private final TextView lesson;
        private final TextView lessonlocation;
        private final TextView lessontime;
        private final TextView period;
        public ViewHolder(View view) {
            super(view);
            lesson = (TextView) view.findViewById(R.id.lesson);
            lessonlocation = (TextView) view.findViewById(R.id.lessonlocation);
            lessontime = (TextView) view.findViewById(R.id.lessontime);
            period = (TextView) view.findViewById(R.id.period);
        }

        @Override
        public String toString() { return (String) lesson.getText(); }
    }

    public CalendarRecAdapter(ArrayList<Appointment> dataSet, Controller controller) {
        mDataSet = dataSet;
        mController = controller;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_appointment, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        String lesson = mDataSet.get(position).description;
        String lessonlocation = mDataSet.get(position).location;
        String lessontime = mDataSet.get(position).startDate.getHours() + ":" +
                mDataSet.get(position).startDate.getMinutes() + " - " +
                mDataSet.get(position).endDate.getHours() + ":" +
                mDataSet.get(position).endDate.getMinutes();
        viewHolder.lesson.setText(lesson);
        viewHolder.lessonlocation.setText(lessonlocation);
        viewHolder.lessontime.setText(lessontime);
        viewHolder.period.setText(mDataSet.get(position).periodFrom + "");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}