#!/bin/bash
set -euo pipefail

cd `dirname "$0"`
cd ..

confDir=grails-app/conf
dsFile=$confDir/DataSource.groovy
configFile=$confDir/Config.groovy

# overwrite all the DB connection details, for all envs
sed -i "s+postgresql://.*\"+postgresql://${DBHOST:?}/${DBNAME:?}\"+" $dsFile
sed -i "s/username = \"\w\+\"/username = \"${DBUSER:?}\"/" $dsFile
sed -i "s/password = \"\w\+\"/password = \"${DBPASS:?}\"/" $dsFile

sed -i "s+aekosServiceRoot = \"\w\+\"+aekosServiceRoot = \"${AEKOS_SERVICES_URL:?}\"+" $configFile

# it would be nice if logs we written to stdout, but they aren't :(
# FIXME need to 'forever' the tail processes so they restart if failed, or just get tomcat to write to stdout
outFile=target/tomcat-out.txt
echo '' > $outFile
tail -f $outFile &
errFile=target/tomcat-err.txt
echo '' > $errFile
tail -f $errFile &

grailsEnv=${GRAILS_ENV:-development}
grails -Dgrails.env=$grailsEnv $@
