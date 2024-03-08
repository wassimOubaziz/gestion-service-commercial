package com.example.sijili.users.clientactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sijili.R;

public class RessourcesActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ressources);
        Log.d("FaqActivity", "onCreate: FaqActivity started.");

        listView = findViewById(R.id.listView);
        MyAdapter myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

    }

    public void retru(View view) {
        finish();
    }

    private class MyAdapter extends BaseAdapter {
        private final Context context;
        private final String[] faqTitles;
        private final String[] faqDescriptions;
        public MyAdapter(Context context) {
            this.context = context;
            this.faqTitles = context.getResources().getStringArray(R.array.faq_titles);
            this.faqDescriptions = context.getResources().getStringArray(R.array.faq_descriptions);
        }
        @Override
        public int getCount() {
            return faqTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("MyAdapter", "getView: position=" + position);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.expandable_item, parent, false);

            LinearLayout motherLayout = myView.findViewById(R.id.motherLayout);
            RelativeLayout itemClicked =  myView.findViewById(R.id.itemClicked);
            ImageView arrowImg = myView.findViewById(R.id.arrowImg);
            LinearLayout discLayout = myView.findViewById(R.id.discLayout);

            TextView titleTextView = myView.findViewById(R.id.titleTextView);
            TextView disTextView = myView.findViewById(R.id.disTextView);

            titleTextView.setText(faqTitles[position]);
            disTextView.setText(faqDescriptions[position]);
            itemClicked.setOnClickListener(v -> {
                if (discLayout.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    discLayout.setVisibility(View.VISIBLE);
                    discLayout.setBackgroundColor(Color.parseColor("#FAFAFA")); // Add #
                    arrowImg.setRotation(-90);
                } else {
                    discLayout.setVisibility(View.GONE);
                    discLayout.setBackgroundColor(Color.parseColor("#FFFFFF")); // Add #
                    arrowImg.setRotation(0);
                }
            });

            return myView;
        }
    }
}