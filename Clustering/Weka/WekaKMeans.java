import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.clusterers.SimpleKMeans;

public class WekaKMeans {

  public static void main(String[] args) {
    try {
      DataSource src = new DataSource("weather.arff");
      Instances dt = src.getDataSet();
      SimpleKMeans model = new SimpleKMeans();
      model.setNumClusters(3);
      model.buildClusterer(dt);
      System.out.println(model);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
