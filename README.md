# CEP学习
__________________
### Esper学习
- [官网资料](http://www.espertech.com/esper/)
- [csdn luonanqin对Esper的专栏介绍](http://blog.csdn.net/luonanqin/article/details/21300263)

### WSO2 CEP学习
- [WSO2 CEP](http://wso2.com/products/complex-event-processor)

### Drools
- [Drools官网](http://drools.org/)
- [docker  drools-workbench-showcase](https://registry.hub.docker.com/u/jboss/drools-workbench-showcase/)

### CEP比较
- [WSO2 CEP(Siddhi与Esper对比信息)](http://srinathsview.blogspot.com/2011/12/siddhi-second-look-at-complex-event.html)
- [WSO2 CEP与Esper对比信息](http://wso2.com/library/blog-post/2013/08/cep-performance-processing-100k-to-millions-of-events-per-second-using-wso2-complex-event/)
- [CEP Tooling Market Survey 2014(CEP工具情况)](http://www.complexevents.com/2014/12/03/cep-tooling-market-survey-2014/)

### Stream与CEP结合
- [Spark Streaming与Siddhi结合处理流式数据](https://github.com/Stratio/streaming-cep-engine)
- [Storm与Esper结合](https://github.com/tomdz/storm-esper)

### Esper的不足
- 没有事件定义设计器

### Esper与WSO2CEP性能比较
- window10 X64        测试平台配置：I5-5200U CPU/12G MM/256G SSD
- ubuntu12.04 LTS X64 测试平台配置：I5-4590  CPU/4G MM/500G 机械硬盘
- Esper吞吐量大概在200W/sec左右
- WSO2CEP吞吐量大概在600W/sec左右，但在windows平台中测试出来只有15W/sec，同样的代码，不知为何数据会差这么多
