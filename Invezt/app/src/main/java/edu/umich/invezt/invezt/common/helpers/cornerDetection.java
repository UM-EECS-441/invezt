package edu.umich.invezt.invezt.common.helpers;

import android.media.Image;
import android.util.Log;

import com.quickbirdstudios.yuv2mat.Yuv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;

import edu.umich.invezt.invezt.patternRecognizer;


public class cornerDetection {

    public synchronized ByteBuffer detect(String pattern_name, int width, int height, int stride, Image input) {
        System.loadLibrary("opencv_java4");

        // convert from yuv image to rgb mat, then to grayscale
        Mat mat = Yuv.rgb(input);

        byte[] test = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0,0, test);

        Mat matGray = new Mat();
        Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_RGB2GRAY);

        Mat outMat = Harris(matGray, 10000);

        // Convert back to rgb to produce Y bytes
        Mat rgbMat = new Mat();
        Imgproc.cvtColor(outMat, rgbMat, Imgproc.COLOR_GRAY2RGB);

        int size = (int) (rgbMat.total() * rgbMat.channels());
        byte[] rgb = new byte[size];
        rgbMat.get(0,0, rgb);

        byte[] intensityBuffer = new byte[(width * height)];

        // Convert values from rgbMat to Y bytes

        for(int i = 0; i < size / 3; ++i) {
            double R = rgb[3 * i];
            double G = rgb[3 * i + 1];
            double B = rgb[3 * i + 2];
            int y = (int) ((0.299 * R) + (0.587 * G) + (0.114 * B));
            intensityBuffer[i] = (byte) y;
        }

        patternRecognizer patternRec = new patternRecognizer();

        // Get the pattern string from the scroll selection bar
        //System.out.println(pattern_name);
        //Log.d("Pattern", "name =  " + pattern_name);
        if (pattern_name == ""){
            Log.d("Pattern", "PATTERN NOT FOUND");
        }
        switch (pattern_name) {
            case "BULL_BEAR_FLAGS":
                mat = patternRec.bull_bear_flag(mat, intensityBuffer, width, height);
                break;
            case "SUPPORT_RESISTANCE": // if support_resistance selected, or default, do support_resistance
            default:
                mat = patternRec.support_resistance(mat, intensityBuffer, width, height);
                break;
        }

        byte[] rgbBuffer = new byte[size];
        mat.get(0, 0, rgbBuffer);

        return ByteBuffer.wrap(rgbBuffer);
    }

    private Mat Harris(Mat Object, int thresh) {

        // This func implements the Harris Corner detection. The corners at intensity > thresh
        // are drawn.

        Mat Harris_object = new Mat();

        Mat harris_object_norm = new Mat(), harris_object_scaled = new Mat();
        int blockSize = 9;
        int apertureSize = 3;
        double k = 0.1;
        Imgproc.cornerHarris(Object, Harris_object, blockSize,apertureSize,k);

        Core.normalize(Harris_object, harris_object_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32FC1, new Mat());

        Core.convertScaleAbs(harris_object_norm, harris_object_scaled);

//        for( int j = 0; j < harris_object_norm.rows() ; j++){
//            for( int i = 0; i < harris_object_norm.cols(); i++){
//                if ((int) harris_object_norm.get(j,i)[0] > thresh){
//                    Imgproc.circle(harris_object_scaled, new Point(i,j), 5 , new Scalar(0), 2 ,8 , 0);
//                }
//            }
//        }

        return harris_object_scaled;
    }


}
