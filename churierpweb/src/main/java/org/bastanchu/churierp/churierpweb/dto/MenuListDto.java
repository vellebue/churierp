package org.bastanchu.churierp.churierpweb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MenuListDto {

    private List<MenuDto> list;

}
