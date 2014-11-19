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
package org.wso2.carbon.ml.admin;

public class DataUploadSettings {
	
	private final String uploadLocation;
	private final int inMemoryThreshold;
	private final long uploadLimit;
	
	public DataUploadSettings(String uploadLocation, int inMemoryThreshold,
                              long uploadLimit) {
		this.uploadLocation = uploadLocation;
		this.inMemoryThreshold = inMemoryThreshold;
		this.uploadLimit = uploadLimit;
	}

	public String getUploadLocation() {
		return this.uploadLocation;
	}

	public int getInMemoryThreshold() {
		return this.inMemoryThreshold;
	}

	public long getUploadLimit() {
		return this.uploadLimit;
	}
}