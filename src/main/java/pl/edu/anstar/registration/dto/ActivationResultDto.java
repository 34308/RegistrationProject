package pl.edu.anstar.registration.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivationResultDto {
    private String message;
    private String operationState;
}
