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

# config (managed in SettingService.groovy) is stored in the DB, you're just setting the *defaults* here
# you can configure all settings in the web UI by accessing http://localhost:8080/ala-soils2sat/admin/settings as an
# admin
sed -i "s+aekosServiceRoot = \"\w\+\"+aekosServiceRoot = \"${DEFAULT_AEKOS_SERVICES_URL:?}\"+" $configFile
sed -i "s+doiS2SRoot = \"\w\+\"+doiS2SRoot = \"${DEFAULT_DOI_S2S_ROOT_URL:?}\"+" $configFile

# see https://grails.github.io/grails2-doc/2.2.1/ref/Command%20Line/run-war.html
cat <<EOF >> $confDir/BuildConfig.groovy
grails.tomcat.jvmArgs= ["-Xmx${TOMCAT_MAX_MEM_MB:-1024}m", "-XX:MaxPermSize=${TOMCAT_MAX_PERM_MB:-512}m"]
EOF

# it would be nice if logs we written to stdout, but they aren't :(
# FIXME need to 'forever' the tail processes so they restart if failed, or just get tomcat to write to stdout
outFile=target/tomcat-out.txt
rm -f $outFile
touch $outFile
tail -f $outFile &

errFile=target/tomcat-err.txt
rm -f $errFile
touch $errFile
tail -f $errFile > /dev/stderr &

grailsEnv=${GRAILS_ENV:-development}

echo "[INFO] logging may not work, the log files live at out=$outFile, err=$errFile inside the container"

grails \
  -Dgrails.env=$grailsEnv \
  $@
