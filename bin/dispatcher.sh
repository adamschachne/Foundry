#!/bin/bash

java -cp foundry-dispatcher-1.0-SNAPSHOT-prod.jar org.neuinfo.foundry.jms.producer.OplogMessageDispatcher $*
