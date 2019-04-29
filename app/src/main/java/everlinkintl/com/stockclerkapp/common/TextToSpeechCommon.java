package everlinkintl.com.stockclerkapp.common;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import butterknife.BindString;
import everlinkintl.com.stockclerkapp.R;

//new TextToSpeechCommon(getContext()).speech("sssssss");
public class TextToSpeechCommon{
    private Context mContext;
    private TextToSpeech textToSpeech;
    @BindString(R.string.tts)
    String tts;

    public TextToSpeechCommon(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){

                }else {
                    Tools.ToastsShort(context, "语音失败");
                }
            }
        });
        textToSpeech.setPitch(1.0f);
        textToSpeech.setSpeechRate(1.0f);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speech(final String s) {
        if (textToSpeech != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    textToSpeech.speak(s, TextToSpeech.QUEUE_ADD, null, null);
                }
            }, 500);   //延迟秒
        } else {
            Tools.ToastsShort(this.mContext, tts);
        }
    }
}
