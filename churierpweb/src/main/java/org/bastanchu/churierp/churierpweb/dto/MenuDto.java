package org.bastanchu.churierp.churierpweb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MenuDto {

    private String menuText;
    private String url;
    private String className;
    private List<MenuDto> children = new ArrayList<>();
}
