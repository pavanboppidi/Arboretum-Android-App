package nwmissouri.edu.missouriarboretum;

/**
 * Created by S521840 on 7/1/2015.
 */
public class AverageAngle {
    private double[] mValues;
    private int mCurrentIndex;
    private int mNumberOfFrames;
    private boolean mIsFull;
    private double mAverageValue = Double.NaN;


    /**
     * COnstructor sets frames number and current index to 0 and creates an empty array of double values
     * @param frames set number of values to average
     */
    public AverageAngle(int frames)
    {
        this.mNumberOfFrames = frames;
        this.mCurrentIndex = 0;
        this.mValues = new double[frames];
    }

    /**
     *
     * @param d Each sensor values are added to mValues array and index is incremented.
     */


    public void putValue(double d)
    {
        mValues[mCurrentIndex] = d;
        if (mCurrentIndex == mNumberOfFrames - 1) {
            mCurrentIndex = 0;
            mIsFull = true;
        } else {
            mCurrentIndex++;
        }
        updateAverageValue();
    }

    public double getAverage()
    {
        return this.mAverageValue;
    }


    /**
     * This method calculates circular mean of the sensor values in the mValues array.
     */
    private void updateAverageValue()
    {
        int numberOfElementsToConsider = mNumberOfFrames;
        if (!mIsFull) {
            numberOfElementsToConsider = mCurrentIndex + 1;
        }

        if (numberOfElementsToConsider == 1) {
            this.mAverageValue = mValues[0];
            return;
        }


        double sumSin = 0.0;
        double sumCos = 0.0;
        for (int i = 0; i < numberOfElementsToConsider; i++) {
            double v = mValues[i];
            sumSin += Math.sin(v);
            sumCos += Math.cos(v);
        }
        this.mAverageValue = Math.atan2(sumSin, sumCos);
    }
}


