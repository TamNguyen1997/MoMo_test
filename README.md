# MoMo_test

## Introduction

This is an application which provides bill payment service.

User can view, create, delete, reschedule, search bills, add fund to account and pay.

User's fund is loaded from file `src/main/resources/user.txt`. Only _1 number_ is in the file.

Bills are loaded from file `src/main/resources/bill.txt`. Each row in file represents a bill, the values of a bill are
separated by `:`.

## Usage
### All commands are case-sensitive

| Command                 |              Description              |
|-------------------------|:-------------------------------------:|
| CASH_IN                 |         Add fund into account         |
| LIST_BILL               |            List all bills             |
| PAY                     |          Pay 1 or many bills          |
| SCHEDULE                |       Change due date of a bill       |
| LIST_PAYMENT            | List all bills with state not NOT_PAY |
| SEARCH_BILL_BY_PROVIDER |     Search all bills by provider      |
| DELETE                  |             Delete a bill             |
| DUE_DATE                |        List all overdue bills         |
| CREATE                  |            Create new bill            |
| EXIT                    |             Exit program              |

### Steps to run the program

1. Navigate to `src/main/java/momo/payment/service`
2. Compile and run the application and provide arguments

- Syntax to run using Gradle `gradlew run --args='LIST_BILL'`
- Run with IntelliJ
    - Edit Run Configuration
      ![Edit Run Configuration](readme-resource/Edit%20Run%20Configuration.png)
    - Add arguments
      ![Add arguments](readme-resource/Add%20arguments.png)

### CASH_IN

CASH_IN command adds a fund into account. Syntax to run `gradlew run --args='CASH_IN 1000'`. First argument is the
command `CASH_IN`, the second argument is the amount of fund to add.

After running, the total fund is saved in `src/main/resources/user.txt`.

### LIST_BILL

LIST_BILL command prints all bill. Syntax to run `gradlew run --args='LIST_BILL'`

### PAY

PAY command pays 1 or many bills. Syntax to run `gradlew run --args='CASH_IN 1 2 3'`. First argument is the
command `PAY`, numbers which come after the command are bill IDs.

If account fund is insufficient to pay all bill, exception is thrown.

### SCHEDULE

SCHEDULE command updates a bill's due date. Syntax to run `gradlew run --args='SCHEDULE 20/10/2024'`. First argument is
the command `SCHEDULE`, only 1 argument comes after the command and has to be in `dd/MM/yyyy`.

### LIST_PAYMENT

LIST_PAYMENT command prints all bills that have state `PENDING`, `PROCESSED` or `PAID`. Syntax to
run `gradlew run --args='LIST_PAYMENT'`

### SEARCH_BILL_BY_PROVIDER

SEARCH_BILL_BY_PROVIDER command prints all bills filter by provider. Syntax to
run `gradlew run --args='SEARCH_BILL_BY_PROVIDER VNPT'`, the second argument is search key.

### DELETE

DELETE command deletes a bill. Syntax to run `gradlew run --args='DELETE 1'`. First argument is the command `DELETE`,
the second argument is bill ID.

### DUE_DATE

DUE_DATE command prints all bills that are overdue. Syntax to run `gradlew run --args='DUE_DATE'`

### CREATE

CREATE command creates new bill. Syntax to run `gradlew run --args='CREATE ELECTRIC 200000 13/03/2024 NOT_PAID MoMo'`.

| Argument number |   Description    |
|-----------------|:----------------:|
| 1               | `CREATE` command |
| 2               |    Bill type     |
| 3               |      Amount      |
| 4               |     Due date     |
| 5               |   Bill status    |
| 6               |     Provider     |

Bill ID auto-increments.
For example, there are 3 bills with ID 1, 2 and 3.
After execute the command the fourth bill will have ID 4.

### EXIT
EXIT to exit the program.

## Future improvement
1. `CREATE` command 
Right now, there aren't many validations to check if inputs are valid. 
For example, check if due date is in correct format.
2. The application terminates itself after running any command, it would be nice if it stays alive and wait for only `EXIT` command to terminate.
3. Tests shouldn't use the same data as the application as it is at the moment. There should be a separate data set for tests.
