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

package org.wso2.carbon.ml.project.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProjectManagementServiceActivator implements BundleActivator {
		private static final Log logger = LogFactory.getLog(ProjectManagementServiceActivator.class);

    /**
     * Creates an instance of ModelService OSGi service
     * @param context OSGi bundle's execution context
     */
		public void start(BundleContext context) {
			ProjectManagementService projectManagementService = new ProjectManagementService();
			context.registerService(ProjectManagementService.class.getName(), projectManagementService, null);
			logger.info("Project Management Service activated.");
            logger.info("ML Wizard URL : http://localhost:9763/mlUI");
        }
		
		public void stop(BundleContext context) {
	        //
        }
}

