package hexlet.code.dto.urls;

import hexlet.code.models.Url;
import hexlet.code.dto.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@AllArgsConstructor

@ToString
public class UrlPage extends BasePage {
    private Url url;
}