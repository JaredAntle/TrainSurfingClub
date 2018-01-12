package com.example.install.trainsurfingclub;

import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.install.trainsurfingclub.MainActivity.fab;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView list;
    TextView RecordDescriptionTextView;

    private OnFragmentInteractionListener mListener;

    public RecordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordsFragment newInstance(String param1, String param2) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        //Hides the floating action button
        /*if(fab.isShown()){
            fab.hide();
        }*/
        fm = getActivity().getSupportFragmentManager();
        fab.show();
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft =fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_main, new CreateRecordFragment());
                ft.commit();
            }
        });
        //Creates listview for the records
        list = (ListView) view.findViewById(R.id.recordslist);
        DatabaseHandler db = new DatabaseHandler(getContext());
        final ArrayList<Record> recordslist = db.getAllRecords();
        db.closeDB();
        //Add all records into the listview
        /*recordslist.add(new Record("Most Riders on Train", "741", "https://www.random.org/"));
        recordslist.add(new Record("Longest Trip", "Jim Smithers - 36 days", "http://www.behindthename.com/random/"));
        recordslist.add(new Record("Most Trains Ridden", "Diana Taurasi - 218", "https://en.wikipedia.org/wiki/Diana_Taurasi"));
        recordslist.add(new Record("Fastest Train Jumped On", "Brian Scalabrine - 55kph", "https://en.wikipedia.org/wiki/Brian_Scalabrine"));
        recordslist.add(new Record("Most Injuries Sustained", "Charles Boyle - 49", "http://brooklyn99.wikia.com/wiki/Charles_Boyle"));*/
        final CustomAdapter adapter = new CustomAdapter(getContext(), recordslist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordDescriptionTextView = (TextView) view.findViewById(R.id.description);
                TextView details = (TextView) view.findViewById(R.id.details);
                ImageView chevron = (ImageView) view.findViewById(R.id.chevron);
                if(RecordDescriptionTextView.getText() != (recordslist.get(position)).getDescription()){
                    //Update text of description
                    RecordDescriptionTextView.setText(((Record) list.getItemAtPosition(position)).getDescription());
                    //Update text of show more
                    details.setText("Click to show less");
                    //Update chevron image
                    chevron.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }
                else {
                    RecordDescriptionTextView.setText("");
                    //Update text of show more
                    details.setText("Click to show more");
                    //Update chevron image
                    chevron.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseHandler db = new DatabaseHandler(getContext());
                Record record = recordslist.get(position);
                db.deleteRecord(record.getId());
                db.closeDB();
                recordslist.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }

    public class CustomAdapter extends ArrayAdapter<Record> {
        public CustomAdapter(Context context, ArrayList<Record> items) {
            super(context, 0, items);
        }

        /**
         * getView is used to take every item in a list and assign a view to it.
         * With this specific adapter we specified item_view as the view we want every
         * item in the list to look like.
         * After that item has the item_view attached to it we populate the item_view's
         * TextView called title
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final Record item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
            }
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(item.getTitle());
            ImageView image = (ImageView) convertView.findViewById(R.id.record);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Check first 7 characters of url (because it needs to be "http://")
                    String prefix = item.getUrl().substring(0,7);
                    System.out.println(prefix);
                    Uri webpage;
                    //Make sure the prefix is HTTP:// when changed to upperCase
                    if(prefix.toUpperCase().equals("HTTP://")){
                        //Change prefix to lowerCase (In case user made mistake) then add the rest of the url however the user entered it
                        webpage = Uri.parse(prefix.toLowerCase() + item.getUrl().substring(7, item.getUrl().length()));
                    }
                    else{
                        //If user did not add "http://", add it to the front of the url
                        webpage = Uri.parse("http://" + item.getUrl());
                    }
                    System.out.println(webpage);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }
            });
            return convertView;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
