package seok.springBank.domain.transactions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TransactionDto {
   private final String senderName;
   private final String receiverName;

   private final Long senderBalance;
   private final Long receiverBalance;
   private final String senderAccountNumber;
   private final String receiverAccountNumber;
   private final String name;
   private final LocalDateTime time;
   private final Long amount;

}
