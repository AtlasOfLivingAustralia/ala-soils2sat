package au.org.ala.soils2sat

class User implements Serializable {

	transient springSecurityService

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    UserApplicationState applicationState
    UserProfile userProfile

    static hasOne = [applicationState: UserApplicationState, userProfile: UserProfile]

	static constraints = {
		username blank: false, unique: true
		password blank: false
        applicationState nullable: true
        userProfile nullable: true
	}

	static mapping = {
        table '`user`'
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
