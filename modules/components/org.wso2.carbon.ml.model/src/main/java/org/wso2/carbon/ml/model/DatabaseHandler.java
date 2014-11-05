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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHandler {

    private static volatile DatabaseHandler databaseHandler = null;
    private Connection connection;
    private static final Log logger = LogFactory.getLog(DatabaseHandler.class);

    /*
     * private Constructor to prevent any other class from instantiating.
     */
    private DatabaseHandler() {
    }

    /**
     * Creates a singleton DatabaseHandler instance and returns it.
     *
     * @return DatabaseHandler instance
     * @throws DatabaseHandlerException
     */
    public static DatabaseHandler getDatabaseHandler() throws DatabaseHandlerException {
        try {
            if (databaseHandler == null) {
                synchronized (DatabaseHandler.class) {
                    if (databaseHandler == null) {
                        databaseHandler = new DatabaseHandler();
                        // load the carbon data source configurations of the H2
                        // database
                        Context initContext = new InitialContext();
                        DataSource ds = (DataSource) initContext.lookup("jdbc/WSO2ML_DB");
                        databaseHandler.connection = ds.getConnection();
                        // enable auto commit
                        databaseHandler.connection.setAutoCommit(true);
                    }
                }
            }
            return databaseHandler;
        } catch (Exception e) {
            String msg = "Error occured while connecting to database. " + e.getMessage();
            logger.error(msg, e);
            throw new DatabaseHandlerException(msg);
        }
    }

    /**
     *
     * @param algorithm Name of the machine learning algorithm
     * @return Json object containing hyper parameters
     * @throws DatabaseHandlerException
     */
    public JSONObject getHyperParameters(String algorithm) throws DatabaseHandlerException {
        JSONObject parameters = null;
        ResultSet result = null;
        PreparedStatement getStatement = null;
        try {
            getStatement = connection.prepareStatement(SQLQueries.GET_HYPER_PARAMETERS);
            getStatement.setString(1, algorithm);
            result = getStatement.executeQuery();
            if (result.first() && result.getString(1) != null) {
                parameters = new JSONObject(result.getString(1));
            }
        } catch (SQLException e) {
            String msg = "Error occured while getting hyper parameters of algorithm: " + algorithm + ".\n" + e.getMessage();
            logger.error(msg, e);
            throw new DatabaseHandlerException(msg);
        } finally {
            // close the database resources
            MLDatabaseUtil.closeResultSet(result);
            MLDatabaseUtil.closeStatement(getStatement);
        }
        return parameters;
    }

    /**
     *
     * @param algorithmType Type of the machine learning algorithm - e.g. Classification
     * @return Array of algorithm names
     * @throws DatabaseHandlerException
     */
    public String[] getAlgorithms(String algorithmType) throws DatabaseHandlerException {
        List<String> algorithms = new ArrayList<String>();
        ResultSet result = null;
        PreparedStatement getStatement = null;
        try {
            getStatement = connection.prepareStatement(SQLQueries.GET_ALGORITHMS_BY_TYPE);
            getStatement.setString(1, algorithmType);
            result = getStatement.executeQuery();
            while (result.next()) {
                algorithms.add(result.getString(1));
            }
        } catch (SQLException e) {
            String msg = "Error occured while getting algorithm names.\n" + e.getMessage();
            logger.error(msg, e);
            throw new DatabaseHandlerException(msg);
        } finally {
            // close the database resources
            MLDatabaseUtil.closeResultSet(result);
            MLDatabaseUtil.closeStatement(getStatement);
        }
        return algorithms.toArray(new String[algorithms.size()]);
    }

    /**
     *
     * @param algorithmType Type of the machine learning algorithm - e.g. Classification
     * @return Map containing algorithm names and recommendation scores
     * @throws DatabaseHandlerException
     */
    public Map<String,List<Integer>> getAlgorithmRatings(String algorithmType) throws
                                                                    DatabaseHandlerException {
        Map<String,List<Integer>> algorithmRatings = new HashMap<String, List<Integer>>();
        ResultSet result = null;
        PreparedStatement getStatement = null;
        try {
            getStatement = connection.prepareStatement(SQLQueries.GET_ALGORITHM_RATINGS);
            getStatement.setString(1, algorithmType);
            result = getStatement.executeQuery();
            List<Integer> value;
            while (result.next()) {
                value = new ArrayList();
                value.add(result.getInt(2));
                value.add(result.getInt(3));
                value.add(result.getInt(4));
                algorithmRatings.put(result.getString(1),value);
            }
        } catch (SQLException e) {
            String msg = "Error occured while getting algorithm names.\n" + e.getMessage();
            logger.error(msg, e);
            throw new DatabaseHandlerException(msg);
        } finally {
            // close the database resources
            MLDatabaseUtil.closeResultSet(result);
            MLDatabaseUtil.closeStatement(getStatement);
        }
        return algorithmRatings;
    }
}
