import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class DataSet {

  private final LinkedList<String> columns = new LinkedList<>();
  private final LinkedList<Record> rows = new LinkedList<>();
  private final LinkedList<Integer> centroidIndexes = new LinkedList<>();
  private final HashMap<String, Double> minimums = new HashMap<>();
  private final HashMap<String, Double> maximums = new HashMap<>();
  private final Random random = new Random();

  public DataSet (String csvFileName) throws IOException {
    String row;
    BufferedReader csvReader = new BufferedReader(new FileReader(csvFileName));
    if ((row = csvReader.readLine()) != null) {
      String[] data = row.split(",");
      Collections.addAll(columns, data);
    }
    while ((row = csvReader.readLine()) != null) {
      String[] data = row.split(",");
      HashMap<String, Double> record = new HashMap<>();
      if (columns.size() == data.length) {
        for (int i = 0; i < columns.size(); i++) {
          String name = columns.get(i);
          double val = Double.parseDouble(data[i]);
          record.put(name, val);
          updateMin(name, val);
          updateMax(name, val);
        }
      } else {
        throw new IOException("This is not a correct file");
      }
      rows.add(new Record(record));
    }
  }

  public void save(String outputFileName) {
    try {
      BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFileName));
      for (int i = 0; i < columns.size(); i++) {
        csvWriter.write(columns.get(i));
        csvWriter.write(",");
      }
      csvWriter.write("Label");
      csvWriter.write("\n");
      for (Record record : rows) {
        for (int i = 0; i < columns.size(); i++) {
          csvWriter.write(String.valueOf(record.getValues().get(columns.get(i))));
          csvWriter.write(",");
        }
        csvWriter.write(String.valueOf(record.getLabel()));
        csvWriter.write("\n");
      }
      csvWriter.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private void updateMin (String name, Double val) {
    if (minimums.containsKey(name)) {
      if (val < minimums.get(name)) {
        minimums.put(name, val);
      }
    } else {
      minimums.put(name, val);
    }
  }

  private void updateMax(String name, Double val) {
    if (maximums.containsKey(name)) {
      if (val > maximums.get(name)) {
        maximums.put(name, val);
      }
    } else {
      maximums.put(name, val);
    }
  }

  private Double meanOfAttr(String attrName, LinkedList<Integer> indices) {
    Double sum = 0.0;
    for (int i : indices)
      if (i < rows.size())
        sum += rows.get(i).getValues().get(attrName);
    return sum / indices.size();
  }

  public HashMap<String, Double> calculateCentroid (int clusterNo) {
    HashMap<String, Double> centroid = new HashMap<>();
    LinkedList<Integer> recsInCluster = new LinkedList<>();
    for (int i = 0; i < rows.size(); i++) {
      Record record = rows.get(i);
      if (record.getLabel() == clusterNo) {
        recsInCluster.add(i);
      }
    }
    for (String name : columns) {
      centroid.put(name, meanOfAttr(name, recsInCluster));
    }
    return centroid;
  }

  public LinkedList<HashMap<String, Double>> recomputeCentroids(int K) {
    LinkedList<HashMap<String, Double>> centroids = new LinkedList<>();
    for (int i = 0; i < K; i++) {
      centroids.add(calculateCentroid(i));
    }
    return centroids;
  }

  public HashMap<String, Double> randomFromDataSet() {
    int index = random.nextInt(rows.size());
    return rows.get(index).getValues();
  }

  public static Double euclideanDistance(HashMap<String, Double> a, HashMap<String, Double> b) {
    if (!a.keySet().equals(b.keySet())) {
      return Double.POSITIVE_INFINITY;
    }
    double sum = 0.0;
    for (String attrName : a.keySet()) {
      sum += Math.pow(a.get(attrName) - b.get(attrName), 2);
    }
    return Math.sqrt(sum);
  }

  private Double calculateClusterSSE(HashMap<String, Double> centroid, int clusterNo) {
    double SSE = 0.0;
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).getLabel() == clusterNo) {
        SSE += Math.pow(euclideanDistance(centroid, rows.get(i).getValues()), 2);
      }
    }
    return SSE;
  }

  public Double calculateTotalSSE(LinkedList<HashMap<String, Double>> centroids) {
    Double SSE = 0.0;
    for (int i = 0; i < centroids.size(); i++) {
      SSE += calculateClusterSSE(centroids.get(i), i);
    }
    return SSE;
  }

  public HashMap<String, Double> calculateCentroid() {
    double sum = 0.0;
    for (int i = 0; i < rows.size(); i++) {
      if (!centroidIndexes.contains(i)) {
        double minDist = Double.MAX_VALUE;
        for (int ind : centroidIndexes) {
          double dist = euclideanDistance(rows.get(i).getValues(), rows.get(ind).getValues());
          if (dist < minDist)
            minDist = dist;
        }
        if (centroidIndexes.isEmpty())
          sum = 0.0;
        sum += minDist;
      }
    }
    double threshold = sum * random.nextDouble();
    for (int i = 0; i < rows.size(); i++) {
      if (!centroidIndexes.contains(i)) {
        double minDist = Double.MAX_VALUE;
        for (int ind : centroidIndexes) {
          double dist = euclideanDistance(rows.get(i).getValues(), rows.get(ind).getValues());
          if (dist < minDist)
            minDist = dist;
        }
        sum += minDist;
        if (sum > threshold) {
          centroidIndexes.add(i);
          return rows.get(i).getValues();
        }
      }
    }
    return new HashMap<>();
  }

  public LinkedList<Record> getRows() {
    return rows;
  }

}
