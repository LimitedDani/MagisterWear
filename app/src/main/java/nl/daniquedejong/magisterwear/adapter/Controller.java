package nl.daniquedejong.magisterwear.adapter;

import nl.daniquedejong.magisterwear.MainActivity;
import nl.daniquedejong.magisterwear.fragment.magister.CalendarFragment;

/**
 * Created by daniq on 11/3/2017.
 */

public class Controller {
    public CalendarFragment mView;

    public Controller(CalendarFragment standaloneMainActivity) {
        mView = standaloneMainActivity;
    }
    public void itemSelected(String notificationStyleSelected) {
        mView.itemSelected(notificationStyleSelected);
    }
}
