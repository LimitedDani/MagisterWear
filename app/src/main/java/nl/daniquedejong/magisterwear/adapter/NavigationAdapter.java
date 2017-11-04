package nl.daniquedejong.magisterwear.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.drawer.WearableNavigationDrawer;

import java.util.List;

import nl.daniquedejong.magisterwear.R;

public class NavigationAdapter extends WearableNavigationDrawer.WearableNavigationDrawerAdapter {

    final private Context context;
    private final List<String> items;

    public NavigationAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public String getItemText(int i) {
        return items.get(i);
    }

    @Override
    public Drawable getItemDrawable(int i) {
        if(items.get(i).equals("Favorieten")) {
            //return ContextCompat.getDrawable(context, R.drawable.ic_favorite_white_24dp);
        }
        if(items.get(i).equals("Help")) {
            //return ContextCompat.getDrawable(context, R.drawable.ic_help_outline_white_24dp);
        }
        //return ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black_24dp);
        return null;
    }

    @Override
    public void onItemSelected(int i) {

    }

    @Override
    public int getCount() {
        return items.size();
    }
}
