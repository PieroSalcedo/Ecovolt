package upc.ecovolt.mapping.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import upc.ecovolt.entity.SubscriptionPlan;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionPlanMapper extends GenericMapper<SubscriptionPlan, SubscriptionPlanDto.Request, SubscriptionPlanDto.Response> {
    // Los nombres de campos coinciden entre Entidad y DTO
}
