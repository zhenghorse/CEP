/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.softleader.siddhi.performance;

import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;

public class SimpleFilterSingleQueryPerformance {
	private static int count = 0;
	private static volatile long start = System.currentTimeMillis();

	public static void main(String[] args) throws InterruptedException {
		SiddhiManager siddhiManager = new SiddhiManager();

		String executionPlan = ""
				+ "@plan:parallel "
				+ "define stream cseEventStream (symbol string, price float, volume long);"
				+ "" + "@info(name = 'query1') "
				+ "from cseEventStream[price > 70] " + "select * "
				+ "insert into outputStream ;";

		ExecutionPlanRuntime executionPlanRuntime = siddhiManager
				.createExecutionPlanRuntime(executionPlan);
		executionPlanRuntime.addCallback("outputStream", new StreamCallback() {
			private long chunk = 0;
			private long prevCount = 0;

			@Override
			public void receive(Event[] events) {
//				Event event = events[0];
//				Object[] data = event.getData();
//				System.out.println(count+":symbol:"+data[0]+",price:"+data[1]+",volume:"+data[2]);
				count += events.length;
				long currentChunk = count / 2000000;
				if (currentChunk != chunk) {
					long end = System.currentTimeMillis();
					double tp = ((count - prevCount) * 1000.0 / (end - start));
//					System.out.println("Throughput = " + tp + " Event/sec");
					System.out.println(tp);
					start = end;
					chunk = currentChunk;
					prevCount = count;
				}
			}
		});

		InputHandler inputHandler = executionPlanRuntime
				.getInputHandler("cseEventStream");
		executionPlanRuntime.start();
		int num = 1;
		while (true) {
			inputHandler.send(new Object[] { "WSO2", 50.6f, 100 });
			inputHandler.send(new Object[] { "IBM", 70.6f, 100 });
			inputHandler.send(new Object[] { "WSO2", 51.6f, 100 });
			inputHandler.send(new Object[] { "IBM", 71.6f, 100 });
			inputHandler.send(new Object[] { "WSO2", 52.6f, 100 });
			inputHandler.send(new Object[] { "IBM", 72.6f, 100 });
			inputHandler.send(new Object[] { "WSO2", 53.6f, 100 });
			inputHandler.send(new Object[] { "IBM", 73.6f, 100 });
			inputHandler.send(new Object[] { "WSO2", 54.6f, 100 });
			inputHandler.send(new Object[] { "IBM", 74.6f, 100 });
//			num++;
//			if (num > 2000) {
//				break;
//			}
		}

	}
}
