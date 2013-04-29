package au.org.ala.soils2sat

class UserProfile implements Serializable {

    String surname
    String givenNames
    String institution
    String contactNumber

    static belongsTo = [user:User]

    static constraints = {
        surname nullable: true
        givenNames nullable: true
        institution nullable: true
        contactNumber nullable: true
    }

}
