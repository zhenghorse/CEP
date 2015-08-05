package com.softleader.esper.performance;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class SimpleFilterSingleQueryPerformanceEsper {
	private static int count = 0;
	private static volatile long start = System.currentTimeMillis();

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager
				.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		admin.createEPL("create objectarray schema cseEventStream (symbol string, price float, volume long)");
		String epl = "select symbol, price, volume from cseEventStream(price > 70)";
		EPStatement state = admin.createEPL(epl);
		state.addListener(new UpdateListener() {
			private long chunk = 0;
			private long prevCount = 0;

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
//				if (newEvents == null) {
//					System.err.println("没有事件也进来了！");
//				}
//				EventBean event = newEvents[0];
//				System.out.println(count+":symbol:"+event.get("symbol")+",price:"+event.get("price")+",volume:"+event.get("volume"));
				count += newEvents.length;
				long currentChunk = count / 2000000;
				//System.out.println("事件进来了！" + count);
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

		int num = 1;
		while (true ) {
			runtime.sendEvent(new Object[] { "WSO2", 50.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "IBM", 70.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "WSO2", 51.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "IBM", 71.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "WSO2", 52.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "IBM", 72.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "WSO2", 53.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "IBM", 73.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "WSO2", 54.6f, 100 },
					"cseEventStream");
			runtime.sendEvent(new Object[] { "IBM", 74.6f, 100 },
					"cseEventStream");
//			num ++;
//			if (num > 100) {
//				break;
//			}
		}

	}
}
