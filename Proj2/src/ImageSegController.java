import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The controller associated with the only view of our application. The
 * application logic is implemented here. It handles the button for
 * starting/stopping the camera, the acquired video stream, the relative
 * controls and the image segmentation process.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @version 1.5 (2015-11-24)
 * @since 1.0 (2013-12-20)
 *
 */
public class ImageSegController
{

	// FXML buttons
	@FXML
	private Button cameraButton;
	// the FXML area for showing the current frame
	@FXML
	private ImageView originalFrame;
	// checkbox for enabling/disabling Canny
	@FXML
	private CheckBox canny;
	// canny threshold value
	@FXML
	private Slider threshold;
	// checkbox for enabling/disabling background removal
	@FXML
	private CheckBox dilateErode;
	// inverse the threshold value for background removal
	@FXML
	private CheckBox inverse;

	@FXML
	public Label carta1;
	@FXML
	public Label carta2;
	@FXML
	public Label carta3;
	@FXML
	public Label carta4;

	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that performs the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive;


	/**
	 * The action triggered by pushing the button on the GUI
	 */
	@FXML
	protected void startCamera()
	{
		// set a fixed width for the frame
		originalFrame.setFitWidth(380);
		// preserve image ratio
		originalFrame.setPreserveRatio(true);

		if (!this.cameraActive)
		{
			this.capture.open(1);

			// is the video stream available?
			if (this.capture.isOpened())
			{

				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run()
					{
						Image imageToShow = grabFrame();
						originalFrame.setImage(imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
				this.cameraButton.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		}
		else
		{
			System.out.println("not active");
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.cameraButton.setText("Start Camera");
			// enable setting checkboxes
			//this.canny.setDisable(false);
			//this.dilateErode.setDisable(false);
			// stop the timer
			try
			{
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log the exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}

			// release the camera
			this.capture.release();
			// clean the frame
			this.originalFrame.setImage(null);
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Image} to show
	 */
	private Image grabFrame()
	{
		// init everything
		Image imageToShow = null;
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty())
				{
					// handle edge detection

					frame = this.doCanny(frame);

					// foreground detection


					// convert the Mat object (OpenCV) to Image (JavaFX)
					imageToShow = mat2Image(frame);
				}

			}
			catch (Exception e)
			{
				// log the (full) error
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}

		return imageToShow;
	}







	private Card parseCard(String c){

		String value= "";
		String suit= "";

		String[] card = c.split("\\.");

		//System.out.println(Arrays.toString(card));

		if(card[0].length()==2){
			//System.out.println("tem 2");
			char[] Str2 = new char[2];
			char[] Str3 = new char[2];
			card[0].getChars(0, 1, Str2, 0);
			card[0].getChars(1, 2, Str3, 0);
			value = Character.toString(Str2[0]);
			suit = Character.toString(Str3[0]);

		}else if(card[0].length()==3){
			//System.out.println("tem 3");
			char[] Str2 = new char[3];
			card[0].getChars(0, 2, Str2, 0);
			suit = Character.toString(Str2[2]);
			value = Character.toString(Str2[0])+Character.toString(Str2[1]);
		}

		value =parseValue(value);

		suit = parseSuit(suit);


		return new Card(value, suit);


	}

	private String parseSuit(String s){

		if(s.equals("c")){
			return "paus";
		}
		if(s.equals("d")){
			return "ouros";
		}
		if(s.equals("h")){
			return "copas";
		}
		if(s.equals(s)){
			return "espadas";
		}

		return "";

	}


	private String parseValue(String v){

		if(v.equals("k")){
			return "rei";
		}
		if(v.equals("j")){
			return "valete";
		}
		if(v.equals("q")){
			return "dama";
		}
		if(v.equals("a")){
			return "as";
		}


		return v;
	}

	private Mat doCanny(Mat frame)
	{
		// init
		Mat grayImage = new Mat();
		Mat detectedEdges = new Mat();
		Mat hierarchy = new Mat();
		Mat colorimage = frame;
		File[] files = new File("./cards/").listFiles();
		Mat finalimage = frame.clone();
		/*
		Imgproc.rectangle(colorimage, new Point(210, 0), new Point(420,240), new Scalar(0, 255, 0), 10);
		Imgproc.rectangle(colorimage, new Point(210, 240), new Point(420,480), new Scalar(0, 255, 0), 10);
		Imgproc.rectangle(colorimage, new Point(420, 120), new Point(640,360), new Scalar(0, 255, 0), 10);
		Imgproc.rectangle(colorimage, new Point(0, 120), new Point(210,360), new Scalar(0, 255, 0), 10);
*/
		//Transforma a frame numa grayimage


		Imgproc.cvtColor(colorimage, grayImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(grayImage, grayImage, new Size(1, 1), 0, 0);
		Imgproc.threshold(grayImage, grayImage, 120, 255, Imgproc.THRESH_BINARY);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//descobre os contornos das cartas
		Imgproc.findContours(grayImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		double largest_area =0;
		int largest_contour_index =-1;
		Rect bounding_rect= new Rect();
		for( int i = 0; i< contours.size(); i++ )
		{
			//  Find the area of contour
			double a=Imgproc.contourArea( contours.get(i),false);
			if(a>largest_area){
				largest_area=a;
				// Store the index of largest contour
				largest_contour_index=i;
				// Find the bounding rectangle for biggest contour
				bounding_rect=Imgproc.boundingRect(contours.get(i));

			}
		}
		//descobre as cartas(4 maiores rectangulos)
		List<MatOfPoint> ordered = new ArrayList<MatOfPoint>();
		for( int i = 0; i< contours.size(); i++ ){
			if(ordered.size()>=4){
				break;
			}
			// Store the index of largest contour
			largest_contour_index=i;
			// Find the bounding rectangle for biggest contour
			bounding_rect=Imgproc.boundingRect(contours.get(i));

			double x =bounding_rect.width;
			double y =bounding_rect.height;
			double ratio1 = x/y;
			double ratio2 = y/x;

			if(((ratio1>=1.4&& ratio1<=1.6)||(ratio2>=1.4&& ratio2<=1.6))&&(bounding_rect.width>50 && bounding_rect.height>50)){
				ordered.add(contours.get(i));
			}
		}
		Scalar color= new Scalar( 0,255,0);  // color of the contour in the
		//Draw the contour and rectangle
		Imgproc.Canny(grayImage, grayImage, 10, 300);
		//Rect bounding_rect2;
		Imgproc.drawContours(grayImage, contours, largest_contour_index, color);
		List<String> cartas = new ArrayList<String>();
		List<Point> pontos = new ArrayList<Point>();
		//para cada carta faz a homografia e compara com as cartas conhecidas
		for(int i=0; i<ordered.size();i++) {
			MatOfPoint2f NewMtx = new MatOfPoint2f(ordered.get(i).toArray());
			MatOfPoint2f aproxMtx = new MatOfPoint2f();
			double curve = Imgproc.arcLength(NewMtx, true);
			Imgproc.approxPolyDP(NewMtx, aproxMtx, 0.02 * curve, true);
			RotatedRect rectangle = Imgproc.minAreaRect(NewMtx);
			bounding_rect = rectangle.boundingRect();
			Mat dst_mat=new Mat(4,1,CvType.CV_32FC2);
			dst_mat.put(0,0,0.0,0.0,0.0,499.0, 499.0,499.0,499.0,0.0);
			Mat card = new Mat();

			if(aproxMtx.rows()==4) {//� rectangulo
				Imgproc.warpPerspective(colorimage, card, Imgproc.getPerspectiveTransform(aproxMtx, dst_mat), new Size(450, 450));
				Imgproc.cvtColor(card, card, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(card, card, new Size(5, 5), 2, 2);
				Imgproc.adaptiveThreshold(card,card,255,1,1,11,1);
				Imgproc.GaussianBlur(card, card, new Size(5, 5), 5, 5);

				//String name = "card"+id+".png";
				//id++;
				//System.out.println(name+" saved");
				//Imgcodecs.imwrite(name, card);

				Imgproc.rectangle(colorimage, new Point(bounding_rect.x, bounding_rect.y), new Point(bounding_rect.x + bounding_rect.width, bounding_rect.y + bounding_rect.height), new Scalar(0, 255, 0), 10);
				Mat gray1 = new Mat();
				Mat diff2 = new Mat();
				int cardj=0;
				double carddiff = -1;

				for(int j=0; j<files.length ;j++){
					Mat photo = Imgcodecs.imread(files[j].toString());
					Imgproc.cvtColor(photo, gray1, Imgproc.COLOR_BGR2GRAY);
					Point center = new Point(photo.cols()/2, photo.rows()/2);
					Mat rot90 = new Mat();
					Mat rotImage = Imgproc.getRotationMatrix2D(center, 90,1);
					Imgproc.warpAffine(gray1, rot90, rotImage, photo.size());

					//Imgproc.GaussianBlur(gray1, gray1, new Size(5, 5), 2, 2);
					//Imgproc.adaptiveThreshold(gray1,gray1,255,1,1,11,1);
					//Imgproc.GaussianBlur(gray1, gray1, new Size(5, 5), 5, 5);

					Core.absdiff(gray1,card, diff2);
					Imgproc.GaussianBlur(diff2, diff2, new Size(5, 5), 5, 5);
					Imgproc.threshold(diff2, diff2, 200, 255, Imgproc.THRESH_BINARY);
					if(j==0){
						carddiff= Core.sumElems(diff2).val[0];
					}else{
						if(Core.sumElems(diff2).val[0] < carddiff && carddiff>=0){
							carddiff = Core.sumElems(diff2).val[0];
							cardj=j;
						}
					}
					Core.absdiff(rot90,card, diff2);
					Imgproc.GaussianBlur(diff2, diff2, new Size(5, 5), 5, 5);
					Imgproc.threshold(diff2, diff2, 200, 255, Imgproc.THRESH_BINARY);
					if(j==0){
						carddiff= Core.sumElems(diff2).val[0];
					}else{
						if(Core.sumElems(diff2).val[0] < carddiff && carddiff>=0){
							carddiff = Core.sumElems(diff2).val[0];
							cardj=j;
						}
					}

				}

				pontos.add(new Point(bounding_rect.x, bounding_rect.y));
				cartas.add(files[cardj].getName());
				//System.out.println(cartas);
				//System.out.println(files[cardj].getName());
			}
		}
		//Interface gr�fica
		for(int i=0; i<cartas.size();i++){
			switch (i){
				case 0:
					StringProperty cartap1 = new SimpleStringProperty(cartas.get(0));
					//System.out.println(cartap1);
					//this.carta1.textProperty().bind(cartap1);

					break;
				case 1:
					StringProperty cartap2 = new SimpleStringProperty(cartas.get(1));
					//this.carta2.textProperty().bind(cartap2);

					break;
				case 2:
					StringProperty cartap3 = new SimpleStringProperty(cartas.get(2));
					//this.carta3.textProperty().bind(cartap3);

					break;
				case 3:
					StringProperty cartap4 = new SimpleStringProperty(cartas.get(3));
					//this.carta4.textProperty().bind(cartap4);

					break;
			}

		}

		double xmax=0;
		double xmin = 640;
		double ymax=0;
		double ymin =480;
		int p1 =-1, p2=-1, p3=-1, p4=-1;
		for(int i=0; i< pontos.size();i++){
			if(pontos.get(i).x < xmin){
				xmin = pontos.get(i).x;
				p2 = i;
			}
			if(pontos.get(i).x > xmax){
				xmax = pontos.get(i).x;
				p3 = i;
			}
			if(pontos.get(i).y < ymin){
				ymin = pontos.get(i).y;
				p1 = i;
			}
			if(pontos.get(i).y > ymax){
				ymax = pontos.get(i).y;
				p4 = i;
			}
		}
		if(cartas.size()==4) {
			System.out.println("\nCartas:-----------------------------");
			System.out.println(cartas.get(p1));
			System.out.println(cartas.get(p2));
			System.out.println(cartas.get(p3));
			System.out.println(cartas.get(p4));
			System.out.println("------------------------------------");

			Hearts jogo = new Hearts();

			jogo.addCard(parseCard(cartas.get(p1)).getValue(),parseCard(cartas.get(p1)).getSuit());

			jogo.addCard(parseCard(cartas.get(p2)).getValue(),parseCard(cartas.get(p2)).getSuit());
			jogo.addCard(parseCard(cartas.get(p3)).getValue(),parseCard(cartas.get(p3)).getSuit());
			jogo.addCard(parseCard(cartas.get(p4)).getValue(),parseCard(cartas.get(p4)).getSuit());
			int winner = jogo.getRoundWinner();

			Imgproc.putText(colorimage, parseCard(cartas.get(p1)).getValue()+" "+parseCard(cartas.get(p1)).getSuit(), pontos.get(p1),5,2,new Scalar(255,255,255));
			Imgproc.putText(colorimage, parseCard(cartas.get(p2)).getValue()+" "+parseCard(cartas.get(p2)).getSuit(), pontos.get(p2),5,2,new Scalar(255,255,255));
			Imgproc.putText(colorimage, parseCard(cartas.get(p3)).getValue()+" "+parseCard(cartas.get(p3)).getSuit(), pontos.get(p3),5,2,new Scalar(255,255,255));
			Imgproc.putText(colorimage, parseCard(cartas.get(p4)).getValue()+" "+parseCard(cartas.get(p4)).getSuit(), pontos.get(p4),5,2,new Scalar(255,255,255));

			switch (winner){
				case 0:
					Imgproc.putText(colorimage, parseCard(cartas.get(p1)).getValue()+" "+parseCard(cartas.get(p1)).getSuit(), pontos.get(p1),5,2,new Scalar(0,255,255));
					break;
				case 1:
					Imgproc.putText(colorimage, parseCard(cartas.get(p2)).getValue()+" "+parseCard(cartas.get(p2)).getSuit(), pontos.get(p2),5,2,new Scalar(0,255,255));
					break;
				case 2:
					Imgproc.putText(colorimage, parseCard(cartas.get(p3)).getValue()+" "+parseCard(cartas.get(p3)).getSuit(), pontos.get(p3),5,2,new Scalar(0,255,255));
					break;
				case 3:
					Imgproc.putText(colorimage, parseCard(cartas.get(p4)).getValue()+" "+parseCard(cartas.get(p4)).getSuit(), pontos.get(p4),5,2,new Scalar(0,255,255));
					break;
			}


			Imgcodecs.imwrite("print.png",colorimage);


		}

		Mat dest = new Mat();
		frame.copyTo(dest, colorimage);
		return dest;
	}

	/**
	 * Action triggered when the Canny checkbox is selected
	 *
	 */


	@FXML
	protected void cannySelected()
	{
		// check whether the other checkbox is selected and deselect it
		if (this.dilateErode.isSelected())
		{
			this.dilateErode.setSelected(false);
			this.inverse.setDisable(true);
		}

		// enable the threshold slider
		if (this.canny.isSelected())
			this.threshold.setDisable(false);
		else
			this.threshold.setDisable(true);

		// now the capture can start
		this.cameraButton.setDisable(false);
	}

	/**
	 * Action triggered when the "background removal" checkbox is selected
	 */
	@FXML
	protected void dilateErodeSelected()
	{
		// check whether the canny checkbox is selected, deselect it and disable
		// its slider
		if (this.canny.isSelected())
		{
			this.canny.setSelected(false);
			this.threshold.setDisable(true);
		}

		if (this.dilateErode.isSelected())
			this.inverse.setDisable(false);
		else
			this.inverse.setDisable(true);

		// now the capture can start
		this.cameraButton.setDisable(false);
	}

	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 *
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	private Image mat2Image(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

}
