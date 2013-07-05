/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    username = "postgres"
    password = "password"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

// environment specific settings
environments {
    development {
        dataSource {
            url = "jdbc:postgresql://syn-db/soils2sat-dev"
//            url = "jdbc:postgresql://localhost/soils2sat-dev"
            dbCreate = "update"
        }
    }
    test {
        dataSource {
            url = "jdbc:postgresql://localhost/soils2sat-dev"
            dbCreate = "update"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:postgresql://localhost/soils2sat"
        }
    }
}
