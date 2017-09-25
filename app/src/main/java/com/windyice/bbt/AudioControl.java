package com.windyice.bbt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.windyice.bbt.speech.util.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AudioControl extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = AudioControl.class.getSimpleName();
    //语音听写对象
    private SpeechRecognizer mAudio;
    //语音听写UI
    private RecognizerDialog mAudioDialog;
    //用HashMap存储听写结果
    private HashMap<String, String> mAudioResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mtoast;
    private SharedPreferences mSharedPreferences;
    //引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private boolean mTranslateEnable = false;

    @Override
    public void onClick(View view) {
        if (null == mAudio) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }

        switch (view.getId()) {
            //TODO:参数设置页面进入
            case R.id.image_iat_set:
                break;
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognize:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(AudioControl.this, "iat_recognize");

                mResultText.setText(null);//清空显示内容
                mAudioResults.clear();
                //设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    //显示听写对话框
                    mAudioDialog.setListener(mRecognizerDialogListener);
                    mAudioDialog.show();
                    showTip(getString(R.string.text_begin));
                } else {
                    //不显示听写对话框
                    //TODO  how to solve this fucking type change?!!!!!!

                    //ret=mAudio.startListening(mRecognizeListener);
                }
                break;
            //音频流识别
            case R.id.iat_recognize_stream:
                mResultText.setText(null);//
        }
    }

    @Override
    @SuppressLint("ShowToast")
    protected void onCreate(Bundle savedInstanceState) {
        SpeechUtility.createUtility(AudioControl.this, SpeechConstant.APPID + "=59c3d82b");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audio_control);


    }


    //初始化layout
    private void initLayout() {
        findViewById(R.id.iat_recognize).setOnClickListener(AudioControl.this);
        findViewById(R.id.iat_recognize_stream).setOnClickListener(AudioControl.this);
        findViewById(R.id.iat_upload_contacts).setOnClickListener(AudioControl.this);
        findViewById(R.id.iat_upload_userwords).setOnClickListener(AudioControl.this);
        findViewById(R.id.iat_stop).setOnClickListener(AudioControl.this);
        findViewById(R.id.iat_cancel).setOnClickListener(AudioControl.this);
        findViewById(R.id.image_iat_set).setOnClickListener(AudioControl.this);
        //在线听写
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (null == mAudio) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
                    return;
                }

                switch (checkedId) {
                    case R.id.iatRadioCloud:
                        mEngineType = SpeechConstant.TYPE_CLOUD;
                        findViewById(R.id.iat_upload_contacts).setEnabled(true);
                        findViewById(R.id.iat_upload_userwords).setEnabled(true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    int ret = 0;

    private void showTip(final String str) {
        mtoast.setText(str);
        mtoast.show();
    }

    public void setParam() {
        //清空参数
        mAudioDialog.setParameter(SpeechConstant.PARAMS, null);

        //设置听写引擎
        mAudioDialog.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置返回结果格式
        mAudioDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string.pref_key_translate), false);
        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mAudioDialog.setParameter(SpeechConstant.ASR_SCH, "1");
            mAudioDialog.setParameter(SpeechConstant.ADD_CAP, "translate");
            mAudioDialog.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mAudioDialog.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mAudioDialog.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mAudioDialog.setParameter(SpeechConstant.ORI_LANG, "en");
                mAudioDialog.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mAudioDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mAudioDialog.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mAudioDialog.setParameter(SpeechConstant.ORI_LANG, "cn");
                mAudioDialog.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mAudioDialog.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mAudioDialog.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mAudioDialog.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mAudioDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mAudioDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    //听写UI监听器
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (mTranslateEnable) {
                printTransResult(recognizerResult);
            } else {
                printResult(recognizerResult);
            }

            if (b) {
                //TODO the result
            }
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    };

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            showTip("解析结果失败，请确认是否已开通翻译功能。");
        } else {
            mResultText.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
        }

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAudioResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mAudioResults.keySet()) {
            resultBuffer.append(mAudioResults.get(key));
        }

        mResultText.setText(resultBuffer.toString());
        mResultText.setSelection(mResultText.length());
    }

    //听写监听器
    private RecognizerListener mRecognizeListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onBeginOfSpeech() {
            //此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }


        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            Log.d(TAG, recognizerResult.getResultString());
            if (mTranslateEnable) {
                printTransResult(recognizerResult);
            } else {
                printResult(recognizerResult);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }


        @Override
        public void onError(SpeechError speechError) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && speechError.getErrorCode() == 14002) {
                showTip(speechError.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                showTip(speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

}
