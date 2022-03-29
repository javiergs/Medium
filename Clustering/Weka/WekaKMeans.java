import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaKMeans {

  public static void main(String[] args) {
    try {
      // clustering
      DataSource src = new DataSource("weather.arff");
      Instances dt = src.getDataSet();
      SimpleKMeans model = new SimpleKMeans();
      model.setNumClusters(3);
      model.setPreserveInstancesOrder(true);
      model.buildClusterer(dt);
      // show labels per record
      int[] assignments = model.getAssignments();
      int i = 0;
      for (int clusterNum : assignments) {
        System.out.printf("Instance %d [%s] -> Cluster %d \n", i, dt.get(i), clusterNum);
        i++;
      }
      // evaluation
      ClusterEvaluation eval = new ClusterEvaluation();
      eval.setClusterer(model);
      eval.evaluateClusterer(dt); // yes, we need to use a test dataset here :)
      System.out.println(eval.clusterResultsToString());
      System.out.println(eval.getLogLikelihood());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}

