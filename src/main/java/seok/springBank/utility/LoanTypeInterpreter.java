package seok.springBank.utility;

public class LoanTypeInterpreter{
    public static String translate(String target, String type){
        if (type.equals("worker")){
            if (target.equals("title")) return "직장인 안심 신용대출";
        }
        if (type.equals("house")){
            if (target.equals("title")) return "주택 담보 대출";
        }
        if (type.equals("business")){
            if (target.equals("title")) return "든든 자영업자 대출";
        }

        throw new IllegalArgumentException("Invalid Access");
    }

}
