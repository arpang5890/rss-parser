RSS parser

Spring Boot application that polls a RSS feed every 5 minutes, and
stores any items or updates or in an in-memory database ( H2). 

Exposed a end point to retrieving the 10 newest items
Example: GET http://<host>:<port>/items
Paginated, with direction based on a given field
Example: GET http://<host>:<port>/items?page=1&size=2&sort=updated_date&direction=asc