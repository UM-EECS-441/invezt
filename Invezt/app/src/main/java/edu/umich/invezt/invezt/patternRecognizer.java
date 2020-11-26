package edu.umich.invezt.invezt;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class patternRecognizer {

    public Mat support2(Mat rgbMat, byte[] intensityBuffer, String pattern, int width, int height) {
        int max_row = -1;
        int min_row = -1;

        int max_col = -1;
        int min_col = -1;

        int max_i = -1;
        int min_i = -1;


        for(int i = 0; i < width * height; ++i) {
            //Log.d("Test", "Intensity Value: " + i + ": " + intensityBuffer[i]);
            if(max_row == -1 && intensityBuffer[i] >= 50) {
                max_col = (int) i / width;
                max_row = (int) i % width;
            }
            else if(intensityBuffer[i] >= 50 && ((int) i / width) > min_row) {
                min_col = (int) i / width;
                min_row = (int) i % width;
            }
//
//            if(intensityBuffer[i] > 99 && max_i == -1) {
//                max_i = i;
//            }
//            if(intensityBuffer[i] > 99 && i > min_i) {
//                min_i = i;
//            }
        }

//        Log.d("Test", "min row " + min_i + ", max row " + max_i);
//        Imgproc.circle(rgbMat, new Point(max_row, max_col), 10, new Scalar(255, 255, 255), 3);
//        Imgproc.circle(rgbMat, new Point(min_row, min_col), 10, new Scalar(255, 255, 255), 3);


        // Mark the support and resistance lines
        Point pt1 = new Point(max_row - 2000, max_col);
        Point pt2 = new Point(max_row + 2000, max_col);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        pt1 = new Point(min_row - 2000, min_col);
        pt2 = new Point(min_row + 2000, min_col);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        return rgbMat;
    }

    public Mat support_resistance(Mat rgbMat, byte[] intensityBuffer, String pattern, int width, int height) {
        int max_row = 0;
        int min_row = height - 1;
        int max_counter = 0;
        int min_counter = 0;
        int current_sum = 0;
        int col = 0;
        int curr_row = 0;
        int max_col = 0;
        int min_col = 0;


        for(int i = 0; i < width * height; ++i) {
            if(col % width == 0) {
                current_sum = 0;
                ++curr_row;
            }
            ++col;

            int value = (int) intensityBuffer[i];
            if (value > -100){
                current_sum += 1;
            }


            if (current_sum > max_counter && i > max_row){
                max_counter = current_sum;
                max_row = curr_row;
                max_col = col % width;
            }
            else if (current_sum > min_counter && i < min_row){
                min_counter = current_sum;
                min_row = curr_row;
                min_col = col % width;
            }
        }

        Log.d("Test", "min row " + min_row + "max row " + max_row);

        // Mark the support and resistance lines
        Point pt1 = new Point(max_row, max_col - 50);
        Point pt2 = new Point(max_row, max_col + 50);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        pt1 = new Point(min_row, min_col - 50);
        pt2 = new Point(min_row, min_col + 50);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        return rgbMat;
    }

    public Mat bull_bear_flag(Mat rgbMat, byte[] intensityBuffer, String pattern, int width, int height){
        // This will be the function that handles bull and bear flag detection

        return rgbMat;
    }

}
