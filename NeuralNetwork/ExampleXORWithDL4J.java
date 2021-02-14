import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.evaluation.classification.Evaluation;

public class ExampleXORWithDL4J {
    
  public static void main(String[] args) throws Exception {
  
    // data set
    INDArray input = Nd4j.zeros(4, 2);
    input.putScalar(new int[]{0, 0}, 0);
    input.putScalar(new int[]{0, 1}, 0);
    input.putScalar(new int[]{1, 0}, 0);
    input.putScalar(new int[]{1, 1}, 1);
    input.putScalar(new int[]{2, 0}, 1);
    input.putScalar(new int[]{2, 1}, 0);
    input.putScalar(new int[]{3, 0}, 1);
    input.putScalar(new int[]{3, 1}, 1);
  
    INDArray knownOutput = Nd4j.zeros(4, 1);
    knownOutput.putScalar(new int[]{0}, 0);
    knownOutput.putScalar(new int[]{1}, 1);
    knownOutput.putScalar(new int[]{2}, 1);
    knownOutput.putScalar(new int[]{3}, 0);
  
    DataSet dataSet = new DataSet(input, knownOutput);
  
    // network
    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
        .weightInit(WeightInit.UNIFORM)
        .list()
        .layer(0, new DenseLayer.Builder()
          .activation(Activation.SIGMOID)
          .nIn(2)
          .nOut(3)
          .build())
        .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
          .activation(Activation.SIGMOID)
          .nIn(3)
          .nOut(1)
          .build())
        .build();
      
    MultiLayerNetwork network = new MultiLayerNetwork(conf);
    network.init();
    network.setLearningRate(0.7);
     
    // This is our network
    System.out.println(network.summary());
  
    // Training time
    for (int i = 0; i < 10000; i++)
      network.fit(dataSet);      
      
    // Evaluation
    Evaluation eval = new Evaluation();
    INDArray output = network.output(input);
    eval.eval(knownOutput, output);
    System.out.println(eval.stats());      
  }

}
