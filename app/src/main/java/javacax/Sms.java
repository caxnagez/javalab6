package javacax;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sms {
    private String phoneNumber;
    private String message;
}