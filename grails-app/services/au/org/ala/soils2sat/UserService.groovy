package au.org.ala.soils2sat

class UserService {

    def deleteUser(User user) {
        // First delete user roles...
        def userRoles = UserRole.findAllByUser(user)
        userRoles.each {
            it.delete(flush: true)
        }
        // now delete the user itself
        user.delete(flush: true)
    }

    def isAdmin(User user) {
        UserRole.findByUserAndRole(user, Role.findByAuthority("ROLE_ADMIN")) ? true : false
    }

    def removeRoles(User user, List<String> authorities) {

        authorities.each { authority ->
            def role = Role.findByAuthority(authority)
            if (role) {
                def userRole = UserRole.findByUserAndRole(user, role)
                if (userRole) {
                    userRole.delete()
                }
            }
        }
    }

    def addRoles(User user, List<String> authorities) {

        authorities.each { authority ->
            def role= Role.findByAuthority(authority)
            def userRole = UserRole.findByUserAndRole(user, role)
            if (!userRole) {
                userRole = new UserRole(user:user, role: role)
                userRole.save(flush: true, failOnError: true)
            }
        }

    }
}
