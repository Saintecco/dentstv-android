package com.jdmaestre.videotest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String category;
    private String mParam2;

    private VideoDataModel videoDataModel = VideoDataModel.getInstance();

    private OnFragmentInteractionListener mListener;

    private GridView videosInfo_GridLLayout;
    ArrayList<VideoInfo> videosDataAdapter;
    ArrayList<VideoInfo> videos;

    VideosAdapter videosAdapter = new VideosAdapter();

    public VideoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoListFragment newInstance(String category, String param2) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        videos = new ArrayList<VideoInfo>();
        videosDataAdapter = new ArrayList<VideoInfo>();
        for (int n=0; n<videoDataModel.getVideoInfos().size(); n++){
            if (videoDataModel.getVideoInfos().get(n).getCategory().equals(category)){
                videos.add(videoDataModel.getVideoInfos().get(n));
            }

            if (videoDataModel.getVideoInfos().get(n).getCategory_2() != null){
                if (videoDataModel.getVideoInfos().get(n).getCategory_2().equals(category)){
                    videos.add(videoDataModel.getVideoInfos().get(n));
                }
            }
        }

        videosDataAdapter.addAll(videos);

        //Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        //videoDataModel.getVideoInfos().size();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        final EditText searchEditText = (EditText) view.findViewById(R.id.searchVideo_EditText);


        videosInfo_GridLLayout = (GridView) view.findViewById(R.id.videosInfo_gridLayout);
        videosInfo_GridLLayout.setAdapter(videosAdapter);

        //Set search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = searchEditText.getText().toString().toLowerCase(Locale.getDefault());
                videosAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Hide keyboard on enter
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                }
                return false;
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Double horizontalSpacing = convertDpToPixel(8.0, getActivity());
        int horizontalspacingINT = horizontalSpacing.intValue();

        // Config 1 column form portrait 2 for landscape
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            videosInfo_GridLLayout.setColumnWidth(width/2 - horizontalspacingINT/2);
            videosInfo_GridLLayout.setHorizontalSpacing(horizontalspacingINT-2);
        }else{
            videosInfo_GridLLayout.setColumnWidth(width);
        }



        return view;

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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    private class VideosAdapter extends BaseAdapter{

        ImageView videoPreviewImage;
        TextView videoName;
        ImageView playButton;

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final VideoInfo video = videosDataAdapter.get(i);
            View videoInfoView;
            videoInfoView = inflater.inflate(R.layout.custom_video_gridlayout, viewGroup, false);

            videoPreviewImage = (ImageView) videoInfoView.findViewById(R.id.videoPreview_imageView);
            videoName = (TextView) videoInfoView.findViewById(R.id.videoName_textView);
            playButton = (ImageView) videoInfoView.findViewById(R.id.playVideoButton);

            if (video.getImage() != null){
                Picasso.with(getActivity()).load(video.getImage()).into(videoPreviewImage);
            }else{
                Picasso.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/dentstv-b5c20.appspot.com/o/crop.png?alt=media&token=f0e01a49-8ebe-4b2c-af67-6686fc792939").into(videoPreviewImage);
            }

            videoName.setText(video.getName());

            //Set button position
            setOnCentre(playButton, videoPreviewImage);

            // Set video name
            String name = video.getName();
            videoName.setText(name);

            // Set video play
            videoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                    intent.putExtra("videoLink", video.getLink());
                    startActivity(intent);
                }
            });

            videoPreviewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                    intent.putExtra("videoLink", video.getLink());
                    startActivity(intent);
                }
            });

            //Toast.makeText(getActivity(), videos.get(i).getImage(), Toast.LENGTH_SHORT).show();
            return videoInfoView;
        }

        @Override
        public int getCount() {
            return videosDataAdapter.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            videosDataAdapter.clear();
            if (charText.length() == 0) {
                videosDataAdapter.addAll(videos);
            } else {
                for (VideoInfo video : videos) {
                    if (video.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        videosDataAdapter.add(video);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    private float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private  double convertDpToPixel(double dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        double px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void setOnCentre(View toCentre, View onCentre){
        float onCetreWidth = onCentre.getWidth();
        float onCetreHeight = onCentre.getHeight();

        float toCentreWidth = toCentre.getWidth();
        float toCentreHeight = toCentre.getHeight();

        toCentre.setX(onCetreWidth/2 - toCentreWidth/2);
        toCentre.setY(onCetreHeight/2 - toCentreHeight/2);

    }
}
