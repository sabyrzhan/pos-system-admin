# POS System backend
## Load test
Load tests are done using JMeter 5. All load tests use Throughput Shaping Timer plugin. To easily install use JMeter Plugin Manager and install through it.

Recommended to run in non-gui mode, no to load system with GUI visualization:
```
jmeter -n -t $PWD/LoadTest_getProducts.jmx -f  -l $PWD/jmeter.log -e -o $PWD/jmeter_dash
```

## Grafana dashbaord
Import json dashboard in Grafana. Backend sends all metrics to Prometheus.