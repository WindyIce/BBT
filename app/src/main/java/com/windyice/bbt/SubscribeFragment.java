package com.windyice.bbt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscribeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscribeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinner_subscribe;
    private Button button_subscribe;
    private Button button_delete;
    private Button button_return;
    private TextView textView_subscribed;
    private String[] topics;
    private String topicChosen;

    private OnFragmentInteractionListener mListener;

    public SubscribeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscribeFragment newInstance(String param1, String param2) {
        SubscribeFragment fragment = new SubscribeFragment();
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
        View view=inflater.inflate(R.layout.fragment_subscribe, container, false);
        spinner_subscribe=(Spinner) view.findViewById(R.id.subscribe_spinnerf);
        button_subscribe=(Button) view.findViewById(R.id.subscribe_buttonf);
        button_delete=(Button) view.findViewById(R.id.delete_buttonf);
        textView_subscribed=(TextView) view.findViewById(R.id.subscribe_textviewf);

        topics= new String[]{"Temperature","Brightness","dht"};

        textView_subscribed.setText("");
        for(String a:MainActivity.topicsChosen){
            textView_subscribed.append(a+"\n");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,topics);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_subscribe.setAdapter(adapter);
        spinner_subscribe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                topicChosen=topics[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                topicChosen="";
            }
        });

        //Add topics logic!!!
        button_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topicChosen.equals("")){
                    Toast.makeText(getActivity(),"Nothing selected!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(MainActivity.topicsChosen.contains(topicChosen)){
                        Toast.makeText(getActivity(),"Selected topic is chosen already!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.topicsChosen.add(topicChosen);
                        textView_subscribed.setText("");
                        for(String a:MainActivity.topicsChosen){
                            textView_subscribed.append(a+"\n");
                        }
                    }
                }
            }
        });

        //Delete topics logic!!!
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topicChosen.equals("")){
                    Toast.makeText(getContext(),"Nothing selected!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!MainActivity.topicsChosen.contains(topicChosen)){
                        Toast.makeText(getContext(),"Selected topic has not been chosen!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.topicsChosen.remove(topicChosen);
                        textView_subscribed.setText("");
                        for(String a:MainActivity.topicsChosen){
                            textView_subscribed.append(a+"\n");
                        }
                    }
                }
            }
        });

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
