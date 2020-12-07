package edu.umich.invezt.invezt;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class patternRecognizer {

    public Mat support_resistance(Mat rgbMat, byte[] intensityBuffer, int width, int height) {
        int max_row = 100;
        int min_row = height - 100;

        int current_sum = 0;

        int[] values = new int[width];
        for (int i = 0; i < width; ++i){
            current_sum = 0;
            for (int j = 0; j < height; j++){
                int pos = j*width + i;
                int cur_val = (int)intensityBuffer[pos];
                if (cur_val > 123){
                    current_sum += 1;
                    //Imgproc.circle(rgbMat, new Point(i, j), 3, new Scalar(255, 255, 255), 2);
                }
            }
            values[i] = current_sum;
        }

        int max_max = 0;
        int min_max = 0;
        max_row = -1;
        min_row = -1;

        for(int i = 1; i < width - 1; ++i) {
            //Log.d("Test", "value: " + values[i] + "   " + i);
            int window_value = values[i] + values[i - 1] + values[i + 1];
            if(i < width / 2 && window_value > max_max) {
                max_row = i;
                max_max = window_value;
            }
            if(i > width / 2 && window_value > min_max) {
                min_row = i;
                min_max = window_value;
            }

        }

        Log.d("Test", "max row " + max_row + "   " + "min row: " + min_row);
        Point pt1 = new Point(0, max_row);
        Point pt2 = new Point(2000, max_row);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        pt1 = new Point(0, min_row);
        pt2 = new Point(2000, min_row);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        return rgbMat;
    }

    public Mat bull_bear_flag(Mat rgbMat, byte[] intensityBuffer, int width, int height){
        // This will be the function that handles bull and bear flag detection

        int max_row = 0;
        int min_row = height - 1;
        int min_col = 0;
        int max_col = width-1;

        // right most extrema variables
        int r_x = 0;
        int r_y = 0;
        int r_val = 0;
        // left most extrema variables
        int l_x = 0;
        int l_y = 0;
        int l_val = 0;

        // Find left most extrema
        for (int i = 0; i < width*0.20; i++){
            for (int j = 0; j < height; j++){
                int pos = j*width + i;
                int cur_val = (int)intensityBuffer[pos];

                // If this point brighter than current extrema
                if (cur_val > l_val){
                    l_val = cur_val;
                    l_x = i;
                    l_y = j;
                }
            }
        }


        // Find right most extrema
        for (int i = width-1; i > width-width*0.20; i--){
            for (int j = 0; j < height; j++){
                int pos = j*width + i;
                int cur_val = (int)intensityBuffer[pos];

                // If this point brighter than current extrema
                if (cur_val > r_val){
                    r_val = cur_val;
                    r_x = i;
                    r_y = j;
                }
            }
        }

        // Connect left to right most extrema
        // Mark the support and resistance lines
        Point pt1 = new Point(l_x, l_y);
        Point pt2 = new Point(r_x, r_y);
        Imgproc.line(rgbMat, pt1, pt2, new Scalar(255,255,255), 2);

        // draw support/resistance for right most extrema
        Point pt3 = new Point(r_x-500, r_y);
        Point pt4 = new Point(r_x, r_y);
        Imgproc.line(rgbMat, pt3, pt4, new Scalar(255,255,255), 2);


        return rgbMat;
    }

}
