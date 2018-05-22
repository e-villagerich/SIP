package UI;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evillari.sip.R;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by etv on 11/29/2017.
 */

public class FeedsAdapter extends ArrayAdapter<ActivityFeeds> {
    public FeedsAdapter(@NonNull Context context, int resource, @NonNull List<ActivityFeeds> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.dashboardfeeds, parent, false);
        }

        ImageView feedimageView = (ImageView) convertView.findViewById(R.id.iv_feedpic);
        TextView feedactivitytext = (TextView) convertView.findViewById(R.id.tv_activityfeed);
        TextView feednametext = (TextView) convertView.findViewById(R.id.tv_feedname);
        TextView feeddetailstext = (TextView) convertView.findViewById(R.id.tv_feeddetails);
        TextView feedtimetext = (TextView) convertView.findViewById(R.id.tv_feedtime);

        ActivityFeeds activityFeeds = getItem(position);


        if (activityFeeds.getPhotoURL() != null) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(5)
                    .oval(true)
                    .scaleType(ImageView.ScaleType.CENTER_CROP)
                    .build();
            Picasso.with(getContext())
                    .load(Uri.parse(activityFeeds.getPhotoURL()))
                    .transform(transformation)
                    .into(feedimageView);

        } else{
            feedimageView.setVisibility(View.GONE);
        }

        feedactivitytext.setText(activityFeeds.getActivity());
        feeddetailstext.setText(activityFeeds.getDetails());
        feednametext.setText(activityFeeds.getName());
        feedtimetext.setText(activityFeeds.getAdate());

        return convertView;
    }

}



