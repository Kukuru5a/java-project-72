package hexlet.code;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controllers.UrlController;
import hexlet.code.repositories.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.AppUtils.createTemplateEngine;
import static hexlet.code.AppUtils.getDataBaseUrl;


@Slf4j
public class App{

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(getPort());
    }
    public static Javalin getApp() throws IOException, SQLException {
        System.setProperty("h2.traceLevel", "TRACE_LEVEL_SYSTEM_OUT=4");

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDataBaseUrl());
        var dataSource = new HikariDataSource(hikariConfig);
        var sql = getResourceFileAsString("schema.sql");
//      conn

        log.info(sql);
        try(var conn = dataSource.getConnection();
            var statement = conn.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });



        app.get("/", ctx -> {
            ctx.render("index.jte");
        });

        app.post("/urls", UrlController::addUrl);
        app.get("/urls", UrlController::urlList);
        app.get("/urls/{id}", UrlController::showUrl);
//        app.get("/urls", ctx -> {
//            ctx.render("urls/urls.jte");
//        });
//        app.post("/urls", UrlController::add);


        JavalinJte.init(createTemplateEngine());
        return app;

    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader cl = App.class.getClassLoader();
        return cl.getResourceAsStream(fileName);
    }
    public static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);

        if(is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }
}