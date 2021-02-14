import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.evaluation.classification.Evaluation;

import java.io.File;

public class ExampleMNISTWithDL4J {
  
  public static void main(String[] args) throws Exception {

    
    DataSetIterator train = new MnistDataSetIterator(100, 60000, true);
    DataSetIterator test = new MnistDataSetIterator(100, 10000, true);
  
    MultiLayerConfiguration cfg = new NeuralNetConfiguration.Builder()
      .weightInit(WeightInit.UNIFORM)
      .list()
      .layer(0, new DenseLayer.Builder()
        .activation(Activation.SIGMOID)
        .nIn(784)
        .nOut(794)
        .build())
      .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
        .activation(Activation.SIGMOID)
        .nIn(794)
        .nOut(10)
        .build())
      .build();
    
    MultiLayerNetwork model = new MultiLayerNetwork(cfg);
    model.init();
    model.setLearningRate(0.7);
    
    for (int i = 0; i < 100; i++) {
      System.out.println("> " + i);
      model.fit(train);
    }
  
    // Evaluation
    Evaluation eval = new Evaluation(10);
    while(test.hasNext()) {
      DataSet testMnist = test.next();
      INDArray predict2 = model.output(testMnist.getFeatures());
      eval.eval(testMnist.getLabels(), predict2);
    }
    System.out.println (eval.stats());
    
    //save the model
    model.save(new File("./mnist_model"));
  }

}
