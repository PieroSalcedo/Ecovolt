package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.SubscriptionPlan;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionPlanMapper extends GenericMapper<SubscriptionPlan, SubscriptionPlanDto.Request, SubscriptionPlanDto.Response> {
    @Override
    @org.mapstruct.Mapping(target = "supportLevel.idDataCatalog", source = "supportLevelId")
    SubscriptionPlan toEntity(SubscriptionPlanDto.Request requestDto);

    @Override
    @org.mapstruct.Mapping(target = "supportLevelId", source = "supportLevel.idDataCatalog")
    @org.mapstruct.Mapping(target = "supportLevelDescription", source = "supportLevel.description")
    SubscriptionPlanDto.Response toResponseDto(SubscriptionPlan entity);
}
