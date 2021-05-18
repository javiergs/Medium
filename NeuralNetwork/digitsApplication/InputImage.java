package digitsApplication;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import javax.swing.*;
import java.awt.*;

public class InputImage extends JPanel {
	
  private double[][] pixels;
  private double label;
  private INDArray features;
	
  private double[][] meanNormalizeFeatures (double[][] pixels) {
    double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;
	double sum = 0;
	for (double pixel : pixels[0]) {
	  sum = sum + pixel;
	  if (pixel > max)  max = pixel;
	  if (pixel < min)  min = pixel;
	}
	double[][] pixelsNorm = 
      new double[pixels.length][pixels[0].length];
    for (int i = 0; i < pixels[0].length; i++)
	  pixelsNorm[0][i] = ((pixels[0][i] - min) / max);	
	return pixelsNorm;
  }
	
  public INDArray getFeatures() {
    return features;
  }
	
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
	if (features == null) {
	  double[][] x = new double[1][28 * 28];
	  for (int i = 0; i < 28; i++)
	    for (int j = 0; j < 28; j++)
		  x [0] [j * 28 + i] = 0.0;
		  features = Nd4j.create(meanNormalizeFeatures(x));
	}
	int size = getHeight() < getWidth() ? getWidth() : getHeight();
	for (int i = 0; i < 28; i++) {
	  for (int j = 0; j < 28; j++) {
	    g.setColor (
          new Color(0, 0, features.getFloat(0, j * 28 + i))
        );
		g.fillRect (i * size / 28, j * size / 28, 10, 10);
	  }
	}
  }
	
  public void set(int label, double[][] pixels) {
    this.label = label;
    this.pixels = pixels;
    features = Nd4j.create(meanNormalizeFeatures(pixels));
  }

}
