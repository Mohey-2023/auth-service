package com.mohey.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class DeleteReqDto {

    @NotEmpty
    @Size(min = 4, max = 100)
    private String memberUuid;
    
}
