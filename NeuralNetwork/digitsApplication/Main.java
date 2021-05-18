package digitsApplication;

import com.twelvemonkeys.image.ResampleOp;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame implements ActionListener {
	
  private DrawArea drawArea;
  private JLabel predictNumber;
  private MultiLayerNetwork model;
  private InputImage labeledImage;
	
  public Main() throws IOException {
    createMenu();
	setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	// draw area
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 0.7; c.weighty = 1;
	c.gridx = 0; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 2;
	JPanel drawPanel = new JPanel(new BorderLayout(1,1));
	labeledImage = new InputImage();
	drawArea = new DrawArea();
	drawPanel.setBorder(
	  BorderFactory.createTitledBorder("Draw here:")
	);
	drawPanel.add(drawArea);
	add(drawPanel, c);
	// captured image
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 0.3; c.weighty = 0.5;
	c.gridx = 1; c.gridy = 0;
	c.gridwidth = 1; c.gridheight = 1;
	JPanel resultPanel = new JPanel(new GridLayout(1,1));
	resultPanel.setBackground(Color.WHITE);
	resultPanel.setBorder(
	  BorderFactory.createTitledBorder("The computer see this: ")
	);
	resultPanel.add(labeledImage);
	add(resultPanel, c);
	// result
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 0.3; c.weighty = 0.5;
	c.gridx = 1; c.gridy = 1;
	c.gridwidth = 2;
	resultPanel = new JPanel(new GridLayout(1,1));
	resultPanel.setBackground(Color.WHITE);
	resultPanel.setBorder(
	  BorderFactory.createTitledBorder("The recognized digit: ")
	);
	predictNumber = new JLabel(" ", SwingConstants.CENTER);
	predictNumber.setForeground(Color.RED);
	predictNumber.setFont(new Font("Courier", Font.BOLD, 48));
	resultPanel.add(predictNumber);
	add(resultPanel, c);
	// load model
	model = MultiLayerNetwork.load(new File("./mnist_model"), false);
  }
	
  private BufferedImage scale (BufferedImage image) {
    ResampleOp resizeOp = new ResampleOp(28, 28);
	BufferedImage filter = resizeOp.filter(image, null);
	return filter;
  }
	
  private BufferedImage toBufferedImage (Image image) {
	BufferedImage bimage = new BufferedImage(
	  image.getWidth(null), 
      image.getHeight(null), 
      BufferedImage.TYPE_INT_ARGB
	);
	Graphics2D bGr = bimage.createGraphics();
	bGr.drawImage(image, 0, 0, null);
	bGr.dispose();
	return bimage;
  }
	
  private double[][] imageToVector(BufferedImage image) {
    double[][] imageGray = new double[1][28 * 28];
	int w = image.getWidth();
	int h = image.getHeight();
	int index = 0;
	for (int i = 0; i < w; i++) {
	  for (int j = 0; j < h; j++) {
		Color color = new Color(image.getRGB(j, i), true);
		int red = (color.getRed());
		int green = (color.getGreen());
		int blue = (color.getBlue());
		double v = 255 - (red + green + blue) / 3d;
		imageGray[0][index] = v;
		index++;
	  }
    }
	return imageGray;
  }
	
  private void createMenu () {
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("Project");
	JMenuItem run = new JMenuItem("Run");
	JMenuItem clear = new JMenuItem("Clear");
	menu.add(run);
	menu.add(clear);
	menuBar.add(menu);
	run.addActionListener(this);
	clear.addActionListener(this);
	setJMenuBar(menuBar);
  }
	
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Run")) {
	  Image drawImage = drawArea.getImage();
	  BufferedImage sbi = toBufferedImage(drawImage);
	  Image scaled = scale(sbi);
	  BufferedImage scaledBuffered = toBufferedImage(scaled);
	  double[][] scaledPixels = imageToVector(scaledBuffered);
	  labeledImage.set(0, scaledPixels);
	  int[] predict = model.predict(labeledImage.getFeatures());
	  predictNumber.setText("" + predict[0]);
	  labeledImage.repaint();
    } else {
	  drawArea.setImage(null);
	  drawArea.repaint();
	  predictNumber.setText("");
	}
  }
	
  public static void main(String[] args) throws Exception {
	Main main = new Main();
	main.setTitle("Digit Recognizer Application");
	main.setSize(800, 600);
	main.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	main.setVisible(true);
  }
	
}
