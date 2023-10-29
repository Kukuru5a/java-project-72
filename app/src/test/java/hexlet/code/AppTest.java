package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {
    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }
    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Hello, World");
        });
    }
    @Test
    public void testHomePage() {
        JavalinTest.test(app, ((server, client) ->{
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        }
        ));
    }
    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testPostUrlPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.post("/urls");
            assertThat(response.code()).isEqualTo(200);
        }));
    }
}
