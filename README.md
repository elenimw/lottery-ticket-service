# lottery-ticket-service

#This is a lottery ticket services with database

# POST /api/tickets/purchase

Allow a user to purchase a lottery ticket, It deducts the ticket cost from 
the use's balance  and stores the ticket information 

- Inorder to do this, user must exist, user must have sufficient balance and ticket price must be defined