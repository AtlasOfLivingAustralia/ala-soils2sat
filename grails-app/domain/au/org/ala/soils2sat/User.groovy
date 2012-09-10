package au.org.ala.soils2sat

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    List<String> selectedPlots
    List<String> layers

    static hasMany = [selectedPlots:String, layers: String]


	static constraints = {
		username blank: false, unique: true
		password blank: false
        selectedPlots nullable: true
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
