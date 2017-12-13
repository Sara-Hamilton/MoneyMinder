# MoneyMinder
a financial web app that allows people to create multiple accounts, unify data across those accounts, categorize and track spending and savings, and set aside funds for specific purposes

## Built With
* Java
* Spring
* MAMP
* SQLAlchemy
* HTML
* CSS
* Bootstrap

## Functionality

### Login
A user signs up to create a new account or logs in to an existing account.  Submitted info is verified for accuracy and checked against existing data in database.  Descriptive error messages appear on screen in case of errors.
Once all user info is verified, a user session is created. Navbar options expand allowing a user to view his/her accounts, categories, and transactions.  

### Accounts
A new user has no accounts.  A user can create accounts by selecting the Add Account button.  The user can name each account and has the option of setting a Min (a minimum amount to try to maintain in the account) and/or a Goal (a desired amount to try to achieve).  The minimum amount will be highlighted red on the accounts page if the account total is below or equal to the minimum.  Likewise, the goal amount will be highlighted green if the account total equals or exceeds the goal amount.  A user can edit accounts.  An account name can be changed at any time.  A user may delete any account that has not been transacted against.  When a user clicks on an account, the transaction history for that account is displayed in a table.

### Categories
When a new user is created, fifteen default categories are created.  A user may change the name of any category at any time.  A user may delete any category that has not been transacted against.  New categories can be created.  Even though the app comes with a pre-defined set of categories, the categories are completely customizeable.  

### Transactions
A user may make a deposit or a withdrawal of any amount into or from any account.  Assigning a category to the transaction is optional.  Assigning a date is required.  Including a description of the transaction is optional.  

### Transfer
A user may transfer any amount from any account to another account.  This is essentially a withdrawal from one account and a transfer into another account.  Assigning a category to the transfer is optional.  Assigning a date is required.  Including a description of the transfer is optional.

### View Transactions by Account, Category, and Date Range
A user may view all transactions within a specified date range.  Sorting by account and/or category is optional. A table will display all transactions within the specified parameters and show the total amount that was transacted.   

### Logout
User session is deleted and user is logged out.  The navbar options retract to only allow access to the main page and to login.  



## User Stories

### Kim
Kim wants to save money.  The dividend that is payed out on her checking account through the local credit union is higher than the interest rates on any savings accounts or CDs she can find.  She decides to keep all of her money in her checking account and creates an account in the MoneyMinder app called Savings to keep her savings and her spending money separate.

### Peter
Peter has three different checking accounts in three different banks.  He also has one savings account and two CDs that he wants to keep track of.  He creates a different account in the MoneyMinder app for each of his accounts.  The app shows him the total amount of all of his separate accounts.

### John and Linda
John and Linda share a checking account.  They want to make sure that they have enough money to cover necessary expenses without overspending.  They create an account called Home which they transfer $2500 to each month to cover all costs associated with their housing: mortgage, utilities, maintenance, etc.  They transfer $300 a month to an account called Vacation.  When the Goal amount of $4000 is reached, they plan to go on a trip to Hawaii.  They each have their own fun money account.  This is money that each of them can spend on whatever they want without having to check with each other.  $250 is transferred into each fun money account each month.  All other money goes in a general fund to be spent on food, clothing, and life's other daily expenses.  

### Kevin
Kevin has paid off his mortgage and owns his own home.  He wants to make sure that he has enough money ready to pay his property tax and house insurance bills when they come around once a year.  He creates an account called Escrow that he transfers $500 to each month.  This will prevent him from thinking that he has more money to spend than he actually does.

