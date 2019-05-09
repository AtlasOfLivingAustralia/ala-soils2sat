#!/bin/bash
# redirect the root to the path with out webapp

urlPath=${PROXY_CONTEXT:?}
confDir=/etc/nginx/vhost.d

# delete any old config after a change of domain name
rm -f $confDir/*_location

# thanks https://stackoverflow.com/a/918931/1410035
# makes sure we use our custom nginx location config for all defined host names
IFS=',' read -ra ADDR <<< "${VIRTUAL_HOST_NAMES:?}"
for i in "${ADDR[@]}"; do
    echo "[INFO] creating config for host=$i"
    cat <<EOF > $confDir/${i}_location
location / {
  return 301 /${urlPath};
}

location /${urlPath} {
  proxy_pass http://${PROXY_HOST:?}:${PROXY_PORT:?}/${urlPath};
  proxy_no_cache 1; # disable caching
}
EOF
done
