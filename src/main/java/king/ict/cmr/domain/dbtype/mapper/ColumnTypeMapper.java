package king.ict.cmr.domain.dbtype.mapper;

import king.ict.cmr.domain.enumeration.ColumnTypeEnum;
import king.ict.cmr.domain.tabledefinition.dto.DropdownDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ColumnTypeMapper {

  public List<DropdownDto> mapToDto(ColumnTypeEnum[] typeEnums) {
    return Arrays.stream(typeEnums).map(this::mapToDto).toList();
  }
  public DropdownDto mapToDto(ColumnTypeEnum typeEnum) {
    DropdownDto dto = new DropdownDto();
    dto.setId(typeEnum.getIdColumnType());
    dto.setValue(typeEnum.getValue());
    return dto;
  }

}
