#!/usr/bin/env bash

MEM_PARAMS="-Xmx1500m -Xms1500m"
MAIN_JAR="mapsforge-map-maker.jar"
OSMOSIS_LIBS="lib/osmosis-pbf-0.40.1.jar:lib/osmosis-xml-0.40.1.jar:lib/osmpbf-1.1.1-754a33af.jar"

java $MEM_PARAMS -cp $MAIN_JAR:$OSMOSIS_LIBS org.mapsforge.mapmaker.GUIOsmosisLauncher
