package sapala.s2sauthservice.buildservice.build

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import sapala.s2sauthservice.api.security.Authenticated
import java.nio.file.Files

@RestController
@RequestMapping("/build-service/api/v1/build")
class BuildController(private val buildService: BuildService) {
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    @Authenticated(["build-manager-service"])
    fun build(@RequestParam("zippedFile") zippedFile: MultipartFile): ByteArrayResource {
        val jar = buildService.buildJar(zippedFile)
        return ByteArrayResource(Files.readAllBytes(jar.toPath()))
    }
}
