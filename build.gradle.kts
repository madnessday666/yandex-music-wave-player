plugins {
    id("java")
    id("org.openjfx.javafxplugin") version ("0.1.0")
    id("org.beryx.jlink") version ("2.26.0")
}

group = "app"
version = "0.1"

repositories {
    mavenCentral()
}

jlink {
    options = listOf(
            "--add-modules=jdk.crypto.ec"
    )
    launcher {
        name = "yandex-music-wave-player"
    }
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media", "javafx.graphics")
}

application {
    mainModule = "yandex.music.wave.player.main"
    mainClass = "ymwp.YandexMusicWavePlayer"
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.yaml:snakeyaml:2.2")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

