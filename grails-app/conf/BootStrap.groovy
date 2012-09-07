import au.org.ala.soils2sat.Role

class BootStrap {

    def init = { servletContext ->
        def roles = ['ROLE_USER','ROLE_ADMIN']

        for (String role : roles) {
            Role.findByAuthority(role) ?: new Role(authority: role).save(failOnError: true)
        }

    }
    def destroy = {
    }
}
