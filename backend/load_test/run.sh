#!/bin/bash
test=$1
echo $1
case $test in
  products)
    jmeter -n -t $PWD/LoadTest_getProducts.jmx -f  -l $PWD/jmeter.log -e -o $PWD/jmeter_dash
    ;;
  orders)
    jmeter -n -t $PWD/LoadTest_getOrders.jmx -f  -l $PWD/jmeter.log -e -o $PWD/jmeter_dash
    ;;
  createorder)
    jmeter -n -t $PWD/LoadTest_createOrder.jmx -f  -l $PWD/jmeter.log -e -o $PWD/jmeter_dash
    ;;
  *)
    echo 'Invalid test name'
    ;;
esac
