package com.aniketvishal.commonindianwords;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrononciationFragment extends Fragment {

    TextToSpeech ttsObject;
    private int result;
    private EditText mSomething;
    private Button mSpeak;
    private TextView mStop;

    private AdView mAdView;

    public PrononciationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prononciation, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ttsObject != null ){
            ttsObject.stop();
            ttsObject.shutdown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ttsObject = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    result = ttsObject.setLanguage(Locale.UK);
                }else {
                    Toast.makeText(getActivity(),"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });

    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdView = (AdView)getActivity().findViewById(R.id.pron_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mSomething = (EditText)getActivity().findViewById(R.id.pron_speak_et);
        mSpeak = (Button)getActivity().findViewById(R.id.pron_speak_btn);
        mStop = (TextView) getActivity().findViewById(R.id.pron_stop_btn);

        ttsObject = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    result = ttsObject.setLanguage(Locale.getDefault());
                }else {
                    Toast.makeText(getActivity(),"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });

        mSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String somethingWord = mSomething.getText().toString();

                if (!TextUtils.isEmpty(somethingWord)){

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        Toast.makeText(getActivity(),"Feature Not Support in your Device",Toast.LENGTH_LONG).show();
                    }else {
                        ttsObject.speak(somethingWord, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }else {
                    Toast.makeText(getActivity(),"Please write something above",Toast.LENGTH_SHORT).show();
                }


            }
        });

        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ttsObject.stop();

            }
        });

    }
    @Override
    public void onDestroy() {


        //Close the Text to Speech Library
        if(ttsObject != null) {

            ttsObject.stop();
            ttsObject.shutdown();
            //   Log.d(TAG, "TTS Destroyed");
        }
        super.onDestroy();
    }

}
