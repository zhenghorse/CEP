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
package com.softleader.esper.performance;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class SimpleFilterMultipleQueryPerformanceEsper {
    private static int count = 0;
    private static volatile long start = System.currentTimeMillis();

    public static void main(String[] args) throws InterruptedException {
    	EPServiceProvider epService = EPServiceProviderManager
				.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		admin.createEPL("create objectarray schema cseEventStream (symbol string, price float, volume long)");
		admin.createEPL("create objectarray schema cseEventStream2(symbol string, price float, volume long)");
		String epl = "select symbol, price, volume from cseEventStream(price < 70);" 
				+"select symbol, price, volume from cseEventStream(volume > 90);";
		
        EPStatement state = admin.createEPL(epl);
		state.addListener(new UpdateListener() {
            private long chunk = 0;
            private long prevCount = 0;

            @Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                count += newEvents.length;
                long currentChunk = count / 2000000;
                if (currentChunk != chunk) {
                    long end = System.currentTimeMillis();
                    double tp = ((count - prevCount) * 1000.0 / (end - start));
                    System.out.println("Throughput = " + tp + " Event/sec");
                    start = end;
                    chunk = currentChunk;
                    prevCount = count;
                }
            }

        });

		int num = 1;
        while (true) {
            runtime.sendEvent(new Object[]{"WSO2", 55.6f, 100});
            runtime.sendEvent(new Object[]{"IBM", 75.6f, 100});
            runtime.sendEvent(new Object[]{"WSO2", 100f, 80});
            runtime.sendEvent(new Object[]{"IBM", 75.6f, 100});
            runtime.sendEvent(new Object[]{"WSO2", 55.6f, 100});
            runtime.sendEvent(new Object[]{"IBM", 75.6f, 100});
            runtime.sendEvent(new Object[]{"WSO2", 100f, 80});
            runtime.sendEvent(new Object[]{"IBM", 75.6f, 100});
            num ++;
			if (num > 100) {
				break;
			}
        }
    }
}
