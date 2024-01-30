package com.ratan.main.processor;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratan.main.utils.TaskStatus;

public class ConvertJsonToTaskStatus implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Hello From First Processor");
		String body = exchange.getIn().getBody(String.class);
		ArrayList<String> allTask = new ArrayList<String>(Arrays.asList(body.split("},")));
		int numberOfTask = allTask.size();
		for (int i = 0; i < numberOfTask; i++) {
			if (i == 0) {
				String updatedTask = (allTask.get(i) + "}").substring(1);
				allTask.set(i, updatedTask);

			} else if (i == numberOfTask - 1) {
				String updatedTask = allTask.get(i).substring(0, allTask.get(i).length() - 1);
				allTask.set(i, updatedTask);
			} else {
				String updatedTask = allTask.get(i) + "}";
				allTask.set(i, updatedTask);
			}
		}
		
		ArrayList<TaskStatus> allTaskObjects = new ArrayList<TaskStatus>();
		ObjectMapper mapper = new ObjectMapper();
		
		for(int i=0;i<numberOfTask;i++) {
			TaskStatus task = mapper.readValue(allTask.get(i), TaskStatus.class);
			allTaskObjects.add(task);
		}
		exchange.getIn().setBody(allTaskObjects);
	}

}
