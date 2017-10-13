package com.windyice.bbt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final String HOST="tcp://39.108.118.166:1883";
    private final String clientId="2233";

    private MqttBaseOperation mqttBaseOperation;

    private EditText editText_topic;
    private EditText editText_message;
    private Button button_publish;

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void mqttOp(){
        try {
            mqttBaseOperation.Setting(false,10,20);
            mqttBaseOperation.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //reconnection can be here.
                    Toast.makeText(getContext(),cause.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(MainActivity.topicsChosen.contains(topic)) {
                        System.out.println("messageArrived----------");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = topic + "---" + message.toString();
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //after publish
                    Toast.makeText(getContext(),"DeliverComplete-------"+token.isComplete(),Toast.LENGTH_SHORT).show();
                }
            });
            //mqttClient.connect(mqttConnectOptions);
            mqttBaseOperation.connect(true);

        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class PublishThread implements Runnable {
        private String topic;
        private byte[] message;
        public PublishThread(String _topic,byte[] _message){
            topic=_topic;
            message=_message;
        }
        @Override
        public void run() {
            try {
                MqttMessage mqttMessage=new MqttMessage(message);
                mqttBaseOperation.publish(topic, mqttMessage);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void publish(String topic,String message){
        PublishThread publishThread=new PublishThread(topic,message.getBytes());
        new Thread(publishThread).start();
        mqttBaseOperation.startReconnect(3000,true);
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
        View view=inflater.inflate(R.layout.fragment_publish, container, false);

        editText_topic=view.findViewById(R.id.publish_topic_edittext);
        editText_message=view.findViewById(R.id.publish_message_edittext);
        button_publish=view.findViewById(R.id.publish_buttonf);

        mqttBaseOperation=new MqttBaseOperation(HOST,clientId);

        mqttOp();

        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic=editText_topic.getText().toString();
                String message=editText_message.getText().toString();
                publish(topic,message);
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
