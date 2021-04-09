/*

Name: Stephen Cartner
Student Id: S1706321

 */

package org.me.gcu.equakestartercode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

class MySuperArrayAdapter extends ArrayAdapter<EarthquakeClass> {
    private Context context;
    ArrayList<EarthquakeClass> displayEarthquakeList = new ArrayList<EarthquakeClass>();

    public MySuperArrayAdapter(Context context, int resource, ArrayList<EarthquakeClass> values) {
        super(context, -1, values);
        System.out.println("Super Set");
        this.context = context;
        this.displayEarthquakeList = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_list, parent, false);

        if (displayEarthquakeList.size() > 0){

            EarthquakeClass earthquake = displayEarthquakeList.get(position);

            TextView txtLocation = (TextView) rowView.findViewById(R.id.titleTextView);
            txtLocation.setText(earthquake.getLocation());

            TextView txtDate = (TextView) rowView.findViewById(R.id.dateTextView);
            txtDate.setText(earthquake.getDate());

            TextView txtMag = (TextView) rowView.findViewById(R.id.magnitudeText);
            txtMag.setText(earthquake.getMagnitude());

            TextView txtDepth = (TextView) rowView.findViewById(R.id.depthText);
            txtDepth.setText(earthquake.getDepth());


            String[] separated = earthquake.getMagnitude().split(":");

            float displayMagnitude = Float.parseFloat(separated[1]);
            ImageView imgMag = (ImageView) rowView.findViewById(R.id.magnitudeImage);


            if (displayMagnitude > 0 && displayMagnitude <= 1)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.Green));
            }
            else if (displayMagnitude > 1 && displayMagnitude <= 2)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.ForestGreen));
            }
            else if (displayMagnitude > 2 && displayMagnitude <= 3)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.YellowGreen));
            }
            else if (displayMagnitude > 3 && displayMagnitude <= 4)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.LawnGreen));
            }
            else if (displayMagnitude > 4 && displayMagnitude <= 5)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.GreenYellow));
            }
            else if (displayMagnitude > 5 && displayMagnitude <= 6)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.Yellow));
            }
            else if (displayMagnitude > 6 && displayMagnitude <= 7)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.Orange));
            }
            else if (displayMagnitude > 7 && displayMagnitude <= 8)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.OrangeRed));
            }
            else if (displayMagnitude > 8 && displayMagnitude <= 9)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.Red));
            }
            else if (displayMagnitude > 9 && displayMagnitude <= 10)
            {
                imgMag.setColorFilter(ContextCompat.getColor(getContext(), R.color.DarkRed));
            }
        }


        return rowView;
    }

    @Override
    public void add(EarthquakeClass object) {

        //System.out.println("This Is Title");

        this.displayEarthquakeList.add(object);
        System.out.println(displayEarthquakeList.get(0).getTitle());
    }
}
