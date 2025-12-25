package io.github.khayrulinnikita

class ServerConfig {
    String name, description
    HttpConfig http
    HttpsConfig https
    List<Mapping> mappings = []

    String mappingsFmt() {
        String resp = ""
        mappings.each {
            resp += """
            url: ${it.url}
            active: ${it.active}
            """
        }
        return resp
    }

    String toString() {
        """
        ### Server configuration ###
        name: ${name}
        description: ${description}
        http: ${http}
        https: ${https}
        mappings: ${mappingsFmt()}
        """.stripIndent()
    }
}

class HttpConfig {
    int port
    boolean secure

    String toString() {
        """
        \tport: ${port}
        \tsecure: ${secure}
        """
    }
}

class HttpsConfig extends HttpConfig {}

class Mapping {
    String url
    boolean active
}