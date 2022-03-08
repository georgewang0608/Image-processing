import graphs.Graph;

import java.util.*;

public class DynamicProgrammingSeamFinder implements SeamFinder {

    public List<Integer> findSeam(Picture picture, EnergyFunction f) {

        //basically what we do is we have to fill out a 2D array with pixel energy values (which can be get from f.apply),
        //also the 2D array is (column, row) format
        double [][] minEnergy = new double[picture.width()][picture.height()];

        //initialize the first column of minEnergy with the energy value from the picture
        for (int i = 0; i < picture.height(); i++) {
            minEnergy[0][i] = f.apply(picture, 0, i);
        }

        //for each column after, look at the energy of each pixel and add it to the minimum of the 3 predecessors of the pixel
        //(ie. left-middle, left-up, left-down)
        for (int m = 1; m < picture.width(); m ++) {
            for (int j = 0; j < picture.height(); j ++) {
                double minValue = f.apply(picture, m, j) + minEnergy[m - 1][j];
                if (j != 0) {
                    if (f.apply(picture, m, j) + minEnergy[m - 1][j - 1] < minValue) {
                        minValue = f.apply(picture, m, j) + minEnergy[m - 1][j - 1];
                    }
                }
                if (j != picture.height() - 1) {
                    if (f.apply(picture, m, j) + minEnergy[m - 1][j + 1] < minValue) {
                        minValue = f.apply(picture, m, j) + minEnergy[m - 1][j + 1];
                    }
                }
                minEnergy[m][j] = minValue;
            }
        }
        

        //now backtrack from right to left, adding y-coordinate of minimum energy pixel to arraylist, still following
        // rules
        List<Integer> horizontalSeam = new ArrayList<>();
        double minRightValue = minEnergy[picture.width() - 1][0];
        horizontalSeam.add(0);
        //for last column, find minimum weight value and add y-coordinate to arraylist
        for (int x = 1; x < picture.height(); x ++) {
            if (minEnergy[picture.width() - 1][x] < minRightValue) {
                minRightValue = minEnergy[picture.width() - 1][x];
                horizontalSeam.set(0, x);
            } 
        }
        int arrayIncrement = 1;
        for (int n = picture.width() - 2; n > -1; n --) {
            int yCoor = horizontalSeam.get(arrayIncrement - 1);
            double minValue = minEnergy[n][yCoor];
            horizontalSeam.add(yCoor);
            if (yCoor != 0) {
                if (minEnergy[n][yCoor - 1] < minValue) {
                    minValue = minEnergy[n][yCoor - 1];
                    horizontalSeam.set(arrayIncrement, yCoor - 1);
                }
            }
            if (yCoor != picture.height() - 1) {
                if (minEnergy[n][yCoor + 1] < minValue) {
                    horizontalSeam.set(arrayIncrement, yCoor + 1);
                }
            }
            arrayIncrement++;
        }
        Collections.reverse(horizontalSeam);
        return horizontalSeam;
    }
}
