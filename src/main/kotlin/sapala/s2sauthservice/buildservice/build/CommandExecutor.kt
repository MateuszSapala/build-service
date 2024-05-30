package sapala.s2sauthservice.buildservice.build

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Service
class CommandExecutor {
    companion object {
        val log: Logger = LoggerFactory.getLogger(CommandExecutor::class.java)
    }

    fun executeInCmd(command: String, workingDir: File = File(".")): Boolean {
        try {
            val commands = mutableListOf("cmd.exe", "/c")
            commands.addAll(command.split(" "))
            val process = ProcessBuilder(commands)
                .redirectErrorStream(true)
                .directory(workingDir)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                log.info(line)
            }

            val exitCode = process.waitFor()
            log.info("Command '$command' exited with code $exitCode")
            return exitCode == 0
        } catch (ex: Exception) {
            log.error("Failed to execute command '$command'", ex)
            return false
        }
    }
}
