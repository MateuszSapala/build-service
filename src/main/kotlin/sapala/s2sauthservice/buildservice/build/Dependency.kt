package sapala.s2sauthservice.buildservice.build

data class Dependency(val groupId: String, val artifactId: String,val version: String?) {
    override fun toString() = "$groupId.$artifactId"
}
