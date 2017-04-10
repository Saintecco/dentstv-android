package com.jdmaestre.videotest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


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
    ArrayList<VideoInfo> videos;

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
        for (int n=0; n<videoDataModel.getVideoInfos().size(); n++){
            if (videoDataModel.getVideoInfos().get(n).getCategory().equals(category)){
                videos.add(videoDataModel.getVideoInfos().get(n));
            }

            if (videoDataModel.getVideoInfos().get(n).getCategory_2().equals(category)){
                videos.add(videoDataModel.getVideoInfos().get(n));
            }
        }

        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        videoDataModel.getVideoInfos().size();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        videosInfo_GridLLayout = (GridView) view.findViewById(R.id.videosInfo_gridLayout);
        VideosAdapter videosAdapter = new VideosAdapter();
        videosInfo_GridLLayout.setAdapter(videosAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Double horizontalSpacing = convertDpToPixel(8.0, getActivity());
        int horizontalspacingINT = horizontalSpacing.intValue();

        // config 1 column form portrait 2 for landscape
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

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final VideoInfo video = videos.get(i);
            View videoInfoView;
            videoInfoView = inflater.inflate(R.layout.custom_video_gridlayout, viewGroup, false);

            videoPreviewImage = (ImageView) videoInfoView.findViewById(R.id.videoPreview_imageView);
            videoName = (TextView) videoInfoView.findViewById(R.id.videoName_textView);
            //videoPreviewImage.setImageURI(Uri.parse(videos.get(i).getImage()));
            Picasso.with(getActivity()).load(video.getImage()).into(videoPreviewImage);
            videoName.setText(video.getName());

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
            return videos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
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
}
