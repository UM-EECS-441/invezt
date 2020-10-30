package edu.umich.invezt.invezt.common.helpers;

import android.media.Image;
import android.util.Log;

import com.quickbirdstudios.yuv2mat.Yuv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class cornerDetection {

    public synchronized ByteBuffer detect(int width, int height, int stride, Image input) {
        System.loadLibrary("opencv_java4");

        // convert from yuv image to rgb mat, then to grayscale
        Mat mat = Yuv.rgb(input);

        Mat matGray = new Mat();
        Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_RGB2GRAY);

        Mat outMat = Harris(matGray, 10000);

        // Convert back to rgb to produce Y bytes
        Mat rgbMat = new Mat();
        Imgproc.cvtColor(outMat, rgbMat, Imgproc.COLOR_GRAY2RGB);

        int size = (int) (rgbMat.total() * rgbMat.channels());
        byte[] rgb = new byte[size];
        rgbMat.get(0,0, rgb);

        byte[] retBuffer = new byte[(width * height)];
        // Convert values from rgbMat to Y bytes


        int max_row = 0;
        int min_row = height - 1;
        int max_counter = 0;
        int min_counter = 0;
        int current_sum = 0;
        int count = 0;

        for(int i = 0; i < size / 3; ++i) {
            if(count % width == 0) current_sum = 0;
            ++count;
            double R = rgb[3 * i];
            double G = rgb[3 * i + 1];
            double B = rgb[3 * i + 2];
            int y = (int) ((0.299 * R) + (0.587 * G) + (0.114 * B));
            //if(y < 100) y = 0;
            retBuffer[i] = (byte) y;

            if (y > 150){
                current_sum += 1;
            }

            if (current_sum > max_counter && i > max_row){
                max_counter = current_sum;
                max_row = i;
            }
            else if (current_sum > min_counter && i < min_row){
                min_counter = current_sum;
                min_row = i;
            }
        }

        //marks support and resistance lines
        for (int j = 0; j < width; j++){
            retBuffer[max_row * width + j] = (byte)255;
            retBuffer[min_row * width + j] = (byte)255;
        }

        return ByteBuffer.wrap(retBuffer);
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