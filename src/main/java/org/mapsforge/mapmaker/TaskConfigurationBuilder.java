/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.mapmaker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;

class TaskConfigurationBuilder {
	private LinkedList<TaskConfiguration> taskConfigurations;
	private static final Map<String, String> EMPTY_PIPE_ARGS = new HashMap<String, String>();
	
	static enum TaskType {
		READ_BINARY, READ_XML, MAP_WRITER, POI_WRITER;
	}

	TaskConfigurationBuilder() {
		this.taskConfigurations = new LinkedList<TaskConfiguration>();
	}
	
	List<TaskConfiguration> getTaskConfigurations() {
		return this.taskConfigurations;
	}
	
	void createAndAddTask(TaskType taskType, String... params) {
		// First task must be a source task
		if(taskConfigurations.size() == 0) {
			assert(taskType == TaskType.READ_BINARY || taskType == TaskType.READ_XML);
		}
		
		// More than one sink task --> add tee task
		if(this.taskConfigurations.size() == 2) {
			createAndInsertTeeTask();
		}
		
		String type = taskTypeToOsmosisString(taskType);
		String id = this.taskConfigurations.size() + "-" + type;
		Map<String, String> configArgs = new HashMap<String, String>();
		String defaultArg = null;
		
		String tag[];
		for(String p : params) {
			tag = p.split("=", 2);
			if(tag.length == 1 && defaultArg == null) {
				defaultArg = p;
			} 
			if(tag.length == 2) {
				configArgs.put(tag[0], tag[1]);
			}
		}
		
		TaskConfiguration config = new TaskConfiguration(id, type, EMPTY_PIPE_ARGS, configArgs, defaultArg);
		this.taskConfigurations.add(config);
	}
	
	private void createAndInsertTeeTask() {
		this.taskConfigurations.add(1, new TaskConfiguration("2-tee", "tee", EMPTY_PIPE_ARGS, EMPTY_PIPE_ARGS, null));
	}
	
	private static String taskTypeToOsmosisString(TaskType taskType) {
		switch(taskType) {
		case READ_BINARY:
			return "rb";
		case READ_XML:
			return "rx";
		case MAP_WRITER:
			return "mw";
		case POI_WRITER:
			return "write-poi";
		default:
			return "";
		}
	}
}
