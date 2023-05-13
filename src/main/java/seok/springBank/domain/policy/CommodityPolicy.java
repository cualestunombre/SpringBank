package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue(value = "COMMODITY_POLICY")
@Entity
public class CommodityPolicy extends Policy{



}