package com.example.install.trainsurfingclub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static com.example.install.trainsurfingclub.MainActivity.fab;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChampionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChampionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChampionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Create viewPager and sectionPager Adapter
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;

    private OnFragmentInteractionListener mListener;

    public ChampionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChampionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChampionsFragment newInstance(String param1, String param2) {
        ChampionsFragment fragment = new ChampionsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_champions, container, false);
        //Hide floating action button
        if(fab.isShown()){
            fab.hide();
        }
        sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.championcontent);
        viewPager.setAdapter(sectionPagerAdapter);
        //Use the zoom out page transformer when using buttons in viewPager
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //Create imageButtons for navigating right and left in viewPager
        ImageButton leftButton = (ImageButton) view.findViewById(R.id.left_nav);
        ImageButton rightButton = (ImageButton) view.findViewById(R.id.right_nav);

        //Navigates left and goes to the last item when at the first one
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location = viewPager.getCurrentItem();
                if(location > 0){
                    location--;
                    viewPager.setCurrentItem(location);
                } else if(location == 0){
                    viewPager.setCurrentItem(viewPager.getChildCount()+2);
                }
            }
        });
        //Navigates right and goes to the first item when at the last one
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location = viewPager.getCurrentItem();
                location++;
                if(location >= viewPager.getChildCount()+2){
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(location);
                }
            }
        });
        return view;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        public SectionPagerAdapter(FragmentManager fm){
            super(fm);
        }
        //getItem switch statement is for each of the champion cases in the viewPager
        public Fragment getItem(int position){
            switch(position){
                case 0:
                    return ChampFragment.newInstance("Steve Rogers", R.drawable.rogers, "1940-1980");
                case 1:
                    return ChampFragment.newInstance("Dikembe Mutumbo", R.drawable.mutumbo, "1981-1986");
                case 2:
                    return ChampFragment.newInstance("Jim Smithers", R.drawable.smithers, "1987-1999, 2004-2007");
                case 3:
                    return ChampFragment.newInstance("Diana Taurasi", R.drawable.taurasi, "2000-2003");
                case 4:
                    return ChampFragment.newInstance("Brian Scalabrine", R.drawable.scalabrine, "2008-present");
                default:
                    return ChampFragment.newInstance("Name", R.drawable.rogers, "Years holding the title");
            }
        }
        //Set the number of cases to 5
        public int getCount(){
            return 5;
        }
    }

    //ZoomOutPageTransformer for animating the transition between pages in the viewPager
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
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
