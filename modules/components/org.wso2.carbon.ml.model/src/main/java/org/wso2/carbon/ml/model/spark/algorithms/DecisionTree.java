/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.ml.model.spark.algorithms;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.wso2.carbon.ml.model.spark.dto.ClassClassificationModelSummary;
import org.wso2.carbon.ml.model.spark.dto.PredictedVsActual;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DecisionTree implements Serializable {

    /**
     * @param train               Training dataset as a JavaRDD of labeled points
     * @param noOfClasses         No of classes
     * @param categoricalFeatures Categorical features
     * @param impurityCriteria    Impurity criteria
     * @param maxTreeDepth        Maximum tree depth
     * @param maxBins             Maximum no of bins
     * @return Decision tree model
     */
    public DecisionTreeModel train(JavaRDD<LabeledPoint> train, int noOfClasses,
            Map<Integer, Integer> categoricalFeatures, String impurityCriteria, int maxTreeDepth,
            int maxBins) {
        return org.apache.spark.mllib.tree.DecisionTree.trainClassifier(train, noOfClasses,
                categoricalFeatures, impurityCriteria, maxTreeDepth, maxBins);
    }

    /**
     * @param model Decision tree model
     * @param test  Test dataset as a JavaRDD of labeled points
     * @return JavaPairRDD containing predictions and labels
     */
    public JavaPairRDD<Double, Double> test(final DecisionTreeModel model, JavaRDD<LabeledPoint>
            test) {
        return test.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
            @Override
            public Tuple2<Double, Double> call(LabeledPoint p) {
                return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
            }
        });

    }

    /**
     * @param predictionsAndLabels Predicted labels and actual labels
     * @return Class classification model summary
     */
    public ClassClassificationModelSummary getClassClassificationModelSummary(
            JavaPairRDD<Double, Double> predictionsAndLabels) {
        ClassClassificationModelSummary classClassificationModelSummary = new
                ClassClassificationModelSummary();
        List<PredictedVsActual> predictedVsActuals = new ArrayList();
        List<Tuple2<Double, Double>> scoresAndLabels = predictionsAndLabels.collect();
        for (Tuple2<Double, Double> scoreAndLabel : scoresAndLabels) {
            PredictedVsActual predictedVsActual = new PredictedVsActual();
            predictedVsActual.setPredicted(scoreAndLabel._1());
            predictedVsActual.setActual(scoreAndLabel._2());
            predictedVsActuals.add(predictedVsActual);
        }
        classClassificationModelSummary.setPredictedVsActuals(predictedVsActuals);
        double error = 1.0 * predictionsAndLabels.filter(new Function<Tuple2<Double, Double>,
                Boolean>() {
            @Override
            public Boolean call(Tuple2<Double, Double> pl) {
                return !pl._1().equals(pl._2());
            }
        }).count() / predictionsAndLabels.count();
        classClassificationModelSummary.setError(error);
        return classClassificationModelSummary;
    }
}
