import java.io.IOException;
import java.util.*;

public class NaiveBayes {

    private final Reader train;
    private final Reader test;
    private final List<List<Map<String, Integer>>> probabilities;
    private Map<String, Integer> truePositives;
    private Map<String, Integer> falsePositives;
    private Map<String, Integer> falseNegatives;
    private Map<String, Integer> trueNegatives;

    public NaiveBayes(String train, String test) throws IOException {
        this.train = new Reader(train);
        this.test = new Reader(test);
        this.probabilities = new ArrayList<>();
        this.truePositives = new HashMap<>();
        this.falsePositives = new HashMap<>();
        this.falseNegatives = new HashMap<>();
        this.trueNegatives = new HashMap<>();
    }

    public void train() {
        for (String class_ : train.getClasses()) {
            List<Map<String, Integer>> list = new ArrayList<>();
            for (int i = 0; i < train.getContent().get(0).length; i++) {
                Map<String, Integer> map = new HashMap<>();
                for (int j = 0; j < train.getContent().size(); j++) {
                    if (train.getContent().get(j)[0].equals(class_)) {
                        String curr = train.getContent().get(j)[i];
                        if (!map.containsKey(curr)) {
                            map.put(curr, 1);
                        } else {
                            map.put(curr, map.get(curr) + 1);
                        }
                    }
                }
                list.add(map);
            }
            probabilities.add(list);
        }
    }

    public String predict(List<String> list) {
        List<Double> results = new ArrayList<>();
        for (int j = 0; j < probabilities.size(); j++) {
            double result = 1;
            List<Map<String, Integer>> mapList = probabilities.get(j);
            result *= mapList.get(0).get(train.getClasses().get(j)) / (double) train.getContent().size();
            for (int i = 0; i < list.size(); i++) {
                if (mapList.get(i + 1).containsKey(list.get(i))) {
                    result *= mapList.get(i + 1).get(list.get(i)) / (double) mapList.get(0).get(train.getClasses().get(j));
                } else {
                    double count = 0;
                    for (int k = 0; k < probabilities.size(); k++) {
                        if (k != j) {
                            if (probabilities.get(k).get(i + 1).containsKey(list.get(i))) count += probabilities.get(k).get(i + 1).get(list.get(i));
                        }
                    }
                    result *= (1 / ((double) mapList.get(0).get(train.getClasses().get(j)) + count));
                }
            }
            results.add(result);
        }
        double maxResult = results.get(0);
        int maxIndex = 0;
        for (int i = 1; i < results.size(); i++) {
            if (results.get(i) > maxResult) {
                maxResult = results.get(i);
                maxIndex = i;
            }
        }
        return train.getClasses().get(maxIndex);
    }

    public String classify() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter characteristics separated by commas:");
        String[] characteristics = scanner.nextLine().split(",");
        return predict(Arrays.asList(characteristics));
    }

    public void test() {
        initializeMetrics();
        for (String[] line : test.getContent()) {
            String actualClass = line[0];
            String predictedClass = predict(Arrays.asList(line).subList(1, line.length));
            if (predictedClass.equals(actualClass)) {
                truePositives.put(actualClass, truePositives.get(actualClass) + 1);
                trueNegatives.put(actualClass, trueNegatives.get(actualClass) - 1);
            } else {
                falsePositives.put(predictedClass, falsePositives.get(predictedClass) + 1);
                falseNegatives.put(actualClass, falseNegatives.get(actualClass) + 1);
            }
        }
    }

    private void initializeMetrics() {
        for (String class_ : train.getClasses()) {
            truePositives.put(class_, 0);
            falsePositives.put(class_, 0);
            falseNegatives.put(class_, 0);
            trueNegatives.put(class_, test.getContent().size());
        }
    }

    public double getAccuracy() {
        double correct = 0;
        for (String class_ : train.getClasses()) {
            correct += truePositives.get(class_);
        }
        return correct / test.getContent().size();
    }

    public double getPrecision(String class_) {
        int tp = truePositives.get(class_);
        int fp = falsePositives.get(class_);
        return tp / (double) (tp + fp);
    }

    public double getRecall(String class_) {
        int tp = truePositives.get(class_);
        int fn = falseNegatives.get(class_);
        return tp / (double) (tp + fn);
    }

    public double getFMeasure(String class_) {
        double precision = getPrecision(class_);
        double recall = getRecall(class_);
        return 2 * (precision * recall) / (precision + recall);
    }

    public void getAccuracyAndSomeOtherStuff() {
        for (String class_ : train.getClasses()) {
            System.out.println("Class: " + class_);
            System.out.println("Precision: " + getPrecision(class_));
            System.out.println("Recall: " + getRecall(class_));
            System.out.println("F-Measure: " + getFMeasure(class_));
            System.out.println();
        }
        System.out.println("Accuracy: " + getAccuracy() * 100 + "%");
    }
}
