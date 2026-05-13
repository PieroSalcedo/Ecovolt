package upc.ecovolt.mapping.dto.notificationdto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.Notification;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "alertId", source = "alert.id")
    NotificationResponseDto toResponseDto(Notification entity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "alert.id", source = "alertId")
    Notification toEntity(NotificationRequestDto requestDto);

    List<NotificationResponseDto> toResponseDtoList(List<Notification> entityList);
}
