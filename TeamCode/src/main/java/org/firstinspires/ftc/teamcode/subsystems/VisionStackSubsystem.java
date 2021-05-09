package org.firstinspires.ftc.teamcode.subsystems;

import com.technototes.logger.Stated;

import org.firstinspires.ftc.teamcode.commands.autonomous.AutoState;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class VisionStackSubsystem extends OpenCvPipeline implements Stated<Integer> {

    //We declare the mats ontop so we can reuse them later to avoid memory leaks
    private Mat matYCrCb = new Mat();
    private Mat matCbBottom = new Mat();
    private Mat matCbTop = new Mat();
    private Mat topBlock = new Mat();
    private Mat bottomBlock = new Mat();

    //Where the average CB value of the rectangles are stored
    private double topAverage;
    private double bottomAverage;

    //The max difference allowed inside the rectangles
    private int minThreshold = 10;
    private int maxThreshold = 30;


    //The position related to the screen
    private double topRectWidthPercentage = 0;
    private double topRectHeightPercentage = 0.55;
    private double bottomRectWidthPercentage = 0;
    private double bottomRectHeightPercentage = 0.64;

    //The width and height of the rectangles in terms of pixels
    private int rectangleWidth = 40;
    private int rectangleHeight = 10;

    private double upRectHeight = 0.5;
    private double upRectWidth = 0.05;

    public List<Integer> goal;
    public int mean;

    private OpenCvCamera webcam;

    public VisionStackSubsystem(OpenCvCamera w) {
        webcam = w;
        webcam.setPipeline(this);
        webcam.openCameraDeviceAsync(() -> webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT));
    }

    double avg;
    Scalar mean1, mean2, mean3;
    Mat m1 = new Mat(), m2 = new Mat(), m3 = new Mat();
    Rect r;

    Rect topRect, bottomRect;
    Scalar topMean, bottomMean;

    Scalar RED = new Scalar(255, 0, 0), GREEN = new Scalar(0, 255, 0);

    @Override
    public Mat processFrame(Mat input) {
        /**
         *input which is in RGB is the frame the camera gives
         *We convert the input frame to the color space matYCrCb
         *Then we store this converted color space in the mat matYCrCb
         *For all the color spaces go to
         *https://docs.opencv.org/3.4/d8/d01/group__imgproc__color__conversions.html
         */
        Imgproc.cvtColor(input, matYCrCb, Imgproc.COLOR_RGB2HSV);

        //The points needed for the rectangles are calculated here
        topRect = new Rect(
                (int) (matYCrCb.width() * topRectWidthPercentage),
                (int) (matYCrCb.height() * topRectHeightPercentage),
                rectangleWidth,
                rectangleHeight
        );

        bottomRect = new Rect(
                (int) (matYCrCb.width() * bottomRectWidthPercentage),
                (int) (matYCrCb.height() * bottomRectHeightPercentage),
                rectangleWidth,
                rectangleHeight
        );

        //The rectangle is drawn into the mat
        drawRectOnToMat(input, topRect, new Scalar(255, 0, 0));
        drawRectOnToMat(input, bottomRect, new Scalar(0, 255, 0));

        //We crop the image so it is only everything inside the rectangles and find the cb value inside of them
        topBlock = matYCrCb.submat(topRect);
        bottomBlock = matYCrCb.submat(bottomRect);
        Core.extractChannel(bottomBlock, matCbBottom, 0);
        Core.extractChannel(topBlock, matCbTop, 0);

        //We take the averaoge
        bottomMean = Core.mean(matCbBottom);
        topMean = Core.mean(matCbTop);

        bottomAverage = bottomMean.val[0];
        topAverage = topMean.val[0];

//        telemetry.addLine(""+getTopAverage());
//        telemetry.addLine(""+getBottomAverage());
//        telemetry.addLine(""+getStackSize());
//
//        telemetry.update();
        //return the mat to be shown onto the screen

//        Imgproc.cvtColor(input, matYCrCb, Imgproc.COLOR_RGB2RGBA);
//
//        goal = new ArrayList<>();
//        for(double d = 0; d<1; d+=upRectWidth){
//            r = new Rect((int) (matYCrCb.width() * d), 0, (int) (matYCrCb.width()*upRectWidth), (int) (matYCrCb.height()*upRectHeight));
//            drawRectOnToMat(input, r, new Scalar(255, 0, 0));
//
//            Core.extractChannel(matYCrCb.submat(r), m1, 0);
//            Core.extractChannel(matYCrCb.submat(r), m2, 1);
//            Core.extractChannel(matYCrCb.submat(r), m3, 2);
//
//            mean1 = Core.mean(m1);
//            mean2 = Core.mean(m2);
//            mean3 = Core.mean(m3);
//
//            avg = mean1.val[0]-mean2.val[0]-mean3.val[0]+100;
//            //mats.add(m);
//
//            if(avg>0) goal.add((int) Math.round(d/upRectWidth));
//
//
//            //telemetry.addLine(Math.round(d/upRectWidth)+": "+avg+" ");
//
//        }
//        m1.release();
//        m2.release();
//        m3.release();
//
////        telemetry.update();
//        mean = 0;
//        for(int i : goal){
//            mean+=i;
//        }
//        if(goal.size()!=0) mean/=goal.size();
////        telemetry.addLine(""+mean);
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            System.out.println("vision thread interrupted");
//        }
        return input;
    }

    /**
     * Draw the rectangle onto the desired mat
     *
     * @param mat   The mat that the rectangle should be drawn on
     * @param rect  The rectangle
     * @param color The color the rectangle will be
     */
    private void drawRectOnToMat(Mat mat, Rect rect, Scalar color) {
        Imgproc.rectangle(mat, rect, color, 1);
    }

    public int getStackSize(){
        return (minThreshold < getTopAverage() && maxThreshold > getTopAverage()) ?
                4 : (minThreshold < getBottomAverage() && maxThreshold > getBottomAverage()) ? 1 : 0;
    }

    public AutoState.StackSize getStackSizeEnum(){
        return getStackSize() == 4 ? AutoState.StackSize.FOUR : getStackSize() == 1 ? AutoState.StackSize.ONE : AutoState.StackSize.ZERO;
    }

    public double getTopAverage() {
        return topAverage;
    }

    public double getBottomAverage() {
        return bottomAverage;
    }

    @Override
    public Integer getState() {
        return getStackSize();
    }
}