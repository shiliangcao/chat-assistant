#!/bin/sh

export JVM_OPTIONS="\
    -Xmx512m -XX:+UseG1GC \
    -Dlogging.config=logback-prod.xml"