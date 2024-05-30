package sapala.s2sauthservice.buildservice.build

//import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.contains
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sapala.s2sauthservice.buildservice.config.Env
import java.io.File
import java.util.*
import java.util.zip.ZipInputStream


@Service
class BuildService(private val env: Env, private val commandExecutor: CommandExecutor) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(BuildService::class.java)
    }

    private val mapper = XmlMapper()

    fun buildJar(zippedFile: MultipartFile): File {
        val buildId = UUID.randomUUID()
        val buildDir = env.buildDirectory().resolve(buildId.toString())
        zippedFile.inputStream.use { inputStream ->
            ZipInputStream(inputStream).use { ZipUtil.extractZip(it, buildDir) }
        }
        val dependency = buildDir.pomJsonNode().dependency()
        log.info("Will build project for $dependency")
        val successful = commandExecutor.executeInCmd("mvn package", buildDir)
        if (!successful) {
            log.info("Failed to build $dependency")
            throw RuntimeException()
        }
        return buildDir.resolve("target").resolve("${dependency.artifactId}-${dependency.version}.jar")
    }

    private fun File.pomJsonNode() = mapper.readTree(this.resolve("pom.xml"))
    private fun JsonNode.getDependencies() = get("dependencies").get("dependency") as ArrayNode
    private fun JsonNode.groupId() = get("groupId").asText()
    private fun JsonNode.artifactId() = get("artifactId").asText()
    private fun JsonNode.version() = if (contains("version")) get("version").asText() else null
    private fun JsonNode.dependency() = Dependency(this.groupId(), this.artifactId(), this.version())
}
