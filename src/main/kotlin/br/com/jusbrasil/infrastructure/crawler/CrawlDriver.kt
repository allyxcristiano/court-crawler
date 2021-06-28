package br.com.jusbrasil.infrastructure.crawler

import br.com.jusbrasil.application.configurations.EnvironmentConfiguration
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL


object CrawlDriver {

    fun driver(environmentConfiguration: EnvironmentConfiguration): RemoteWebDriver {
        val options = ChromeOptions()
        options.addArguments("--headless", "--disable-gpu", "--blink-settings=imagesEnabled=false")
        return RemoteWebDriver(URL(environmentConfiguration.chromeWebDriverLocation), options)
    }
}
