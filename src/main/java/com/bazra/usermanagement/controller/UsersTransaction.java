package com.bazra.usermanagement.controller;

import java.math.BigDecimal;
import java.util.*;

//import com.bazra.usermanagement.TransactionService;
import com.bazra.usermanagement.model.Transaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.bazra.usermanagement.model.Account;
import com.bazra.usermanagement.model.AccountService;
import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.repository.AccountRepository;
import com.bazra.usermanagement.repository.TransactionRepository;
import com.bazra.usermanagement.request.DepositRequest;
import com.bazra.usermanagement.request.TransferRequest;
import com.bazra.usermanagement.request.WithdrawRequest;
import com.bazra.usermanagement.response.BalanceResponse;
import com.bazra.usermanagement.response.ResponseError;
import com.bazra.usermanagement.response.TransactionResponse;

import javax.persistence.criteria.Order;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounts")
@Api(value = "Wallet  User's Activity  Endpoint", description = "This EndPoint Activities For Bazra  Wallet User's Activity")
@ApiResponses(value ={
        @ApiResponse(code = 404, message = "web user that a requested page is not available "),
        @ApiResponse(code = 200, message = "The request was received and understood and is being processed "),
        @ApiResponse(code = 201, message = "The request has been fulfilled and resulted in a new resource being created "),
        @ApiResponse(code = 401, message = "The client request has not been completed because it lacks valid authentication credentials for the requested resource. "),
        @ApiResponse(code = 403, message = "Forbidden response status code indicates that the server understands the request but refuses to authorize it. "),
        @ApiResponse(code = 400, message = "Bad Request or Bad Credentials Been Added . ")

})
public class UsersTransaction {

    //    @Autowired
//    TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository data;



    public UserInfo getCurrentUser(@AuthenticationPrincipal UserInfo user) {
        return user;
    }
    @GetMapping("/all")
    @ApiOperation(value ="This EndPoint To Get All Users Who User The Bazra Wallet")
    public List<Account> all() {
        return accountService.findAll();
    }

    @PostMapping("/sendmoney")
    @ApiOperation(value ="This Allows User To Transfer Money From One Account To Other. Post Method")
    public ResponseEntity<?> sendMoney(@RequestBody TransferRequest transferBalanceRequest, Authentication authentication) {

        return accountService.sendMoney(transferBalanceRequest,authentication.getName());
    }

    @PostMapping("/withdraw")
    @ApiOperation(value ="This EndPoint To WithDrawl Money From an Account. Post Method")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest,Authentication authentication) {

        return accountService.withdraw(withdrawRequest,authentication.getName());
    }

    @PostMapping("/deposit")
    @ApiOperation(value ="This EndPoint To Deposit Money From Account. Post Method")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest,Authentication authentication) {

        return accountService.Deposit(depositRequest,authentication.getName());
    }
    @PostMapping("/pay")
    @ApiOperation(value ="This EndPoint To Deposit Money From Account. Post Method")
    public ResponseEntity<?> pay(@RequestBody DepositRequest depositRequest,Authentication authentication) {

        return accountService.pay(depositRequest,authentication.getName());
    }
    @GetMapping("/transaction")
    @ApiOperation(value ="This EndPoint To Get All Transaction History. Get Method" )
    public ResponseEntity<?> transaction(Authentication authentication) {
        Account account = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
        if (account == null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }

        return ResponseEntity.ok(new TransactionResponse(transactionRepository.findByfromAccountNumberEquals(authentication.getName())));
//        return accountService.findall(transactionRequest.getAccountNumber());
    }

    @GetMapping("/balance")
    @ApiOperation(value ="This EndPoint Bring The Current Balance Get Method")
    public ResponseEntity<?> balance(Authentication authentication) {
        Account account= accountRepository.findByAccountNumberEquals(authentication.getName()).get();

        if (account==null) {
            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
        }
        BigDecimal balance = account.getBalance();
        return ResponseEntity.ok(new BalanceResponse(balance,"Your current balance equals "+balance,account.getUsername()));
    }


    //this for transaction by pagging
    private Sort.Direction getSortDirection(String direction){
        if(direction.equals("asc")){
            return Sort.Direction.ASC;
        }else if(direction.equals("desc")){
            return Sort.Direction.DESC;
        }return Sort.Direction.ASC;
    }

    @GetMapping("/Sorted")
    public ResponseEntity<List<Transaction> >getAllTransaction(@RequestParam(defaultValue = "transactionId,desc") String[] sort){
        try {
            List<Sort.Order> orders = new ArrayList<Sort.Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            }
            List<Transaction> transactions = transactionRepository.findAll(Sort.by(orders));
            if (transactions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }return new ResponseEntity<>(transactions, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
//    this is the previous get
//    @GetMapping("/TransactionPaged")
//    public ResponseEntity<Map<String, Object>> getAllTransactionPage(
//            Authentication authentication,
//            @RequestParam(required = false) String accountNumber,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "transactionId,desc") String[] sort){
//        Account account = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
////        if (account == null) {
////            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
////        }
////        return ResponseEntity.ok(new TransactionResponse(transactionRepository.findByfromAccountNumberEquals(authentication.getName())));
//
////    }
//
//        try{
//            List<Sort.Order> orders = new ArrayList<Sort.Order>();
//
//            if (sort[0].contains(",")) {
//                // will sort more than 2 fields
//                // sortOrder="field, direction"
//                for (String sortOrder : sort) {
//                    String[] _sort = sortOrder.split(",");
//                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
//                }
//            } else {
//                // sort=[field, direction]
//                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
//            }
//  List<Transaction>  transactions = new ArrayList<>();
//            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
//
//            Page<Transaction> pageTuts;
//            if(accountNumber == null)
//                pageTuts = transactionRepository.findAll(pagingSort);
//
//            else {
//                pageTuts = transactionRepository.findByAccountNumberContaining(accountNumber, pagingSort);
//            }
//            transactions = pageTuts.getContent();
//            Map<String, Object> response = new HashMap<>();
//            response.put("transactions", transactions);
//            response.put("currentPage", pageTuts.getNumber());
//            response.put("totalTransaction", pageTuts.getTotalElements());
//            response.put("totalPages", pageTuts.getTotalPages());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


//    this is the master get
@GetMapping("/TransactionPaged")
public ResponseEntity<Map<String, Object>> getAllTransactionPage(
        Authentication authentication,
        @RequestParam(required = false) String accountNumber,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "transactionId,desc") String[] sort){
    Account account = accountRepository.findByAccountNumberEquals(authentication.getName()).get();
//        if (account == null) {
//            return ResponseEntity.badRequest().body(new ResponseError("Invalid account"));
//        }
//        return ResponseEntity.ok(new TransactionResponse(transactionRepository.findByfromAccountNumberEquals(authentication.getName())));

//    }

    try{
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        List<Transaction>  transactions = new ArrayList<>();
        List<Transaction>  transactionss = new ArrayList<>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        List<Transaction>  trans = new ArrayList<>();
        Page<Transaction> pageTuts;
        Page<Transaction> pageTutss = null;
        if(authentication.getName().isEmpty())
            pageTuts = transactionRepository.findAll(pagingSort);

        else {
            pageTuts = transactionRepository.findByfromAccountNumberEquals(authentication.getName(), pagingSort);
            pageTutss =transactionRepository.findByaccountNumberEquals(authentication.getName(), pagingSort);
        }
        System.out.println(pageTuts);
        transactions = pageTuts.getContent();
        transactionss =pageTutss.getContent();
        for (int i = 0; i < transactionss.size(); i++) {
            transactionss.get(i).setUserID(transactions.get(0).getUserID());
        }
        trans.addAll(transactions);
        trans.addAll(transactionss);
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", trans);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalTransaction", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);


    } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/TransactionPaged/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("transactionId")long transactionId){
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if(transaction.isPresent()){
            return  new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("TransactionPaged/published")
    public ResponseEntity<Map<String, Object>> findByPublished(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try{
            List<Transaction>  transactions =new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);

            Page<Transaction> pageTuts = transactionRepository.findByAccountNumber(accountNumber, paging);
            transactions = pageTuts.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", transactions);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalTransactions", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}