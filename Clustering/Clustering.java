import java.util.HashMap;
import java.util.LinkedList;

public class Clustering {

  public  LinkedList<HashMap<String, Double>> calculateCentroids (DataSet dataset, int k) {
    LinkedList<HashMap<String, Double>> centroids = new LinkedList<>();
    centroids.add(dataset.randomFromDataSet());
    for (int i = 1; i < k; i++) {
      centroids.add (dataset.calculateCentroid());
    }
    return centroids;
  }

  public void kmeansAlgorithm (DataSet dataset, int k) {
    LinkedList<HashMap<String, Double>> centroids = calculateCentroids (dataset, k);
    Double SSE = Double.MAX_VALUE;
    while (true) {
      LinkedList<Record> records = dataset.getRows();
      for (Record record : records) {
        Double minDist = Double.MAX_VALUE;
        for (int i = 0; i < centroids.size(); i++) {
          Double dist = DataSet.euclideanDistance (centroids.get(i), record.getValues());
          if (dist < minDist) {
            minDist = dist;
            record.setLabel(i);
          }
        }
      }
      centroids = dataset.recomputeCentroids(k);
      Double newSSE = dataset.calculateTotalSSE(centroids);
      if (SSE - newSSE <= 0)
        break;
      else
        SSE = newSSE;
    }
  }

  public static void main(String[] args) throws Exception {
    DataSet dataset = new DataSet("src/iris.csv");
    Clustering clustering = new Clustering();
    clustering.kmeansAlgorithm(dataset, 3);
    dataset.save("src/result.csv");
  }
}
