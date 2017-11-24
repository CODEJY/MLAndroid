package org.tensorflow.demo.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.EventLog;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import de.greenrobot.event.EventBus;

/**
 * Created by wujy on 2017/11/23.
 */

public class RecognizeImg {
    private static final String input_layer = "inputs/X";
    private static final String output_layer = "output/predict";
    private static final String MODEL_FILE = "file:///android_asset/rounded_graph.pb"; //asserts目录下的pb文件名字
    private static final int HEIGHT = 64;
    private static final int WIDTH = 256;
    private static final int CHANNEL = 1;
    private float[] inputs = new float[HEIGHT*WIDTH*CHANNEL];
    private long[] outputs = new long[11];

    private TensorFlowInferenceInterface inferenceInterface;
    RecognizeImg(Context context) {
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(),MODEL_FILE);
    }
    public void recognize(float[] pixelData){
        inputs=pixelData;

        inferenceInterface.feed(input_layer,inputs,1,16384);
        inferenceInterface.run(new String[] { output_layer }, false);//输出张量
        byte[] outPuts = new byte[88];
        inferenceInterface.fetch(output_layer,outPuts);
        long[] tOutputs=new long[11];
        //提取输出数据，每8个byte都是一样的，所以只取首个byte
        for (int i=0;i<11;i++)
        {
            int k=i*8;
            tOutputs[i]=outPuts[k];
        }
        //将刚才提取的输出通过ASCII码转换
        String outputStr="";
        for(int i=0;i<11;i++){
            long char_idx=tOutputs[i];
            long char_code = 0;
            if (char_idx<10){
                char_code = char_idx + (int)('0');
            }
            else if (char_idx<36){
                char_code = char_idx-10 + (int)('A');
            }
            else if (char_idx<62){
                char_code = char_idx + (int)('a');
            }
            outputStr+= (char)char_code;
        }
        EventBus.getDefault().post(outputStr);
    }

}
