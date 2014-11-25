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

import org.wso2.carbon.ml.model.exceptions.ModelServiceException;

public class MLModelUtils {
    /**
     * Private constructor to prevent any other class from instantiating.
     */
    private MLModelUtils() {
        //
    }

    static int getResponseIndex(String response, String headerRow,
                                String columnSeparator) throws
                                                        ModelServiceException {
        try {
            int responseIndex = 0;
            String[] headerItems = headerRow.split(columnSeparator);
            for (int i = 0; i < headerItems.length; i++) {
                if (response.equals(headerItems[i])) {
                    responseIndex = i;
                    break;
                }
            }
            return responseIndex;
        } catch (Exception e) {
            throw new ModelServiceException(e.getMessage(), e);
        }
    }

    static String getColumnSeparator(String datasetURL) throws
                                                        ModelServiceException {
        try {
            if (datasetURL.endsWith(MLModelConstants.CSV)) {
                return ",";
            } else if (datasetURL.endsWith(MLModelConstants.TSV)) {
                return "\t";
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new ModelServiceException(e.getMessage(), e);
        }
    }


}