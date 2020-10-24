# webshop project


# lokal

Docker starten:

    docker-compose up 

Docker stoppen:
    
    docker-compose down 
    
Datenbank zurücksetzen:

    docker-compose down --rmi all
    
Docker im Hintergrund starten und laufen lassen:

    docker-compose up -d
    
   
 # Schnittstellendefinition
 
     /api/user
     GET /api/user/init
     Consumes: application/json
     Produces: application/json
     GET /api/user/login
     Consumes: application/json
     Produces: application/json
     POST /api/user/edit
     Consumes: application/json
     Produces: application/json
     POST /api/user/register
     Consumes: application/json
     Produces: application/json
     GET /api/user/list
     Consumes: application/json
     Produces: application/json
     /api/HelloWorld
     GET /api/HelloWorld/get
     Consumes: application/json
     Produces: application/json
     POST /api/HelloWorld/send
     Consumes: application/json
     Produces: application/json
  
  ## Akzeptierte Parameter für einen Benutzer: 
  
    String: username, password, street, country, postalCode, role, firstName, lastName
    Date:    birth
    
Diese Variablen müssen bei der Schnittstelle auch genau so übergeben werden.
   
 # Backend Test:
 
 ## Hello World
    curl -i -X GET http://localhost:8080/api/HelloWorld/get
 
    {"value":"Dies ist der Hello World Test um 2020-10-23 17:42:28"}
    
 ## User 
initiale Benutzerdaten laden: 

    curl -i -X GET http://localhost:8080/api/user/init
    
    HTTP/1.1 200 OK
    Content-Length: 140
    Content-Type: application/json
    
    [{"id":1,"password":"Test1234","username":"admin"},{"id":2,"birth":"1970-01-11T02:39:20.374Z[UTC]","password":"Test1234"
    
    
Liste Aller Benutzer 

    curl -i -X GET http://localhost:8080/api/user/list
    
    HTTP/1.1 200 OK
    Content-Length: 140
    Content-Type: application/json
    
    [{"id":1,"password":"Test1234","username":"admin"},{"id":2,"birth":"1970-01-11T02:39:20.374Z[UTC]","password":"Test1234"
    

Benutzer Registrieren:

Template: {
               
                  "password": "Test1234",
                  "role": "admin",
                  "username": "admin"
          }
          
per Post Request an: 
    
    localhost:8080/api/user/register
    
sofern der Benutzer bereits vorhanden ist kommt diese Exception: 

    Caused by: java.lang.Exception: Benutzer bereits vorhanden


          
          
 