/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.plugins.fsm

/**
 * Convention object to configure this plugin from gradle DSL.
 *
 */
class FSMPluginConvention {

	public static final String MODULE_DIR_NAME_CONVENTION = "moduleDirName"
	
	/**
	 * The name of the directory containing the module.xml, relative to the project directory.
	 */
	String moduleDirName = 'src/main/resources'
}
