/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.ml.model;

public class SQLQueries {

    public static final String GET_ALGORITHMS_BY_TYPE = "SELECT NAME FROM ML_ALGORITHM WHERE " +
                                                        "TYPE=?";

    public static final String GET_HYPER_PARAMETERS = "SELECT HYPERPARAMETERS FROM " +
                                                      "ML_ALGORITHM WHERE " +
                                                      "NAME=?";

    public static final String GET_ALGORITHM_RATINGS = "SELECT NAME,INTERPRETABILITY,SCALABILITY," +
                                                      "DIMENSIONALITY FROM ML_ALGORITHM WHERE " +
                                                      "TYPE=?";

    public static final String INSERT_EXECUTION =
            "INSERT INTO ML_EXECUTION(ID,WORKFLOW" +
            ") VALUES(?,?)";

    public static final String INSERT_MODEL =
            "INSERT INTO ML_MODEL(ID,EXECUTION,MODEL,MODEL_SUMMARY" +
            ") VALUES(?,?,?,?)";
}
