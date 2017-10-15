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
 * {@link ControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {
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
    private String prefix="";

    private Button button_control_0;
    private Button button_control_1;
    private Button button_control_2;
    private Button button_control_3;
    private Button button_control_4;
    private Button button_control_5;
    private Button button_control_6;
    private Button button_control_7;
    private Button button_control_8;
    private Button button_control_9;
    private Button button_control_10;
    private Button button_control_11;

    private EditText editText_prefix;
    private Button button_prefix;

    private MqttBaseOperation mqttBaseOperation;

    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
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
        View view=inflater.inflate(R.layout.fragment_control, container, false);
        button_control_0=view.findViewById(R.id.control0_buttonf);
        button_control_1=view.findViewById(R.id.control1_buttonf);
        button_control_2=view.findViewById(R.id.control2_buttonf);
        button_control_3=view.findViewById(R.id.control3_buttonf);
        button_control_4=view.findViewById(R.id.control4_buttonf);
        button_control_5=view.findViewById(R.id.control5_buttonf);
        button_control_6=view.findViewById(R.id.control6_buttonf);
        button_control_7=view.findViewById(R.id.control7_buttonf);
        button_control_8=view.findViewById(R.id.control8_buttonf);
        button_control_9=view.findViewById(R.id.control9_buttonf);
        button_control_10=view.findViewById(R.id.control10_buttonf);
        button_control_11=view.findViewById(R.id.control11_buttonf);

        editText_prefix=view.findViewById(R.id.subscribe_edittext);
        button_prefix=view.findViewById(R.id.addprefix_buttonf);

        button_prefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefix=editText_prefix.getText().toString();
            }
        });

        mqttBaseOperation=new MqttBaseOperation(HOST,clientId);

        mqttOp();

        button_control_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("0");
            }
        });

        button_control_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("1");
            }
        });

        button_control_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("2");
            }
        });

        button_control_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("3");
            }
        });

        button_control_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("4");
            }
        });

        button_control_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("5");
            }
        });

        button_control_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("6");
            }
        });

        button_control_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("7");
            }
        });

        button_control_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("8");
            }
        });

        button_control_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("9");
            }
        });

        button_control_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("10");
            }
        });

        button_control_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control("11");
            }
        });


        return view;
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

    private class ControlThread implements Runnable {
        private byte[] toPass;
        private String topic;
        public ControlThread(String _topic,byte[] a){
            topic=_topic;
            toPass=a;
        }
        @Override
        public void run() {
            try {
                MqttMessage mqttMessage=new MqttMessage(toPass);
                mqttBaseOperation.publish(topic, mqttMessage);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void control(String signal){
        if(prefix.equals("")){
            ControlThread controlThread=new ControlThread("wuzeen",signal.getBytes());
            new Thread(controlThread).start();
            mqttBaseOperation.startReconnect(3000,true);
        }
        else {
            ControlThread controlThread = new ControlThread(prefix,signal.getBytes());
            new Thread(controlThread).start();
            mqttBaseOperation.startReconnect(3000, true);
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
