package com.celica.infinity.common.settings.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Requirement {
    private String name;
    private boolean required;
    private String type;
}
