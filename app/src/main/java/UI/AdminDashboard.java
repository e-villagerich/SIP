package UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.evillari.sip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by etv on 11/27/2017.
 */

public class AdminDashboard extends Fragment{

    private TextView textView;
    private ListView feedsListView;
    private FeedsAdapter feedsAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    public AdminDashboard(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_dashboard, container,false);

        textView = (TextView) rootView.findViewById(R.id.tv_label_whosonline);
        feedsListView = (ListView)rootView.findViewById(R.id.lv_feeds);


        List<ActivityFeeds> activityFeeds = new ArrayList<>();
        feedsAdapter = new FeedsAdapter(getContext(), R.layout.dashboardfeeds,activityFeeds);
        feedsListView.setAdapter(feedsAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ActivityFeeds");

        return rootView;
    }

    private void attachDbReadListener(){

        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    ActivityFeeds activityFeeds = dataSnapshot.getValue(ActivityFeeds.class);
                    feedsAdapter.insert(activityFeeds,0);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }

    }

    private void detachDbReadListener(){

        if(childEventListener!=null){
            databaseReference.removeEventListener(childEventListener);
            childEventListener= null;
        }

    }



    @Override
    public void onPause() {
        super.onPause();
        detachDbReadListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDbReadListener();
    }


}
