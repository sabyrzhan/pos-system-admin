#!/bin/bash
jmeter -n -t $PWD/LoadTest_getProducts.jmx -f  -l $PWD/jmeter.log -e -o $PWD/jmeter_dash
