import au.edu.aekos.shared.doiclient.service.DoiClientConfig
import au.edu.aekos.shared.doiclient.service.DoiClientServiceImpl
import au.org.ala.soils2sat.S2SUserDetailsService

// Place your Spring DSL code here
beans = {
    userDetailsService(S2SUserDetailsService)
}
