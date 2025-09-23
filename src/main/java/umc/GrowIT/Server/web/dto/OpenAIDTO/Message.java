package umc.GrowIT.Server.web.dto.OpenAIDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;

    public void addContent (String additionalContent) {
        this.content = this.content + " " + additionalContent;
    }
}
