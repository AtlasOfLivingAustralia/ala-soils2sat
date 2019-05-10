#!/bin/bash
set -euo pipefail

cd `dirname "$0"`
cd ..

confDir=grails-app/conf
dsFile=$confDir/DataSource.groovy
configFile=$confDir/Config.groovy

# overwrite all the DB connection details, for all envs
sed -i "s+postgresql://.*\"+postgresql://${DBHOST:?}/${DBNAME:?}\"+" $dsFile
sed -i "s/username = \".*/username = \"${DBUSER:?}\"/" $dsFile
sed -i "s/password = \".*/password = \"${DBPASS:?}\"/" $dsFile

# config (managed in SettingService.groovy) is stored in the DB, you're just setting the *defaults* here
# you can configure all settings in the web UI by accessing http://localhost:8080/ala-soils2sat/admin/settings as an
# admin
sed -i "s+aekosServiceRoot = \".*+aekosServiceRoot = \"${DEFAULT_AEKOS_SERVICES_URL:?}\"+" $configFile
sed -i "s+doiS2SRoot = \".*+doiS2SRoot = \"${DOI_S2S_ROOT_URL:?}\"+" $configFile

# see https://grails.github.io/grails2-doc/2.2.1/ref/Command%20Line/run-war.html
cat <<EOF >> $confDir/BuildConfig.groovy
grails.tomcat.jvmArgs= ["-Xmx${TOMCAT_MAX_MEM_MB:-1024}m", "-XX:MaxPermSize=${TOMCAT_MAX_PERM_MB:-512}m"]
EOF

grailsEnv=${GRAILS_ENV:-development}

# pass 'run-app' to have logs written to stdout/err, 'run-war' writes to target/tomcat-{out,err}.txt
grails \
  -Ddisable.auto.recompile=${DISABLE_AUTO_RECOMPILE:-false} \
  -Dgrails.env=$grailsEnv \
  $@
