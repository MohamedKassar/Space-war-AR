package fr.upmc.spacewarar.ocv.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import fr.upmc.spacewarar.ocv.interfaces.IObjectTracker;

public class CalculPosition implements IObjectTracker {


	public CalculPosition() {
		// TODO Auto-generated constructor stub
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
			double y = vCircle[1];

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

	public double calculPosition(Mat image) {
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

		Core.inRange(out, new Scalar(147, 150, 150), new Scalar(163, 255, 255), magentaHueRange);

		// Filtre gaussien
		Imgproc.GaussianBlur(magentaHueRange, magentaHueRange, new Size(9,9), 2, 2);

		Mat circles = new Mat();

		// Hough transformation
		Imgproc.HoughCircles(magentaHueRange, circles,
				Imgproc.CV_HOUGH_GRADIENT, 1,
				magentaHueRange.rows()/4, 100, 20, 0, 0);
		
		System.out.println(circles.cols());

		return circles;
	}

	private Mat detectGreenCircles(Mat out) {

		// Threshold the HSV image, keep only the red pixels
		Mat greenHueRange = new Mat();
		Mat magentaHueRange = new Mat();

		Core.inRange(out, new Scalar(43, 100, 100), new Scalar(69, 255, 255), greenHueRange);

		// Filtre gaussien
		Imgproc.GaussianBlur(greenHueRange, greenHueRange, new Size(9,9), 2, 2);

		Mat circles = new Mat();
		// Hough transformation
		Imgproc.HoughCircles(greenHueRange, circles,
				Imgproc.CV_HOUGH_GRADIENT, 1,
				greenHueRange.rows()/4, 100, 20, 0, 0);

		return circles;
	}
	
	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CalculPosition c = new CalculPosition();
		Mat image = Highgui.imread("C:\\Users\\hajar\\Pictures\\cercles.jpg");
		System.out.println("Position ="+c.calculPosition(image));
	}

}
