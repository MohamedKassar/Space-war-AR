package fr.upmc.spacewarar.ocv.impl;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.awt.image.DataBufferByte;


import com.github.sarxos.webcam.Webcam;

import fr.upmc.spacewarar.ocv.interfaces.IObjectTracker;

public class CalculPosition implements IObjectTracker {

	private static Webcam webcam = null;

	static {
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		nu.pattern.OpenCV.loadShared();
	}

	private double calculPositionRelative(Mat circlesGreen, Mat circleMagenta) {

		double longueurTable = 0;
		double[] centers = new double[2];

		// Pas besoin pour le moment
		// On considere que les centres des cercles correspondent 
		// aux positions 
		double[] radius = new double[2];

		for (int i = 0; i < circlesGreen.cols(); i++)
		{
			double vCircle[] = circlesGreen.get(0,i);

			// Cordonnee du centre du cercle
			double x = vCircle[0];
			//double y = vCircle[1];

			centers[i] = x;
			radius[i] = vCircle[2];
		}

		longueurTable = Math.abs(centers[1] - centers[0]); 

		double mCircle[] = circleMagenta.get(0, 0);

		double positionAbs = mCircle[0];

		double leftCircle;
		if(centers[1] > centers[0]) {
			leftCircle = centers[0];
		} else {
			leftCircle = centers[1];
		}

		return (positionAbs - leftCircle) * 100 / longueurTable;
	}

	public double calculPosition(Mat image) throws Exception {
		Mat out = new Mat(image.size(), CvType.CV_8UC1);

		// medianBlur for noise
		Imgproc.medianBlur(image, out, 3);

		// HSV conversion
		Imgproc.cvtColor(out, out, Imgproc.COLOR_BGR2HSV);

		Mat greenCircles = detectGreenCircles(out);
		Mat magentaCircle = detectMagentaCircle(out);

		return calculPositionRelative(greenCircles, magentaCircle);
	}

	private Mat detectMagentaCircle(Mat out) {

		// Threshold the HSV image, keep only the red pixels
		Mat magentaHueRange = new Mat();

		Core.inRange(out, new Scalar(147, 50, 50), new Scalar(163, 200, 200), magentaHueRange);

		// Filtre gaussien
		Imgproc.GaussianBlur(magentaHueRange, magentaHueRange, new Size(9,9), 2, 2);

		Mat circles = new Mat();

		// Hough transformation
		Imgproc.HoughCircles(magentaHueRange, circles,
				Imgproc.CV_HOUGH_GRADIENT, 1,
				magentaHueRange.rows()/4, 100, 20, 0, 0);

		return circles;
	}

	private Mat detectGreenCircles(Mat out) {

		// Threshold the HSV image, keep only the red pixels
		Mat greenHueRange = new Mat();

		Core.inRange(out, new Scalar(43, 50, 50), new Scalar(78, 200, 200), greenHueRange);

		// Filtre gaussien
		Imgproc.GaussianBlur(greenHueRange, greenHueRange, new Size(9,9), 2, 2);

		Mat circles = new Mat();
		// Hough transformation
		Imgproc.HoughCircles(greenHueRange, circles,
				Imgproc.CV_HOUGH_GRADIENT, 1,
				greenHueRange.rows()/4, 100, 20, 0, 0);
		
		return circles;
	}

	private Mat getImage() {
		return bufferedImageToMat(webcam.getImage());
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	public CalculPosition() {
		ScheduledThreadPoolExecutor sc = new ScheduledThreadPoolExecutor(1, r ->  {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		});
		sc.scheduleWithFixedDelay(this.new Job(), 0, 25, TimeUnit.MILLISECONDS);
	}

	private int position = 0;
	private class Job implements Runnable{
		@Override
		public void run() {
			try {
				position = (int) calculPosition(getImage());
				System.out.println("Position = "+position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public int getPosition() {
		return position;
	}
}
