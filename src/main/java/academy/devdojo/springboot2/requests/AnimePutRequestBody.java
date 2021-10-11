package academy.devdojo.springboot2.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AnimePutRequestBody {

    private Long id;

    @NotEmpty(message = "The anime name cannot be empyt ")
    private String name;
}
