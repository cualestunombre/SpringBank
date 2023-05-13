package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "COMMODITY_ACCOUNT")
public class CommodityAccount extends Account{
}
