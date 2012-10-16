import au.org.ala.soils2sat.Role
import au.org.ala.soils2sat.User
import au.org.ala.soils2sat.UserRole

class BootStrap {

    def init = { servletContext ->

        def roles = ['ROLE_USER','ROLE_ADMIN']

        for (String role : roles) {
            Role.findByAuthority(role) ?: new Role(authority: role).save(failOnError: true)
        }

        def admin = User.findByUsername('admin')
        if (!admin) {
            admin = new User(username: 'admin', password:'P@ssw0rd', enabled: true, accountExpired: false, accountLocked: false)
            admin.save()
        }

        ['ROLE_USER','ROLE_ADMIN'].each {
            def role= Role.findByAuthority(it)
            def userRole = UserRole.findByUserAndRole(admin, role)
            if (!userRole) {
                userRole = new UserRole(user:admin, role: role)
                userRole.save(flush: true, failOnError: true)
            }
        }

    }

    def destroy = {
    }

}
